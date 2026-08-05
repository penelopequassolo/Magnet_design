# config.py
# ─────────────────────────────────────────────────────────────────────────────
# All tunable parameters and shared constants live here.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np

# ── Self-consistent solver ───────────────────────────────────────────────────
MAX_ITER = 50        # [—]      max self-consistent iterations per cell
TOL_REL  = 1e-4      # [—]      relative convergence tolerance on Je
JE_INIT  = 500.0     # [A/mm²]  initial guess for Je (all cells)

# ── Mechanical limits ────────────────────────────────────────────────────────
SIGMA_LIMIT_PA  = 750e6   # [Pa]  hoop stress threshold for Je derating
STRESS_DERATING = 0.80    # [—]   derating factor when stress exceeds limit

# ── Grid: Ri vs Th ───────────────────────────────────────────────────────────
N_RI = 150
N_TH = 150
RI_MIN, RI_MAX = 0.010, 1.500   # m   (100 mm  → 1500 mm)
TH_MIN, TH_MAX = 0.002, 0.400   # m   (2 mm  →  400 mm)

# ── Grid: Ri vs A (conductor cross-section) ──────────────────────────────────
N_RI_A = 150
N_A    = 150
A_MIN = np.pi * ((RI_MAX + TH_MIN)**2 - RI_MAX**2)  # smallest Th at largest Ri
A_MAX = np.pi * ((RI_MAX + TH_MAX)**2 - RI_MAX**2)  # largest Th at largest Ri
TH_MAX_LIMIT = None             # m   max coil thickness mask (None disables)

# ── Plotting ─────────────────────────────────────────────────────────────────
B0_ISO_LEVELS = [1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70]
B0_ISO_LW     = 1.8
B0_ISO_LS     = "--"

STRESS_ISO_LEVELS = [100, 200, 300, 500, 700, 800]

SAVE_DPI = 150

# ── Grid grading (power law, fine at the low end) ────────────────────────────
#   v_k = v_min + (v_max - v_min) * (k / (n - 1))**p        p = 1  ->  uniform
P_RI = 1.0     # [—] radius axis (range is only a factor 7.5 -> uniform is fine)
P_TH = 2.0     # [—] thickness axis, quadratic grading
P_A  = 2.0     # [—] area axis (ignored if A_AXIS_FROM_TH is True)

# Fix the FIRST step instead of the point count. If not None, overrides N_*.
D0_RI = None            # [m]
D0_TH = None            # [m]   e.g. 5e-4 -> first step 0.5 mm
D0_A  = None            # [m²]

# Snap to a manufacturable resolution after grading; duplicates are removed.
ROUND_RI = None         # [m]
ROUND_TH = None         # [m]   e.g. 1e-4 -> 0.1 mm, one tape thickness
ROUND_A  = None         # [m²]

# Build the A axis from a graded Th axis taken at RI_MAX.
A_AXIS_FROM_TH = True

# Which end of each axis gets the fine spacing ("min" or "max").
# Irrelevant when the corresponding P_* is 1.0 (uniform).
FINE_AT_RI = "max"      # dense at the OUTER radius, steps shrink with Ri
FINE_AT_TH = "min"      # dense at the thin end, unchanged
FINE_AT_A  = "min"      # only used when A_AXIS_FROM_TH is False