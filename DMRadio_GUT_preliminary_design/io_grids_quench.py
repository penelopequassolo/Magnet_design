# io_grids.py
# ─────────────────────────────────────────────────────────────────────────────
# Save / load result grids to disk as flat CSV (long format) and/or per-field
# 2-D CSV matrices.
#
# Naming follows grid.py: "tape" means full tape cross-section (w_tape ×
# t_tape), so f_tape_min is the minimum tape fraction of the winding pack and
# length_tape is physical tape length, not REBCO-layer length.
#
# The field list is taken from grid.RESULT_KEYS when grid.py is importable, so
# any new result added there is saved automatically. The explicit list below is
# the fallback and also fixes the column ORDER of the geometry block.
#
# Non-float grids are handled explicitly: 'binding' is a text label,
# 'binding_code' is int8, and converged/feasible/valid are boolean. They are
# written as text / int / 0-1 and restored to their original dtype on load.
#
# CSV only: the design point (sigma_limit, gamma_cu, R_EE, ...) is NOT written
# to disk. It lives in config.py / the params object that produced the grid.
# ─────────────────────────────────────────────────────────────────────────────
import os

import numpy as np
import pandas as pd

try:
    import solenoid_lib
    W_TAPE_M = solenoid_lib.w_tape_mm * 1e-3
except (ImportError, AttributeError):
    # Fallback to 4 mm tape if library isn't found
    solenoid_lib = None
    W_TAPE_M = 0.004


# ── geometry / axis-derived fields, written first and in this order ──────────
_GEOM_FIELDS = [
    "ri", "rf", "th", "a", "a_tape", "v", 
]

# ── results, used when grid.py cannot be imported ────────────────────────────
_RESULT_FIELDS_FALLBACK = [
    # field / figure of merit
    "b0", "scan",
    # current densities
    "je", "je_coil", "je_tape", "j_crit",

    # mechanics
    "stress", "stress_hoop", "sigma_struct", "f_struct",
    # winding
     "f_tape_built", "n_tp", "n_tot", "pitch",
    "i0", "length_tape", "tape_pancake_length",
    # circuit / quench
    "em_tot", "l_self", "l_dump", "r_ee", "u_ee", "tau_ee",
    "miits", "j_cu_max", "j_cu", "gamma_op",
    "f_cu_req", "f_cu_have", "f_cu_add",
    # flags
    "n_eval", "binding", 
]


def _result_fields():
    """grid.RESULT_KEYS order when available, else the frozen fallback list."""
    try:
        import grid as _grid
        keys = list(_grid.RESULT_KEYS.keys()) + ["binding"]
    except Exception:
        return list(_RESULT_FIELDS_FALLBACK)
    # keep the legacy aliases so old grids still round-trip
    extra = [f for f in _RESULT_FIELDS_FALLBACK if f not in keys]
    return keys + extra


def _grid_fields():
    seen, out = set(), []
    for f in _GEOM_FIELDS + _result_fields():
        if f not in seen:
            seen.add(f)
            out.append(f)
    return out


# Kept as a module-level name because other code imports it.
_GRID_FIELDS = _grid_fields()

# Columns written by this module that are derived from _GRID_FIELDS and must
# not be re-inflated into grids on load.
_DERIVED_COLS = ("row_i", "col_j", "solenoid_length_m",
                 "length_tape_km", "pancake_length_m")

# Grids that are not plain floats. Everything else round-trips as float64.
_BOOL_FIELDS = ("converged", "feasible", "valid")
_INT8_FIELDS = ("binding_code",)
_TEXT_FIELDS = ("binding",)

# Old -> new names, applied on load so pre-rename CSVs still open.
_LEGACY_RENAMES = {
    "f_sc": "f_tape_min",
    "length_sc": "length_tape",
    "je_sc": "je_tape",
    "a_sc": "a_tape",
    "rebco_pancake_length": "tape_pancake_length",
}


# ═════════════════════════════════════════════════════════════════════════════
# Long (one row per cell) CSV
# ═════════════════════════════════════════════════════════════════════════════
def save_grids_long(grids, path, solenoid_length=None):
    """
    Save grids by directly dumping the exact variables from the dictionary,
    while appending the specific length metrics requested.

    solenoid_length defaults to grids["l_built"] (the quantised coil length
    actually used by the solver) when it is not given.
    """
    shape = grids["ri"].shape
    n = grids["ri"].size

    if solenoid_length is None:
        solenoid_length = grids.get("l_built")
        if solenoid_length is None and solenoid_lib is not None:
            solenoid_length = getattr(solenoid_lib, "solenoid_length", None)
    l_val = float(solenoid_length) if solenoid_length is not None else np.nan

    ii, jj = np.indices(shape)
    data = {
        "row_i": ii.ravel(),
        "col_j": jj.ravel(),
        "solenoid_length_m": np.full(n, l_val),
    }

    # Pass through the exact raw variables
    skipped = []
    for f in _GRID_FIELDS:
        if f not in grids:
            continue
        arr = np.asarray(grids[f])
        if arr.shape != shape:            # scalars, axes, params: not per-cell
            skipped.append(f)
            continue
        if f in _BOOL_FIELDS:
            data[f] = arr.astype(np.int8).ravel()
        elif f in _INT8_FIELDS:
            data[f] = arr.astype(np.int16).ravel()      # -1 = no design
        elif f in _TEXT_FIELDS:
            data[f] = arr.astype(str).ravel()
        else:
            data[f] = arr.astype(float).ravel()

    # --- Add the requested length metrics ---
    if "length_tape" in grids:
        len_tape_m = np.asarray(grids["length_tape"], dtype=float).ravel()

        # Total tape length in kilometers
        data["length_tape_km"] = len_tape_m / 1000.0

        # Single pancake length in meters
        num_pancakes = grids.get("n_pc")
        if num_pancakes is None and np.isfinite(l_val):
            num_pancakes = l_val / W_TAPE_M
        if num_pancakes:
            data["pancake_length_m"] = len_tape_m / float(num_pancakes)
        else:
            data["pancake_length_m"] = np.full(n, np.nan)

    # Save to disk
    df = pd.DataFrame(data)
    df.to_csv(path, index=False)
    n_ok = int(np.count_nonzero(np.isfinite(np.asarray(
        grids.get("b0", np.full(shape, np.nan)), dtype=float))))
    print(f"Saved raw grids    -> {path}  ({n} cells, {n_ok} feasible, "
          f"{len(df.columns)} columns)")
    if skipped:
        print(f"  not per-cell, not saved: {', '.join(skipped)}")
    return df


def load_grids_long(path):
    """
    Re-load the CSV back into a dict of 2-D numpy arrays. Legacy column names
    (f_sc, length_sc, je_sc, ...) are mapped to the current ones, and the
    boolean / int8 / text grids are restored to their original dtype.
    """
    df = pd.read_csv(path)
    n_rows = int(df["row_i"].max()) + 1
    n_cols = int(df["col_j"].max()) + 1
    shape = (n_rows, n_cols)

    rows = df["row_i"].to_numpy()
    cols = df["col_j"].to_numpy()

    grids = {}
    renamed = []
    for col in df.columns:
        if col in _DERIVED_COLS:
            continue

        name = _LEGACY_RENAMES.get(col, col)
        if name != col:
            renamed.append(f"{col}->{name}")

        if name in _TEXT_FIELDS:
            arr = np.full(shape, "", dtype="<U12")
            arr[rows, cols] = df[col].fillna("").astype(str).to_numpy()
        elif name in _BOOL_FIELDS:
            arr = np.zeros(shape, dtype=bool)
            arr[rows, cols] = df[col].fillna(0).to_numpy().astype(float) > 0.5
        elif name in _INT8_FIELDS:
            arr = np.full(shape, -1, dtype=np.int8)
            arr[rows, cols] = df[col].fillna(-1).to_numpy().astype(np.int8)
        else:
            arr = np.full(shape, np.nan)
            arr[rows, cols] = pd.to_numeric(df[col], errors="coerce").to_numpy()

        grids[name] = arr

    # l_built is a constant column, not a map: put it back as a scalar.
    if "solenoid_length_m" in df.columns:
        l_val = float(pd.to_numeric(df["solenoid_length_m"],
                                    errors="coerce").iloc[0])
        if np.isfinite(l_val):
            grids["l_built"] = l_val

    if renamed:
        print(f"[!] {path}: legacy field names remapped ({', '.join(renamed)})")
    print(f"Loaded raw grids from {path}  shape={shape}, fields={list(grids)}")
    return grids


# ═════════════════════════════════════════════════════════════════════════════
# Per-field 2-D matrices
# ═════════════════════════════════════════════════════════════════════════════
def save_grids_matrices(grids, out_dir, prefix=""):
    """
    Save each 2-D field as its own CSV matrix (rows x cols).
    """
    os.makedirs(out_dir, exist_ok=True)
    shape = grids["ri"].shape
    saved = []

    for f in _GRID_FIELDS:
        if f not in grids:
            continue
        arr = np.asarray(grids[f])
        if arr.shape != shape:
            continue
        fname = os.path.join(out_dir, f"{prefix}{f}.csv")
        if f in _TEXT_FIELDS or arr.dtype.kind in "US":
            np.savetxt(fname, arr.astype(str), delimiter=",", fmt="%s")
        else:
            # booleans -> int for clean CSV; int8 codes keep their sign
            out = arr.astype(int) if arr.dtype == bool else arr
            np.savetxt(fname, out, delimiter=",", fmt="%.8g")
        saved.append(fname)

    print(f"Saved {len(saved)} matrix CSVs -> {out_dir}/")
    return saved