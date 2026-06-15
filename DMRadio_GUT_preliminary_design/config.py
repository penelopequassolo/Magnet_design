# config.py
# ─────────────────────────────────────────────────────────────────────────────
# All tunable parameters and shared constants live here.
# ─────────────────────────────────────────────────────────────────────────────

# ── Self-consistent solver ───────────────────────────────────────────────────
MAX_ITER = 50        # [—]      max self-consistent iterations per cell
TOL_REL  = 1e-4      # [—]      relative convergence tolerance on Je
JE_INIT  = 500.0     # [A/mm²]  initial guess for Je (all cells)

# ── Mechanical limits ────────────────────────────────────────────────────────
SIGMA_LIMIT_PA  = 750e6   # [Pa]  hoop stress threshold for Je derating
STRESS_DERATING = 0.80    # [—]   derating factor when stress exceeds limit

# ── Grid: Ri vs Th ───────────────────────────────────────────────────────────
N_RI = 60
N_TH = 60
RI_MIN, RI_MAX = 0.010, 1.000   # m   (10 mm  → 1000 mm)
TH_MIN, TH_MAX = 0.010, 0.200   # m   (10 mm  →  200 mm)

# ── Grid: Ri vs A (conductor cross-section) ──────────────────────────────────
N_RI_A = 60
N_A    = 60
A_MIN, A_MAX = 1e-4, 0.7        # m²  (~0 → 700000 mm²)
TH_MAX_LIMIT = None             # m   max coil thickness mask (None disables)

# ── Plotting ─────────────────────────────────────────────────────────────────
B0_ISO_LEVELS = [1, 5, 10, 15, 20, 25, 30, 40]
B0_ISO_LW     = 1.8
B0_ISO_LS     = "--"

STRESS_ISO_LEVELS = [100, 200, 300, 500, 700, 900, 1200, 1500]

SAVE_DPI = 150