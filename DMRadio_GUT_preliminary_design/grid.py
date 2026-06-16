# grid.py
# ─────────────────────────────────────────────────────────────────────────────
# Build meshgrids and run the self-consistent EM + mechanical solver.
# Returns a dict of result grids that downstream plotting modules consume.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np

import config
import solenoid_lib


def _empty_result_grids(shape):
    """Create the standard set of NaN-initialised output grids."""
    nan = lambda: np.full(shape, np.nan)
    return {
        "b0":        nan(),
        "scan":      nan(),
        "stress":    nan(),
        "j_crit":    nan(),
        "je_max":    nan(),
        "je_sc":     nan(),
        "margin":    nan(),
        "converged": np.zeros(shape, dtype=bool),
    }


def _solve_cell(ri, rf, v, je_mech_mm2=None):
    """
    Self-consistent fixed point for one geometry.

    Returns a dict of scalar results, or None if the physics fails.
    Physical fixed point:
        Je -> B0 = solenoid_field_center(Je)
           -> Jc(B0) via scaling law (Je_tape)
           -> Je_new = min(Jc-derated, mechanical limit)
    """
    if je_mech_mm2 is not None:
        je_mm2 = min(config.JE_INIT, je_mech_mm2)
    else:
        je_mm2 = config.JE_INIT

    converged = False
    for _ in range(config.MAX_ITER):
        je_si = je_mm2 * 1e6
        b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si, solenoid_lib.solenoid_length)
        _, je_em_mm2 = solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid)

        je_new_mm2 = je_em_mm2 if je_mech_mm2 is None else min(je_em_mm2, je_mech_mm2)

        rel_change = abs(je_new_mm2 - je_mm2) / (abs(je_mm2) + 1e-12)
        je_mm2 = je_new_mm2
        if rel_change < config.TOL_REL:
            converged = True
            break

    # ── Final quantities at converged Je ─────────────────────────────────────
    je_si = je_mm2 * 1e6
    b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si, solenoid_lib.solenoid_length)
    sigma_pa, _ = solenoid_lib.hoop_stress(ri, rf, je_si, solenoid_lib.solenoid_length)
    j_crit_mm2, je_em_mm2 = solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid)
    load_ratio = j_crit_mm2 / je_mm2

    return {
        "b0":        b0,
        "scan":      (b0**2 * v**(5 / 3))**2,
        "stress":    sigma_pa / 1e6,
        "j_crit":    j_crit_mm2,
        "je_max":    je_em_mm2,
        "je_sc":     je_mm2,
        "margin":    load_ratio,
        "converged": converged,
    }


# ─────────────────────────────────────────────────────────────────────────────
# Thickness-based scan : x = Ri, y = Th
# ─────────────────────────────────────────────────────────────────────────────
def build_thickness_grid():
    ri_vals = np.linspace(config.RI_MIN, config.RI_MAX, config.N_RI)
    th_vals = np.linspace(config.TH_MIN, config.TH_MAX, config.N_TH)

    ri, th = np.meshgrid(ri_vals, th_vals)
    rf = ri + th
    v  = np.pi * ri**2 * solenoid_lib.solenoid_length

    res = _empty_result_grids((config.N_TH, config.N_RI))
    total = config.N_RI * config.N_TH
    done = 0

    for i in range(config.N_TH):
        for j in range(config.N_RI):
            try:
                je_mech_si = solenoid_lib.je_max_stress_limited(
                    ri[i, j], rf[i, j], solenoid_lib.solenoid_length, config.SIGMA_LIMIT_PA
                )
                cell = _solve_cell(ri[i, j], rf[i, j], v[i, j],
                                   je_mech_mm2=je_mech_si / 1e6)
                for key, val in cell.items():
                    res[key][i, j] = val
            except (ValueError, RuntimeError):
                pass
            done += 1
        print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")

    print("\n2-D meshgrid (Th-scan) computed.")
    grids = {"ri": ri, "th": th, "rf": rf, "v": v, **res}
    _print_summary(grids, total, label="Th",
                   extra={"Th": th * 1e3, "V_bore": v * 1e6})
    return grids


# ─────────────────────────────────────────────────────────────────────────────
# Area-based scan : x = Ri, y = A = pi(Ro^2 - Ri^2)
# ─────────────────────────────────────────────────────────────────────────────
def build_area_grid():
    ri_vals = np.linspace(config.RI_MIN, config.RI_MAX, config.N_RI_A)
    a_vals  = np.linspace(config.A_MIN, config.A_MAX, config.N_A)   # total conductor area

    ri, a_total = np.meshgrid(ri_vals, a_vals)

    # Scan axis is the TOTAL conductor cross-section. The SC-layer area is the
    # non-copper fraction of the tape.
    a_sc = a_total / solenoid_lib.Cu_SC_ratio        # = a_total * (1 - Cu)

    # A_total = pi((Ri+Th)^2 - Ri^2)  ->  Th = sqrt(Ri^2 + A_total/pi) - Ri
    th = np.sqrt(ri**2 + a_total / np.pi) - ri
    rf = ri + th
    v  = np.pi * ri**2 * solenoid_lib.solenoid_length
    
    rebco_total_length = a_sc * solenoid_lib.solenoid_length/(solenoid_lib.t_tape_mm*1e-3*solenoid_lib.w_tape_mm*1e-3)

    res = _empty_result_grids((config.N_A, config.N_RI_A))
    total = config.N_RI_A * config.N_A
    done = 0


    for i in range(config.N_A):
        for j in range(config.N_RI_A):
            th_ij = th[i, j]
            if not np.isfinite(th_ij) or th_ij <= 0.0:
                done += 1
                continue
            if config.TH_MAX_LIMIT is not None and th_ij > config.TH_MAX_LIMIT:
                done += 1
                continue
            try:
                je_mech_si = solenoid_lib.je_max_stress_limited(
                    ri[i, j], rf[i, j], solenoid_lib.solenoid_length, config.SIGMA_LIMIT_PA
                )
                cell = _solve_cell(ri[i, j], rf[i, j], v[i, j],
                                   je_mech_mm2=je_mech_si / 1e6)
                for key, val in cell.items():
                    res[key][i, j] = val
            except (ValueError, RuntimeError):
                pass
            done += 1
        print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")




    print("\n2-D meshgrid (A-scan) computed.")
    grids = {"ri": ri, "a": a_total, "a_sc": a_sc, "rebco_total_length": rebco_total_length,
             "th": th, "rf": rf, "v": v, **res}
    _print_summary(grids, total, label="A",
                   extra={"A_total (mm²)": a_total * 1e6,
                          "A_SC (mm²)": a_sc * 1e6,
                          "Th (derived)": th * 1e3})
    return grids



# ─────────────────────────────────────────────────────────────────────────────
def _print_summary(grids, total, label, extra=None):
    b0 = grids["b0"]
    n_valid = int(np.sum(np.isfinite(b0)))
    n_conv  = int(np.sum(grids["converged"]))
    print(f"  Valid cells     : {n_valid} / {total}")
    print(f"  Converged cells : {n_conv} / {n_valid}")
    print(f"  Ri              : {np.nanmin(grids['ri'])*1e3:.1f} – "
          f"{np.nanmax(grids['ri'])*1e3:.1f} mm")
    if extra:
        for name, arr in extra.items():
            print(f"  {name:<15} : {np.nanmin(arr):.1f} – {np.nanmax(arr):.1f}")
    if n_valid:
        print(f"  B0              : {np.nanmin(b0):.3f} – {np.nanmax(b0):.3f} T")
        print(f"  Stress          : {np.nanmin(grids['stress']):.1f} – "
              f"{np.nanmax(grids['stress']):.1f} MPa")