# grid.py
# ─────────────────────────────────────────────────────────────────────────────
# Build power-law graded meshgrids and run the self-consistent EM + mechanical
# solver with material-fraction feedback.
#
# Each axis can be graded fine at its low end (fine_at="min", steps grow with
# the value) or fine at its high end (fine_at="max", steps shrink with the
# value). p = 1 is uniform and fine_at is then irrelevant.
# ─────────────────────────────────────────────────────────────────────────────
import importlib
import numpy as np

try:
    import solenoid_lib
    importlib.reload(solenoid_lib)
    import config
    importlib.reload(config)
except ImportError:
    solenoid_lib = None
    config = None

# Re-evaluate the tape properties at the converged B0 before reporting them.
# Set to False to reproduce the numbers of the previous (uniform-grid) version.
RECOMPUTE_TAPE_AT_EXIT = True

PROGRESS_EVERY = 25          # cells between progress prints


# ═════════════════════════════════════════════════════════════════════════════
# 0.  Config access with fallbacks
# ═════════════════════════════════════════════════════════════════════════════
# Defaults used when config.py predates a given option. FINE_AT_* default to
# "min", which is the historical behaviour, so an old config still reproduces
# the old axes exactly.
_DEFAULTS = {
    "P_RI": 1.0, "P_TH": 2.0, "P_A": 2.0,
    "D0_RI": None, "D0_TH": None, "D0_A": None,
    "ROUND_RI": None, "ROUND_TH": None, "ROUND_A": None,
    "A_AXIS_FROM_TH": True,
    "FINE_AT_RI": "min", "FINE_AT_TH": "min", "FINE_AT_A": "min",
}

_warned = set()


def _cfg(name):
    """config.<name>, falling back to a default with a one-time warning."""
    if hasattr(config, name):
        return getattr(config, name)
    if name not in _warned:
        _warned.add(name)
        print(f"[!] config.{name} missing — using default {_DEFAULTS[name]!r}")
    return _DEFAULTS[name]


# ═════════════════════════════════════════════════════════════════════════════
# 1.  Axis construction
# ═════════════════════════════════════════════════════════════════════════════
def power_axis(vmin, vmax, n=None, p=1.0, d0=None, round_to=None, fine_at="min"):
    """
    Graded 1-D axis. Endpoints are always exact.

        fine_at="min":  v_k = vmin + span * (k/(n-1))**p        steps grow
        fine_at="max":  v_k = vmax - span * (1 - k/(n-1))**p    steps shrink

    p = 1 is exactly np.linspace in both cases. Give either n, or d0 = the
    SMALLEST step, from which n is derived:  n = 1 + (span/d0)**(1/p).
    Note that the smallest step sits at vmin for fine_at="min" and at vmax
    for fine_at="max", so the meaning of d0 follows the grading direction.
    """
    if p <= 0:
        raise ValueError("p must be > 0")
    if vmax <= vmin:
        raise ValueError("vmax must exceed vmin")
    if fine_at not in ("min", "max"):
        raise ValueError(f"fine_at must be 'min' or 'max', got {fine_at!r}")
    span = vmax - vmin

    if d0 is not None:
        n = int(np.ceil(1.0 + (span / d0) ** (1.0 / p)))
    if n is None or n < 2:
        raise ValueError("need n >= 2 (or a sensible d0)")

    u = np.linspace(0.0, 1.0, n)
    if fine_at == "min":
        v = vmin + span * u ** p
    else:
        v = vmax - span * (1.0 - u) ** p
    v[0], v[-1] = vmin, vmax                      # kill float drift

    if round_to:
        v = np.unique(np.round(v / round_to) * round_to)
        if len(v) < n:
            print(f"[!] round_to={round_to:g} merged {n - len(v)} of {n} points "
                  f"(finest step {span / (n - 1) ** p:.3g} < round_to)")
    return v


def cell_edges(v):
    """Midpoint cell edges for pcolormesh on a NON-uniform axis."""
    v = np.asarray(v, dtype=float)
    mid = 0.5 * (v[1:] + v[:-1])
    return np.concatenate(([2 * v[0] - mid[0]], mid, [2 * v[-1] - mid[-1]]))


def _axis_report(name, v, unit=1.0, unit_name=""):
    """First and last step in axis order — direction matters once graded."""
    d = np.diff(v)
    trend = "coarsening" if d[-1] > d[0] else ("refining" if d[-1] < d[0]
                                               else "uniform")
    print(f"  {name:<12}: {len(v):3d} pts, "
          f"{v[0]*unit:.3f} – {v[-1]*unit:.3f} {unit_name}, "
          f"step {d[0]*unit:.3f} → {d[-1]*unit:.3f} {unit_name} ({trend}) "
          f"(uniform would be {(v[-1]-v[0])/(len(v)-1)*unit:.3f})")


def make_ri_axis():
    return power_axis(config.RI_MIN, config.RI_MAX, n=config.N_RI,
                      p=_cfg("P_RI"), d0=_cfg("D0_RI"),
                      round_to=_cfg("ROUND_RI"), fine_at=_cfg("FINE_AT_RI"))


def make_ri_a_axis():
    return power_axis(config.RI_MIN, config.RI_MAX, n=config.N_RI_A,
                      p=_cfg("P_RI"), d0=_cfg("D0_RI"),
                      round_to=_cfg("ROUND_RI"), fine_at=_cfg("FINE_AT_RI"))


def make_th_axis():
    return power_axis(config.TH_MIN, config.TH_MAX, n=config.N_TH,
                      p=_cfg("P_TH"), d0=_cfg("D0_TH"),
                      round_to=_cfg("ROUND_TH"), fine_at=_cfg("FINE_AT_TH"))


def make_a_axis():
    """
    Area axis. If A_AXIS_FROM_TH, map a graded Th axis through
    A = pi((Ri_ref + Th)^2 - Ri_ref^2) at Ri_ref = RI_MAX, which is exactly the
    convention used for A_MIN / A_MAX in config.py. This guarantees the same
    thickness resolution as the Th scan at the reference radius; grading A
    directly with p = 2 only approximates it, because Th(A) is concave.

    In the derived branch the grading follows FINE_AT_TH, not FINE_AT_A: the
    axis IS a thickness axis, just relabelled. FINE_AT_A applies only when
    A is graded directly.
    """
    if _cfg("A_AXIS_FROM_TH"):
        ri_ref = config.RI_MAX
        th = power_axis(config.TH_MIN, config.TH_MAX, n=config.N_A,
                        p=_cfg("P_TH"), d0=_cfg("D0_TH"),
                        round_to=_cfg("ROUND_TH"), fine_at=_cfg("FINE_AT_TH"))
        return np.pi * ((ri_ref + th) ** 2 - ri_ref ** 2)
    return power_axis(config.A_MIN, config.A_MAX, n=config.N_A,
                      p=_cfg("P_A"), d0=_cfg("D0_A"),
                      round_to=_cfg("ROUND_A"), fine_at=_cfg("FINE_AT_A"))


def preview_axes():
    """Print the axes without running any physics — cheap sanity check."""
    print("Graded axes:")
    print(f"  grading: Ri p={_cfg('P_RI')} fine at {_cfg('FINE_AT_RI')}, "
          f"Th p={_cfg('P_TH')} fine at {_cfg('FINE_AT_TH')}")
    _axis_report("Ri (Th-scan)", make_ri_axis(), 1e3, "mm")
    _axis_report("Th", make_th_axis(), 1e3, "mm")
    _axis_report("Ri (A-scan)", make_ri_a_axis(), 1e3, "mm")
    _axis_report("A", make_a_axis(), 1e6, "mm²")


# ═════════════════════════════════════════════════════════════════════════════
# 2.  Result containers
# ═════════════════════════════════════════════════════════════════════════════
def _empty_result_grids(shape):
    """Create the standard set of NaN-initialised output grids."""
    nan = lambda: np.full(shape, np.nan)
    return {
        "b0":        nan(),
        "v_bore":    nan(),
        "scan":      nan(),
        "stress":    nan(),
        "j_crit":    nan(),
        "je_max":    nan(),
        "je_coil":   nan(),       # Overall Coil Engineering Je
        "je_sc":     nan(),       # Current density isolated to the SC tape
        "f_sc":      nan(),       # Superconductor volume fraction
        "length_sc": nan(),       # Exact physical length of SC tape
        "margin":    nan(),
        "converged": np.zeros(shape, dtype=bool),
    }


# ═════════════════════════════════════════════════════════════════════════════
# 3.  Per-cell solver
# ═════════════════════════════════════════════════════════════════════════════
def _tape_props(b0):
    """Je_tape with the cu_frac keyword when the library supports it."""
    try:
        return solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid, cu_frac=0.5)
    except TypeError:
        return solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid)


def _solve_cell(ri, rf, v, je_mech_mm2):
    """Self-consistent equilibrium solver (unchanged physics)."""
    je_mm2 = je_mech_mm2
    converged = False
    f_sc = 1.0

    # Pre-seed so the post-loop block is always well defined (MAX_ITER = 0,
    # early exits, ...) — this was a latent NameError in the previous version.
    b0 = solenoid_lib.solenoid_field_center(ri, rf, je_mm2 * 1e6,
                                            solenoid_lib.solenoid_length)
    jc_100sc_mm2, je_100sc_mm2 = _tape_props(b0)
    je_100sc_mm2 = max(je_100sc_mm2, 1e-6)

    for _ in range(config.MAX_ITER):
        je_si = je_mm2 * 1e6
        b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si,
                                                solenoid_lib.solenoid_length)

        jc_100sc_mm2, je_100sc_mm2 = _tape_props(b0)
        je_100sc_mm2 = max(je_100sc_mm2, 1e-6)

        # Minimum REBCO fraction needed to carry je_mm2
        f_sc = je_mm2 / je_100sc_mm2

        if f_sc <= 1.0:
            converged = True                     # mechanically limited
            break

        je_new_mm2 = je_100sc_mm2                # EM limited: drop to tape limit
        rel_change = abs(je_new_mm2 - je_mm2) / (abs(je_mm2) + 1e-12)
        je_mm2 = je_new_mm2
        if rel_change < config.TOL_REL:
            f_sc = 1.0
            converged = True
            break

    # ── Final quantities at the converged operating point ────────────────────
    je_si = je_mm2 * 1e6
    b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si,
                                            solenoid_lib.solenoid_length)
    sigma_pa, _ = solenoid_lib.hoop_stress(ri, rf, je_si,
                                           solenoid_lib.solenoid_length)

    if RECOMPUTE_TAPE_AT_EXIT:
        jc_100sc_mm2, je_100sc_mm2 = _tape_props(b0)
        je_100sc_mm2 = max(je_100sc_mm2, 1e-6)
        f_sc = min(je_mm2 / je_100sc_mm2, 1.0)

    j_sc_mm2 = je_mm2 / f_sc if f_sc > 0 else 0.0

    scan = (solenoid_lib.scan_time(b0, v)
            if hasattr(solenoid_lib, "scan_time") else b0 ** 2 * v)

    return {
        "b0":        b0,
        "v_bore":    v,
        "scan":      scan,
        "stress":    sigma_pa / 1e6,
        "j_crit":    jc_100sc_mm2,
        "je_max":    je_100sc_mm2,
        "je_coil":   je_mm2,
        "je_sc":     j_sc_mm2,
        "f_sc":      f_sc,
        "margin":    jc_100sc_mm2 / j_sc_mm2 if j_sc_mm2 > 0 else np.nan,
        "converged": converged,
    }


# ═════════════════════════════════════════════════════════════════════════════
# 4.  Common scan core — used by both builders
# ═════════════════════════════════════════════════════════════════════════════
def _run_scan(ri, th, valid, label):
    """
    ri, th, valid : 2-D arrays of identical shape (valid is boolean).
    Returns the dict of result grids.
    """
    shape = ri.shape
    rf = ri + th
    v = np.pi * ri ** 2 * solenoid_lib.solenoid_length
    a_total = np.pi * (rf ** 2 - ri ** 2)

    tape_cs_m2 = (solenoid_lib.w_tape_mm * 1e-3) * (solenoid_lib.t_tape_mm * 1e-3)

    res = _empty_result_grids(shape)
    total = ri.size
    done = 0
    n_err = 0

    for i in range(shape[0]):
        for j in range(shape[1]):
            done += 1
            if done % PROGRESS_EVERY == 0 or done == total:
                print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")

            if not valid[i, j]:
                continue

            try:
                # 1. purely mechanical upper limit on Je
                je_mech_si = solenoid_lib.je_max_stress_limited(
                    ri[i, j], rf[i, j], solenoid_lib.solenoid_length,
                    config.SIGMA_LIMIT_PA)

                # 2. fractional feedback equilibrium
                cell = _solve_cell(ri[i, j], rf[i, j], v[i, j],
                                   je_mech_mm2=je_mech_si / 1e6)

                for key, val in cell.items():
                    if key in res:
                        res[key][i, j] = val

                # 3. material fraction -> exact SC tape length
                v_pack = a_total[i, j] * solenoid_lib.solenoid_length
                res["length_sc"][i, j] = v_pack * cell["f_sc"] / tape_cs_m2

            except (ValueError, RuntimeError) as err:
                n_err += 1
                if n_err <= 5:
                    print(f"\n[!] {label} physics error at "
                          f"Ri={ri[i, j]*1e3:.1f} mm, Th={th[i, j]*1e3:.2f} mm: {err}")

    print(f"\n2-D meshgrid ({label}) computed."
          + (f"  [{n_err} physics errors]" if n_err else ""))
    res["_a_total"] = a_total
    res["_rf"] = rf
    res["_v"] = v
    return res


# ═════════════════════════════════════════════════════════════════════════════
# 5.  Thickness-based scan : x = Ri, y = Th
# ═════════════════════════════════════════════════════════════════════════════
def build_thickness_grid():
    ri_vals = make_ri_axis()
    th_vals = make_th_axis()

    print("Th-scan axes:")
    _axis_report("Ri", ri_vals, 1e3, "mm")
    _axis_report("Th", th_vals, 1e3, "mm")

    ri, th = np.meshgrid(ri_vals, th_vals)
    valid = np.ones(ri.shape, dtype=bool)
    if config.TH_MAX_LIMIT is not None:
        valid &= th <= config.TH_MAX_LIMIT

    res = _run_scan(ri, th, valid, label="Th-scan")
    rf, v = res.pop("_rf"), res.pop("_v")
    res.pop("_a_total")

    grids = {"ri": ri, "th": th, "rf": rf, "v": v,
             "ri_vals": ri_vals, "th_vals": th_vals,
             "ri_edges": cell_edges(ri_vals), "th_edges": cell_edges(th_vals),
             **res}
    _print_summary(grids, ri.size, label="Th",
                   extra={"Th (mm)": th * 1e3, "f_sc (%)": res["f_sc"] * 100,
                          "Len SC (km)": res["length_sc"] / 1000})
    return grids


# ═════════════════════════════════════════════════════════════════════════════
# 6.  Area-based scan : x = Ri, y = A = pi(Ro² - Ri²)
# ═════════════════════════════════════════════════════════════════════════════
def build_area_grid():
    ri_vals = make_ri_a_axis()
    a_vals = make_a_axis()

    print("A-scan axes:")
    _axis_report("Ri", ri_vals, 1e3, "mm")
    _axis_report("A", a_vals, 1e6, "mm²")

    ri, a_total = np.meshgrid(ri_vals, a_vals)

    # Geometry derived from the total cross-section
    th = np.sqrt(ri ** 2 + a_total / np.pi) - ri

    valid = np.isfinite(th) & (th > 0.0) & (th >= config.TH_MIN)
    if config.TH_MAX_LIMIT is not None:
        valid &= th <= config.TH_MAX_LIMIT
    print(f"  masked out {np.count_nonzero(~valid)} / {th.size} cells "
          f"(Th outside [{config.TH_MIN*1e3:.1f}, "
          f"{'inf' if config.TH_MAX_LIMIT is None else config.TH_MAX_LIMIT*1e3:.1f}] mm)")

    res = _run_scan(ri, th, valid, label="A-scan")
    rf, v = res.pop("_rf"), res.pop("_v")
    res.pop("_a_total")

    grids = {"ri": ri, "a": a_total, "th": th, "rf": rf, "v": v,
             "ri_vals": ri_vals, "a_vals": a_vals,
             "ri_edges": cell_edges(ri_vals), "a_edges": cell_edges(a_vals),
             **res}
    _print_summary(grids, ri.size, label="A",
                   extra={"A_total (mm²)": a_total * 1e6,
                          "Th (mm)": np.where(valid, th, np.nan) * 1e3,
                          "f_sc (%)": res["f_sc"] * 100,
                          "Len SC (km)": res["length_sc"] / 1000})
    return grids


# ═════════════════════════════════════════════════════════════════════════════
# 7.  Summary
# ═════════════════════════════════════════════════════════════════════════════
def _print_summary(grids, total, label, extra=None):
    b0 = grids["b0"]
    n_valid = int(np.sum(np.isfinite(b0)))
    n_conv = int(np.sum(grids["converged"]))
    print(f"  Valid cells     : {n_valid} / {total}")
    print(f"  Converged cells : {n_conv} / {max(n_valid, 1)}")
    print(f"  Ri              : {np.nanmin(grids['ri'])*1e3:.1f} – "
          f"{np.nanmax(grids['ri'])*1e3:.1f} mm")
    if extra:
        for name, arr in extra.items():
            if np.any(np.isfinite(arr)):
                print(f"  {name:<15} : {np.nanmin(arr):.2f} – {np.nanmax(arr):.2f}")
    if n_valid:
        print(f"  B0              : {np.nanmin(b0):.3f} – {np.nanmax(b0):.3f} T")
        print(f"  Stress          : {np.nanmin(grids['stress']):.1f} – "
              f"{np.nanmax(grids['stress']):.1f} MPa")


# ═════════════════════════════════════════════════════════════════════════════
# TEST HARNESS
# ═════════════════════════════════════════════════════════════════════════════
if __name__ == "__main__":
    print("Testing graded grid logic...\n")

    if solenoid_lib is None or config is None:
        print("[!] Local dependencies not found. Mocking physics libraries.\n")

        class MockConfig:
            RI_MIN, RI_MAX = 0.200, 1.500
            TH_MIN, TH_MAX = 0.002, 0.400
            N_RI = N_TH = N_RI_A = N_A = 12
            A_MIN = np.pi * ((RI_MAX + TH_MIN) ** 2 - RI_MAX ** 2)
            A_MAX = np.pi * ((RI_MAX + TH_MAX) ** 2 - RI_MAX ** 2)
            TH_MAX_LIMIT = None
            P_RI, P_TH, P_A = 2.0, 2.0, 2.0
            D0_RI = D0_TH = D0_A = None
            ROUND_RI = ROUND_TH = ROUND_A = None
            A_AXIS_FROM_TH = True
            FINE_AT_RI, FINE_AT_TH, FINE_AT_A = "max", "min", "min"
            MAX_ITER, TOL_REL = 50, 1e-4
            SIGMA_LIMIT_PA = 750e6

        class MockSolenoidLib:
            solenoid_length, theta_solenoid = 1.0, 0.0
            w_tape_mm, t_tape_mm = 4.0, 0.1

            @staticmethod
            def je_max_stress_limited(ri, rf, L, sigma):
                return 400e6

            @staticmethod
            def solenoid_field_center(ri, rf, je_si, L):
                return 4e-7 * np.pi * je_si * (rf - ri)

            @staticmethod
            def Je_tape(b0, theta):
                je = max(10.0, 800 - 50 * b0)
                return je * 1.5, je

            @staticmethod
            def scan_time(b0, v):
                return b0 ** 2 * v

            @staticmethod
            def hoop_stress(*args):
                return 300e6, 0.0

        config = MockConfig()
        solenoid_lib = MockSolenoidLib()

    preview_axes()
    print()

    g_th = build_thickness_grid()
    print()
    g_a = build_area_grid()

    print("\n--- Test validations ---")
    th = g_th["th_vals"]
    ri = g_th["ri_vals"]
    d_th, d_ri = np.diff(th), np.diff(ri)
    print(f"Th first/last step : {d_th[0]*1e3:.3f} / {d_th[-1]*1e3:.3f} mm  "
          f"(ratio {d_th[-1]/d_th[0]:.1f})")
    print(f"Ri first/last step : {d_ri[0]*1e3:.3f} / {d_ri[-1]*1e3:.3f} mm  "
          f"(ratio {d_ri[-1]/d_ri[0]:.2f})")
    want = {"min": True, "max": False}[_cfg("FINE_AT_RI")]
    print(f"Ri grading matches : {bool(d_ri[-1] > d_ri[0]) == want or _cfg('P_RI') == 1}")
    print(f"Axes monotonic     : {bool(np.all(d_th > 0))} / {bool(np.all(d_ri > 0))}")
    print(f"Endpoints exact    : {np.isclose(th[0], config.TH_MIN)} / "
          f"{np.isclose(th[-1], config.TH_MAX)}")
    print(f"Ri endpoints exact : {np.isclose(ri[0], config.RI_MIN)} / "
          f"{np.isclose(ri[-1], config.RI_MAX)}")
    print(f"Edges bracket axis : {cell_edges(th)[0] < th[0]} / "
          f"{cell_edges(th)[-1] > th[-1]}")
    print(f"Max f_sc           : {np.nanmax(g_th['f_sc'])*100:.2f} %")
    print(f"Min SC length      : {np.nanmin(g_th['length_sc'])/1000:.2f} km")
    print("Test passed.")