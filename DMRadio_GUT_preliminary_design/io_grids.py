# grid.py
# ─────────────────────────────────────────────────────────────────────────────
# Build power-law graded meshgrids and run the quench-aware design solver on
# every cell.
#
# Each axis can be graded fine at its low end (fine_at="min", steps grow with
# the value) or fine at its high end (fine_at="max", steps shrink with the
# value). p = 1 is uniform and fine_at is then irrelevant.
#
# Physics per cell (solenoid_lib section 5): maximise B0 subject to the hoop
# limit, the packing ceiling and the tape capability, with the copper needed
# for quench protection competing for the same radial build.  Monotone in Je,
# so the cell solve is a bisection and always converges.
#
# Naming: every current density ending in _mm2 that describes the tape is per
# mm² of FULL tape cross-section (w_tape × t_tape), not per mm² of REBCO.
# f_tape_min is the minimum tape fraction of the pack; f_tape_built is what the
# integer turn count actually delivers.
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

PROGRESS_EVERY = 25          # cells between progress prints


# ═════════════════════════════════════════════════════════════════════════════
# 0.  Config access with fallbacks
# ═════════════════════════════════════════════════════════════════════════════
# Defaults used when config.py predates a given option. FINE_AT_* default to
# "min", which is the historical behaviour, so an old config still reproduces
# the old axes exactly. The design block mirrors solenoid_lib.DesignParams.
_DEFAULTS = {
    "P_RI": 1.0, "P_TH": 2.0, "P_A": 2.0,
    "D0_RI": None, "D0_TH": None, "D0_A": None,
    "ROUND_RI": None, "ROUND_TH": None, "ROUND_A": None,
    "A_AXIS_FROM_TH": True,
    "FINE_AT_RI": "min", "FINE_AT_TH": "min", "FINE_AT_A": "min",
    # ── design point ──
    "SIGMA_LIMIT_PA": 750e6,
    "U_TARGET": 0.995, "FILL_TARGET": 0.98,
    "F_CU": 0.50, "GAMMA_CU": 5.0e16, "SF_QUENCH": 1.0,
    "TOPOLOGY": "isolated", "DUMP_MODE": "resistance", "R_EE": 4.0,
    "QUANTIZE_LENGTH": True, "SOLVER_RTOL": 1e-12,
    "L_COIL": None,
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


def design_params():
    """The single DesignParams object shared by every cell of every scan."""
    return solenoid_lib.DesignParams(
        sigma_limit=_cfg("SIGMA_LIMIT_PA"),
        u_target=_cfg("U_TARGET"),
        fill_target=_cfg("FILL_TARGET"),
        fCu=_cfg("F_CU"),
        gamma_cu=_cfg("GAMMA_CU"),
        sf_quench=_cfg("SF_QUENCH"),
        topology=_cfg("TOPOLOGY"),
        dump_mode=_cfg("DUMP_MODE"),
        R_EE=_cfg("R_EE"),
    )


def coil_length():
    """Requested coil length [m]: config.L_COIL wins, else solenoid_lib."""
    l = _cfg("L_COIL")
    if l is None:
        l = getattr(solenoid_lib, "solenoid_length", None)
    if l is None:
        raise ValueError("coil length undefined: set config.L_COIL or "
                         "solenoid_lib.solenoid_length")
    return float(l)


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
# name -> (source key in the design dict | None, scale, dtype, unit, meaning).
# Anything added here is created, filled, summarised and SAVED automatically:
# io_grids is key-agnostic, so no other file needs editing.
RESULT_KEYS = {
    # ── field / figure of merit ──────────────────────────────────────────
    "b0":            ("b0",            1.0,    float, "T",       "central field"),
    "v_bore":        (None,            1.0,    float, "m3",      "bore volume"),
    "scan":          (None,            1.0,    float, "yr",      "axion scan time"),
    # ── current densities ────────────────────────────────────────────────
    "je_coil":       ("je",            1.0,    float, "A/mm2",   "Je of the whole pack"),
    "je_tape":       (None,            1.0,    float, "A/mm2",   "J in the tape as built"),
    "je_max":        ("je_tape",       1.0,    float, "A/mm2",   "tape capability at B0"),
    "j_crit":        ("jc_tape",       1.0,    float, "A/mm2",   "tape Jc at B0"),
    "je_mech":       ("je_seed",       1.0,    float, "A/mm2",   "stress-only Je, no copper"),
    "b0_mech":       ("b0_seed",       1.0,    float, "T",       "field of the stress-only seed"),
    "je_closed":     ("je_closed_form", 1.0,   float, "A/mm2",   "closed-form hoop+quench Je"),
    # ── mechanics ────────────────────────────────────────────────────────
    "stress":        ("sigma_pa",      1e-6,   float, "MPa",     "smeared hoop stress"),
    "sigma_struct":  ("sigma_struct",  1e-6,   float, "MPa",     "stress in the structure"),
    "util":          ("util",          1.0,    float, "-",       "sigma_struct / sigma_limit"),
    "f_struct":      ("f_struct",      1.0,    float, "-",       "load-bearing build fraction"),
    # ── winding ──────────────────────────────────────────────────────────
    "f_tape_min":    ("f_tape",        1.0,    float, "-",       "minimum tape fraction"),
    "f_tape_built":  ("f_built",       1.0,    float, "-",       "tape fraction as wound"),
    "fill":          ("fill",          1.0,    float, "-",       "tape + co-wound Cu"),
    "n_tp":          ("n_tp",          1.0,    float, "-",       "radial turns per pancake"),
    "n_tot":         ("n_tot",         1.0,    float, "-",       "total turns"),
    "pitch":         ("pitch",         1e3,    float, "mm",      "radial pitch"),
    "i0":            ("i0",            1.0,    float, "A",       "operating current per turn"),
    "i_max":         ("i_max",         1.0,    float, "A",       "tape current limit at B0"),
    "i_margin":      ("i_margin",      1.0,    float, "-",       "I_max / I0"),
    "length_tape":   ("len_tape",      1.0,    float, "m",       "tape length in the pack"),
    # ── circuit / quench ─────────────────────────────────────────────────
    "em_tot":        ("em_tot",        1e-6,   float, "MJ",      "stored energy"),
    "em_dump":       ("em_dump",       1e-6,   float, "MJ",      "energy seen by one dump"),
    "l_self":        ("l_tot",         1.0,    float, "H",       "self-inductance"),
    "l_dump":        ("l_dump",        1.0,    float, "H",       "inductance seen by the dump"),
    "r_ee":          ("r_ee",          1.0,    float, "ohm",     "dump resistance"),
    "u_ee":          ("u_ee",          1.0,    float, "V",       "dump voltage"),
    "tau_ee":        ("tau_ee",        1.0,    float, "s",       "dump time constant"),
    "miits":         ("ql_dump",       1e-6,   float, "MIIt",    "action dumped per turn"),
    "j_cu_max":      ("j_cu_max",      1e-6,   float, "A/mm2",   "allowed copper current density"),
    "j_cu":          ("j_cu",          1e-6,   float, "A/mm2",   "copper current density as built"),
    "gamma_op":      ("gamma_op",      1.0,    float, "A2s/m4",  "hot-spot action as built"),
    "f_cu_req":      ("f_cu_req",      1.0,    float, "-",       "copper fraction required"),
    "f_cu_have":     ("f_cu_have",     1.0,    float, "-",       "copper already in the tape"),
    "f_cu_add":      ("f_cu_add",      1.0,    float, "-",       "copper to co-wind"),
    "t_cu_add":      (None,            1.0,    float, "mm",      "co-wound Cu thickness per turn"),
    "m_cu":          ("m_cu",          1.0,    float, "kg",      "copper mass in the pack"),
    # ── flags ────────────────────────────────────────────────────────────
    "n_eval":        ("n_eval",        1.0,    float, "-",       "bisection evaluations"),
    "binding_code":  (None,            1.0,    np.int8, "-",     "0 mech 1 pack 2 tape 3 none"),
    "feasible":      (None,            1.0,    bool,  "-",       "a feasible Je exists"),
    "converged":     (None,            1.0,    bool,  "-",       "solver returned a design"),
}

_FILL = {float: np.nan, bool: False, np.int8: np.int8(-1)}


def _empty_result_grids(shape):
    """Create the standard set of output grids, filled with their null value."""
    out = {}
    for name, (_, _, dt, _, _) in RESULT_KEYS.items():
        out[name] = np.full(shape, _FILL[dt], dtype=dt)
    out["binding"] = np.full(shape, "", dtype="<U12")     # human-readable label
    return out


# ═════════════════════════════════════════════════════════════════════════════
# 3.  Per-cell solver
# ═════════════════════════════════════════════════════════════════════════════
def _solve_cell(ri, rf, l, v, par):
    """
    Quench-aware operating point of one (Ri, Th) cell.

    Returns a flat dict keyed exactly like RESULT_KEYS, or None if the cell
    admits no feasible Je (protection impossible, or the pack cannot be packed).
    """
    coef = solenoid_lib.pack_coefficients(ri, rf, l, quantize=False)
    d = solenoid_lib.solve_design(coef=coef, par=par, rtol=_cfg("SOLVER_RTOL"))

    if d is None:                       # infeasible: keep the seed for diagnosis
        je_seed = solenoid_lib.je_stress_only(coef, par) / 1e6
        return {"je_mech": je_seed, "b0_mech": coef["k_b"] * je_seed * 1e6,
                "v_bore": v, "feasible": False, "converged": False,
                "binding": "none", "binding_code": np.int8(3)}

    out = {}
    for name, (src, scale, dt, _, _) in RESULT_KEYS.items():
        if src is not None and src in d:
            out[name] = d[src] * scale

    # derived / non-scalar-mapped entries
    out["v_bore"]      = v
    out["je_tape"]     = d["je"] / d["f_built"]          # J in the tape as built
    out["t_cu_add"]    = d["f_cu_add"] * d["pitch"] * 1e3
    out["binding"]     = d["binding"]
    out["binding_code"] = np.int8(solenoid_lib.BINDING_CODES[d["binding"]])
    out["feasible"]    = True
    out["converged"]   = True
    out["scan"]        = (solenoid_lib.scan_time(d["b0"], v)
                          if hasattr(solenoid_lib, "scan_time")
                          else d["b0"] ** 2 * v)
    return out


# ═════════════════════════════════════════════════════════════════════════════
# 4.  Common scan core — used by both builders
# ═════════════════════════════════════════════════════════════════════════════
def _run_scan(ri, th, valid, label, l_built, par):
    """
    ri, th, valid : 2-D arrays of identical shape (valid is boolean).
    Returns the dict of result grids.
    """
    shape = ri.shape
    rf = ri + th
    v = np.pi * ri ** 2 * l_built

    res = _empty_result_grids(shape)
    total, done, n_err = ri.size, 0, 0

    for i in range(shape[0]):
        for j in range(shape[1]):
            done += 1
            if done % PROGRESS_EVERY == 0 or done == total:
                print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")

            if not valid[i, j]:
                continue

            try:
                cell = _solve_cell(ri[i, j], rf[i, j], l_built, v[i, j], par)
                for key, val in cell.items():
                    if key in res:
                        res[key][i, j] = val

            except (ValueError, RuntimeError, ZeroDivisionError) as err:
                n_err += 1
                if n_err <= 5:
                    print(f"\n[!] {label} physics error at "
                          f"Ri={ri[i, j]*1e3:.1f} mm, Th={th[i, j]*1e3:.2f} mm: {err}")

    print(f"\n2-D meshgrid ({label}) computed."
          + (f"  [{n_err} physics errors]" if n_err else ""))
    res["_rf"] = rf
    res["_v"] = v
    return res


# ═════════════════════════════════════════════════════════════════════════════
# 5.  Thickness-based scan : x = Ri, y = Th
# ═════════════════════════════════════════════════════════════════════════════
def build_thickness_grid():
    par = design_params()
    l_built, n_pc = _length_banner()

    ri_vals = make_ri_axis()
    th_vals = make_th_axis()

    print("Th-scan axes:")
    _axis_report("Ri", ri_vals, 1e3, "mm")
    _axis_report("Th", th_vals, 1e3, "mm")

    ri, th = np.meshgrid(ri_vals, th_vals)
    valid = np.ones(ri.shape, dtype=bool)
    if config.TH_MAX_LIMIT is not None:
        valid &= th <= config.TH_MAX_LIMIT

    res = _run_scan(ri, th, valid, "Th-scan", l_built, par)
    rf, v = res.pop("_rf"), res.pop("_v")

    grids = {"ri": ri, "th": th, "rf": rf, "v": v,
             "ri_vals": ri_vals, "th_vals": th_vals,
             "ri_edges": cell_edges(ri_vals), "th_edges": cell_edges(th_vals),
             "valid": valid, "l_built": l_built, "n_pc": n_pc,
             "params": par, **res}
    _print_summary(grids, ri.size, label="Th",
                   extra={"Th (mm)": th * 1e3})
    return grids


# ═════════════════════════════════════════════════════════════════════════════
# 6.  Area-based scan : x = Ri, y = A = pi(Ro² - Ri²)
# ═════════════════════════════════════════════════════════════════════════════
def build_area_grid():
    par = design_params()
    l_built, n_pc = _length_banner()

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

    res = _run_scan(ri, np.where(valid, th, config.TH_MIN), valid,
                    "A-scan", l_built, par)
    rf, v = res.pop("_rf"), res.pop("_v")

    grids = {"ri": ri, "a": a_total, "th": np.where(valid, th, np.nan),
             "rf": rf, "v": v,
             "ri_vals": ri_vals, "a_vals": a_vals,
             "ri_edges": cell_edges(ri_vals), "a_edges": cell_edges(a_vals),
             "valid": valid, "l_built": l_built, "n_pc": n_pc,
             "params": par, **res}
    _print_summary(grids, ri.size, label="A",
                   extra={"A_total (mm²)": a_total * 1e6,
                          "Th (mm)": np.where(valid, th, np.nan) * 1e3})
    return grids


def _length_banner():
    """Quantise the coil length once and say so — it changes v_bore."""
    l_req = coil_length()
    if _cfg("QUANTIZE_LENGTH"):
        l_built, n_pc = solenoid_lib.quantize_length(l_req)
    else:
        l_built = l_req
        n_pc = max(1, int(round(l_req / (solenoid_lib.w_tape_mm * 1e-3))))
    if abs(l_built - l_req) > 1e-12:
        print(f"  length quantised: {l_req*1e3:.3f} -> {l_built*1e3:.3f} mm "
              f"({n_pc} pancakes of {solenoid_lib.w_tape_mm:.2f} mm)")
    else:
        print(f"  length {l_built*1e3:.1f} mm = {n_pc} pancakes")
    return l_built, n_pc


# ═════════════════════════════════════════════════════════════════════════════
# 7.  Summary
# ═════════════════════════════════════════════════════════════════════════════
def _rng(arr, unit="", fmt=".2f"):
    if not np.any(np.isfinite(arr)):
        return "  —"
    return f"{np.nanmin(arr):{fmt}} – {np.nanmax(arr):{fmt}} {unit}".rstrip()


def _print_summary(grids, total, label, extra=None):
    b0 = grids["b0"]
    par = grids["params"]
    ok = np.isfinite(b0)
    n_valid = int(np.count_nonzero(grids["valid"]))
    n_ok = int(np.count_nonzero(ok))

    print(f"\n─ {label}-scan summary "
          f"(sigma<={par.sigma_limit/1e6:.0f} MPa @ u={par.u_target}, "
          f"fill<={par.fill_target}, R_EE={par.R_EE:g} ohm, "
          f"gamma_max={par.gamma_max:.2e}) ─")
    print(f"  cells in mask   : {n_valid} / {total}")
    print(f"  feasible        : {n_ok} / {max(n_valid,1)}"
          f"   ({100*n_ok/max(n_valid,1):.0f} %)")

    if n_ok:
        codes = grids["binding_code"]
        names = ["mechanical", "packing", "EM/tape"]
        share = "  ".join(f"{nm} {100*np.count_nonzero(codes==k)/n_ok:.0f}%"
                          for k, nm in enumerate(names)
                          if np.any(codes == k))
        print(f"  binding         : {share}")

    print(f"  Ri              : {_rng(grids['ri']*1e3, 'mm', '.1f')}")
    if extra:
        for name, arr in extra.items():
            if np.any(np.isfinite(arr)):
                print(f"  {name:<15} : {_rng(arr)}")
    if not n_ok:
        print("  [!] no feasible cell — relax gamma_cu, R_EE or fill_target")
        return

    print(f"  B0              : {_rng(b0, 'T', '.3f')}")
    print(f"  B0 if no quench : {_rng(grids['b0_mech'], 'T', '.3f')}"
          f"   (median cost {100*np.nanmedian(1 - b0/grids['b0_mech']):.1f} % of B0)")
    print(f"  Je pack         : {_rng(grids['je_coil'], 'A/mm2')}")
    print(f"  sigma smeared   : {_rng(grids['stress'], 'MPa', '.1f')}")
    print(f"  sigma structure : {_rng(grids['sigma_struct'], 'MPa', '.1f')}")
    print(f"  utilisation     : {_rng(grids['util'], '-', '.3f')}")
    print(f"  build budget    : tape {_rng(grids['f_tape_built']*100, '%')} | "
          f"Cu {_rng(grids['f_cu_req']*100, '%')} | "
          f"structure {_rng(grids['f_struct']*100, '%')}")
    print(f"  co-wound Cu     : {_rng(grids['f_cu_add']*100, '%')} "
          f"({np.count_nonzero(grids['f_cu_add'] > 1e-9)} cells need it)")
    print(f"  turns / pancake : {_rng(grids['n_tp'], '-', '.0f')}")
    print(f"  I per turn      : {_rng(grids['i0'], 'A', '.0f')}"
          f"   margin {_rng(grids['i_margin'], '-', '.2f')}")
    print(f"  tape length     : {_rng(grids['length_tape']/1e3, 'km')}")
    print(f"  E stored        : {_rng(grids['em_tot'], 'MJ')}")
    print(f"  tau_EE          : {_rng(grids['tau_ee'], 's', '.3f')}"
          f"   U_EE {_rng(grids['u_ee'], 'V', '.0f')}")
    print(f"  Cu mass         : {_rng(grids['m_cu'], 'kg', '.0f')}")
    print(f"  scan time       : {_rng(grids['scan'], 'yr', '.3f')}")
    print(f"  bisection evals : {np.nanmean(grids['n_eval']):.0f} avg / cell")

    # closed form must reproduce the bisection wherever mechanics binds
    m = np.isfinite(b0) & (grids["binding_code"] == 0)
    if np.any(m):
        err = np.nanmax(np.abs(grids["je_closed"][m] / grids["je_coil"][m] - 1.0))
        print(f"  closed-form check: max |Je_cf/Je - 1| = {err:.2e} on "
              f"{np.count_nonzero(m)} mechanically-bound cells "
              f"{'ok' if err < 1e-6 else 'MISMATCH'}")

    # the best cell, which is what you actually want to read off the scan
    k = np.unravel_index(np.nanargmax(np.where(ok, b0, -np.inf)), b0.shape)
    print(f"  best B0 cell    : Ri={grids['ri'][k]*1e3:.1f} mm, "
          f"Th={grids['th'][k]*1e3:.1f} mm -> B0={b0[k]:.3f} T, "
          f"Je={grids['je_coil'][k]:.1f} A/mm2, n_tp={grids['n_tp'][k]:.0f}, "
          f"f_Cu={grids['f_cu_req'][k]*100:.1f} %, limited by {grids['binding'][k]}")
    if np.any(np.isfinite(grids["scan"])):
        k = np.unravel_index(np.nanargmin(np.where(ok, grids["scan"], np.inf)),
                             b0.shape)
        print(f"  best scan cell  : Ri={grids['ri'][k]*1e3:.1f} mm, "
              f"Th={grids['th'][k]*1e3:.1f} mm -> {grids['scan'][k]:.3f} yr "
              f"at B0={b0[k]:.3f} T")


# ═════════════════════════════════════════════════════════════════════════════
# TEST HARNESS
# ═════════════════════════════════════════════════════════════════════════════
if __name__ == "__main__":
    print("Testing graded grid logic...\n")

    if solenoid_lib is None or config is None:
        print("[!] Local dependencies not found — axis tests only "
              "(the design layer lives in solenoid_lib and is not mocked).\n")

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
            SIGMA_LIMIT_PA = 750e6
            L_COIL = 4.0

        config = MockConfig()
        preview_axes()
        th, ri = make_th_axis(), make_ri_axis()
        g_th = None
    else:
        preview_axes()
        print()
        g_th = build_thickness_grid()
        print()
        g_a = build_area_grid()
        th, ri = g_th["th_vals"], g_th["ri_vals"]

    print("\n--- Test validations ---")
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
    if g_th is not None:
        print(f"Max f_tape built   : {np.nanmax(g_th['f_tape_built'])*100:.2f} %")
        print(f"Max packing        : {np.nanmax(g_th['fill'])*100:.2f} % "
              f"(target {g_th['params'].fill_target*100:.0f} %)")
        print(f"Max utilisation    : {np.nanmax(g_th['util']):.4f} "
              f"(target {g_th['params'].u_target})")
        print(f"Hot spot respected : "
              f"{bool(np.nanmax(g_th['gamma_op']) <= g_th['params'].gamma_max*(1+1e-9))}")
        print(f"Min tape length    : {np.nanmin(g_th['length_tape'])/1000:.2f} km")
    print("Test passed.")