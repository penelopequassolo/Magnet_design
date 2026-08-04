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
N_RI = 30
N_TH = 30
RI_MIN, RI_MAX = 0.010, 1.500   # m   (100 mm  → 1500 mm)
TH_MIN, TH_MAX = 0.002, 0.500   # m   (2 mm  →  400 mm)

# ── Grid: Ri vs A (conductor cross-section) ──────────────────────────────────
N_RI_A = 30
N_A    = 30
A_MIN = np.pi * ((RI_MAX + TH_MIN)**2 - RI_MAX**2)  # smallest Th at largest Ri
A_MAX = np.pi * ((RI_MAX + TH_MAX)**2 - RI_MAX**2)  # largest Th at largest Ri
TH_MAX_LIMIT = None             # m   max coil thickness mask (None disables)

# ── Plotting ─────────────────────────────────────────────────────────────────
B0_ISO_LEVELS = [1, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70]
B0_ISO_LW     = 1.8
B0_ISO_LS     = "--"

STRESS_ISO_LEVELS = [100, 200, 300, 500, 700, 800]

SAVE_DPI = 150

