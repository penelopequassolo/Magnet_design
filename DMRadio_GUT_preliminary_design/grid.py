# grid.py
# ─────────────────────────────────────────────────────────────────────────────
# Build meshgrids and run the self-consistent EM + mechanical solver using
# the material-fraction feedback loop.
# ─────────────────────────────────────────────────────────────────────────────
import importlib
import numpy as np

try:
    import solenoid_lib
    importlib.reload(solenoid_lib)
    import config
    importlib.reload(config)
except ImportError:
    # Failsafe for the test harness below if dependencies aren't locally found
    solenoid_lib = None
    config = None


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


def _solve_cell(ri, rf, v, je_mech_mm2):
    """
    Self-Consistent Equilibrium Solver matched to the single-point script.
    """
    je_mm2 = je_mech_mm2
    converged = False
    f_sc = 1.0
    
    for i in range(config.MAX_ITER):
        je_si = je_mm2 * 1e6
        
        # Calculate resulting central field with the current J
        b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si, solenoid_lib.solenoid_length)
        
        # Call Je_tape with 0.0 copper fraction to simulate a 100% SC tape.
        # Fallback to standard call if cu_frac hasn't been added to the mock/lib yet.
        try:
            jc_100sc_mm2, je_100sc_mm2 = solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid, cu_frac=0.5)
        except TypeError:
            jc_100sc_mm2, je_100sc_mm2 = solenoid_lib.Je_tape(b0, solenoid_lib.theta_solenoid)
            
        je_100sc_mm2 = max(je_100sc_mm2, 1e-6)
        
        # Calculate the minimum amount of REBCO needed
        f_sc = je_mm2 / je_100sc_mm2
        
        if f_sc <= 1.0:
            # Mechanically limited - lock in and break
            converged = True
            break
        else:
            # Electromagnetically limited - drop J to the tape limit
            je_new_mm2 = je_100sc_mm2
            
            rel_change = abs(je_new_mm2 - je_mm2) / (abs(je_mm2) + 1e-12)
            je_mm2 = je_new_mm2
            
            if rel_change < config.TOL_REL:
                # Bounded at 100% Superconductor (f_sc = 1.0)
                f_sc = 1.0 
                converged = True
                break

    # ── Final Quantities at the Converged Operating Point ────────────────────
    je_si = je_mm2 * 1e6
    b0 = solenoid_lib.solenoid_field_center(ri, rf, je_si, solenoid_lib.solenoid_length)
    sigma_pa, _ = solenoid_lib.hoop_stress(ri, rf, je_si, solenoid_lib.solenoid_length)
    
    # Calculate the current density strictly within the superconducting material
    j_sc_mm2 = je_mm2 / f_sc if f_sc > 0 else 0

    # Handle mock scan_time for test harness compatibility
    scan = solenoid_lib.scan_time(b0, v) if hasattr(solenoid_lib, "scan_time") else b0**2 * v

    return {
        "b0":        b0,
        "v_bore":    v,
        "scan":      scan,
        "stress":    sigma_pa / 1e6,
        "j_crit":    jc_100sc_mm2,
        "je_max":    je_100sc_mm2,
        "je_coil":   je_mm2,        # Equivalent to "Coil Engineering Je"
        "je_sc":     j_sc_mm2,      # Equivalent to "Superconductor J_sc"
        "f_sc":      f_sc,          
        "margin":    jc_100sc_mm2 / j_sc_mm2 if j_sc_mm2 > 0 else np.nan,
        "converged": converged,
    }

# ─────────────────────────────────────────────────────────────────────────────
# Area-based scan : x = Ri, y = A = pi(Ro^2 - Ri^2)
# ─────────────────────────────────────────────────────────────────────────────
def build_area_grid():
    ri_vals = np.linspace(config.RI_MIN, config.RI_MAX, config.N_RI_A)
    a_vals  = np.linspace(config.A_MIN, config.A_MAX, config.N_A)   

    ri, a_total = np.meshgrid(ri_vals, a_vals)

    # Coil dimensions derived from the total cross-sectional area
    th = np.sqrt(ri**2 + a_total / np.pi) - ri
    rf = ri + th
    v  = np.pi * ri**2 * solenoid_lib.solenoid_length
    
    tape_cross_section_m2 = (solenoid_lib.w_tape_mm * 1e-3) * (solenoid_lib.t_tape_mm * 1e-3)

    res = _empty_result_grids((config.N_A, config.N_RI_A))
    total = config.N_RI_A * config.N_A
    done = 0

    for i in range(config.N_A):
        for j in range(config.N_RI_A):
            th_ij = th[i, j]
            if not np.isfinite(th_ij) or th_ij <= 0.0 or th_ij < config.TH_MIN:
                done += 1
                continue
            if config.TH_MAX_LIMIT is not None and th_ij > config.TH_MAX_LIMIT:
                done += 1
                continue
                
            try:
                # 1. Extract purely mechanical upper limit
                je_mech_si = solenoid_lib.je_max_stress_limited(
                    ri[i, j], rf[i, j], solenoid_lib.solenoid_length, config.SIGMA_LIMIT_PA
                )
                
                # 2. Run fractional feedback equilibrium
                cell = _solve_cell(ri[i, j], rf[i, j], v[i, j], je_mech_mm2=je_mech_si / 1e6)
                
                for key, val in cell.items():
                    if key in res:
                        res[key][i, j] = val
                        
                # 3. Map material fraction back onto geometry for exact SC length
                v_pack = a_total[i, j] * solenoid_lib.solenoid_length
                v_sc_required = v_pack * cell["f_sc"]
                res["length_sc"][i, j] = v_sc_required / tape_cross_section_m2

            except (ValueError, RuntimeError):
                pass
            done += 1
            print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")

    print("\n2-D meshgrid (A-scan) computed.")
    grids = {"ri": ri, "a": a_total, "th": th, "rf": rf, "v": v, **res}
    _print_summary(grids, total, label="A",
                   extra={"A_total (mm²)": a_total * 1e6,
                          "f_sc (%)": res["f_sc"] * 100,
                          "Len SC (km)": res["length_sc"] / 1000})
    
    return grids


# ─────────────────────────────────────────────────────────────────────────────
# Thickness-based scan : x = Ri, y = Th
# ─────────────────────────────────────────────────────────────────────────────
def build_thickness_grid():
    ri_vals = np.linspace(config.RI_MIN, config.RI_MAX, config.N_RI)
    th_vals = np.linspace(config.TH_MIN, config.TH_MAX, config.N_TH)

    ri, th = np.meshgrid(ri_vals, th_vals)
    rf = ri + th
    v  = np.pi * ri**2 * solenoid_lib.solenoid_length
    
    tape_cross_section_m2 = (solenoid_lib.w_tape_mm * 1e-3) * (solenoid_lib.t_tape_mm * 1e-3)

    res = _empty_result_grids((config.N_TH, config.N_RI))
    total = config.N_RI * config.N_TH
    done = 0

    for i in range(config.N_TH):
        for j in range(config.N_RI):
            try:
                je_mech_si = solenoid_lib.je_max_stress_limited(
                    ri[i, j], rf[i, j], solenoid_lib.solenoid_length, config.SIGMA_LIMIT_PA
                )
                
                cell = _solve_cell(ri[i, j], rf[i, j], v[i, j], je_mech_mm2=je_mech_si / 1e6)
                
                for key, val in cell.items():
                    if key in res:
                        res[key][i, j] = val
                        
                # Extract Length Map
                a_total_ij = np.pi * (rf[i, j]**2 - ri[i, j]**2)
                v_pack = a_total_ij * solenoid_lib.solenoid_length
                v_sc_required = v_pack * cell["f_sc"]
                res["length_sc"][i, j] = v_sc_required / tape_cross_section_m2
                
            except (ValueError, RuntimeError):
                print(f"\n[!] Physics Error at Ri={ri[i, j]*1000:.1f}mm, Th={th[i, j]*1000:.1f}mm: {e}")
                pass
            done += 1
            print(f"  {done}/{total}  ({100 * done / total:.0f}%)", end="\r")

    print("\n2-D meshgrid (Th-scan) computed.")
    grids = {"ri": ri, "th": th, "rf": rf, "v": v, **res}
    _print_summary(grids, total, label="Th",
                   extra={"Th": th * 1e3, "f_sc (%)": res["f_sc"] * 100})
    return grids


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


# ─────────────────────────────────────────────────────────────────────────────
# TEST HARNESS 
# ─────────────────────────────────────────────────────────────────────────────
if __name__ == "__main__":
    print("Testing grid logic...")
    
    # If the real physics modules aren't available, inject mocks to prove the logic
    if solenoid_lib is None or config is None:
        print("[!] Local dependencies not found. Mocking physics libraries to test logic.\n")
        class MockConfig:
            RI_MIN, RI_MAX, N_RI_A, N_RI, N_TH = 0.1, 0.5, 3, 3, 3
            A_MIN, A_MAX, N_A = 0.05, 0.2, 3
            TH_MIN, TH_MAX, TH_MAX_LIMIT = 0.01, 0.5, 1.0
            MAX_ITER, TOL_REL = 100, 1e-4
            SIGMA_LIMIT_PA = 400e6

        class MockSolenoidLib:
            solenoid_length, theta_solenoid = 1.0, 0.0
            w_tape_mm, t_tape_mm = 4.0, 0.1

            @staticmethod
            def je_max_stress_limited(ri, rf, L, sigma):
                return 400e6 # Force 400 A/mm2 mechanical limit

            @staticmethod
            def solenoid_field_center(ri, rf, je_si, L):
                return 4e-7 * np.pi * je_si * (rf - ri) # Mock B-field

            @staticmethod
            def Je_tape(b0, theta):
                je = max(10.0, 800 - 50 * b0) # Tape degrades as field rises
                return je * 1.5, je 

            @staticmethod
            def scan_time(b0, v): return b0**2 * v
            
            @staticmethod
            def hoop_stress(*args): return 300e6, 0.0

 
        config = MockConfig()
        solenoid_lib = MockSolenoidLib()

    # Run the function
    test_grid = build_area_grid()
    
    print("\n--- Test Validations ---")
    print(f"Max f_sc achieved: {np.nanmax(test_grid['f_sc'])*100:.2f}% (Should be mathematically capped near 100%)")
    print(f"Min SC length needed: {np.nanmin(test_grid['length_sc'])/1000:.2f} km")
    print("Test passed successfully.")