# config.py
# ─────────────────────────────────────────────────────────────────────────────
# All tunable parameters and shared constants live here.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np

try:
    from solenoid_lib import t_tape_mm as _T_TAPE_MM
except ImportError:
    _T_TAPE_MM = 0.1

# ── Winding ──────────────────────────────────────────────────────────────────
N_PAR    = 10                          # tapes paralleled into one turn
T_BUNDLE = N_PAR * _T_TAPE_MM * 1e-3   # [m] radial build of ONE turn = 1.0 mm
# T_BUNDLE is the resolution floor of the Th axis: two thicknesses inside the
# same bundle give identical n_bund, i0, L, tau and f_cu_req.

# ── Coil ─────────────────────────────────────────────────────────────────────
L_COIL          = 5.000    # [m]  REQUIRED. solenoid_lib.solenoid_length is None,
                           #      so without this coil_length() raises.
QUANTIZE_LENGTH = True     # snap L to a whole number of tape widths

# ── Design point (mirrors solenoid_lib.DesignParams) ─────────────────────────
SIGMA_LIMIT_PA = 750e6     # [Pa]  hoop limit of the structure
U_TARGET       = 1.00      # [-]   sit at the limit; keep the margin in
                           #       SIGMA_LIMIT_PA, not in two places at once
FILL_TARGET    =1     # [-]   tape + co-wound Cu packing ceiling
F_CU           = 0.50      # [-]   copper fraction of the tape itself
GAMMA_CU       = 5.0e16    # [A2 s m-4] copper action limit at the hot spot
SF_QUENCH      = 1.00      # [-]   safety factor on required Cu area

TOPOLOGY  = "series"       # ONE resistor for the stack -> the dump sees l_tot.
                           # "isolated" uses l_iso (one pancake, ~1250x smaller),
                           # which collapses tau and hands you a field with
                           # essentially no protection in it. Set deliberately.
DUMP_MODE = "resistance"
U_EE      = 1000.0            

SOLVER_RTOL = 1e-9         # bisection bracket. 1e-12 is wasted: Je* lands on a
                           # ceil() jump, so the extra digits resolve nothing.

# ── Grid: Ri vs Th ───────────────────────────────────────────────────────────
N_RI = 150
N_TH = 150
RI_MIN, RI_MAX = 0.1, 8.0         # [m] 100 mm -> 1500 mm
TH_MIN         = 3 * T_BUNDLE          # [m] 3 bundles: below 1 bundle nothing
                                       #     fits, at 2 only n_bund=1 is legal
TH_MAX         = 0.5                # [m]

# ── Grid: Ri vs A (conductor cross-section) ──────────────────────────────────
N_RI_A = 150
N_A    = 150
A_MIN = np.pi * ((RI_MAX + TH_MIN)**2 - RI_MAX**2)  # smallest Th at largest Ri
A_MAX = np.pi * ((RI_MAX + TH_MAX)**2 - RI_MAX**2)  # largest Th at largest Ri
TH_MAX_LIMIT = TH_MAX      # [m] REQUIRED for the A-scan: constant-A curves run
                           # to Th ~ 1.2 m at small Ri. Expect a large masked
                           # fraction there — that is correct, not a failure.

# ── Grid grading (power law) ─────────────────────────────────────────────────
#   fine_at="min":  v_k = v_min + span * (k/(n-1))**p
#   fine_at="max":  v_k = v_max - span * (1 - k/(n-1))**p     p = 1 -> uniform
P_RI = 1.0     # uniform: B0 curves hardest at small Ri, the scan-time optimum
               # sits at RI_MAX, and uniform is the honest split between them
P_TH = 1.5     # was 2.0 — with ROUND_TH on, 2.0 merged ~20 points at the thin
               # end and stepped 10 bundles at a time at the thick end
P_A  = 2.0     # ignored while A_AXIS_FROM_TH is True

D0_RI = None            # [m]  fix the smallest step instead of the count
D0_TH = None            # [m]
D0_A  = None            # [m²]

ROUND_RI = None         # [m]  Ri is continuous, nothing to snap to
ROUND_TH = T_BUNDLE     # [m]  snap Th to whole bundles. This is the change
                        #      that actually redistributes the resolution.
ROUND_A  = None         # [m²]

A_AXIS_FROM_TH = True   # A axis = graded Th axis at RI_MAX, so it inherits
                        # ROUND_TH and the two scans quantise identically

FINE_AT_RI = "max"      # inert while P_RI == 1.0
FINE_AT_TH = "min"      # dense at the thin end, where B0 ~ sqrt(Th) curves
FINE_AT_A  = "min"

# ── Plotting ─────────────────────────────────────────────────────────────────
B0_ISO_LEVELS = [2, 4, 6, 8, 9, 10, 11, 12, 14]   # was up to 70 T; the solved
                                                  # range is roughly 6-12 T
B0_ISO_LW     = 1.8
B0_ISO_LS     = "--"
STRESS_ISO_LEVELS = [100, 200, 300, 500, 700, 800]
SAVE_DPI = 150

# ── DEAD: left from the secant solver, unused by the bisection ───────────────
MAX_ITER = 50
TOL_REL  = 1e-4
JE_INIT  = 500.0
STRESS_DERATING = 0.80