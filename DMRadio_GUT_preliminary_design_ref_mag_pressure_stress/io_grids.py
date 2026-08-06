# io_grids.py
# ─────────────────────────────────────────────────────────────────────────────
# Save / load result grids to disk as flat CSV (long format) and/or per-field
# 2-D CSV matrices.
#
# Naming follows grid.py: "tape" means full tape cross-section (w_tape ×
# t_tape), so f_tape_min is the minimum tape fraction of the winding pack and
# length_tape is physical tape length, not REBCO-layer length.
# ─────────────────────────────────────────────────────────────────────────────
import os
import numpy as np
import pandas as pd

try:
    import solenoid_lib
    W_TAPE_M = solenoid_lib.w_tape_mm * 1e-3
except (ImportError, AttributeError):
    # Fallback to 4 mm tape if library isn't found
    W_TAPE_M = 0.004

# The exact list of variables you want to save if they are present in the grid
_GRID_FIELDS = [
    "ri", "rf", "th", "a", "a_tape", "v", "v_bore",
    "b0", "b_center", "scan", "je", "je_tape", "je_max", "je_coil", "je_mech",
    "stress", "stress_hoop", "tape_pancake_length", "length_tape",
    "j_crit", "converged", "f_tape_min",
]

# Columns written by this module that are derived from _GRID_FIELDS and must
# not be re-inflated into grids on load.
_DERIVED_COLS = ("row_i", "col_j", "solenoid_length_m",
                 "length_tape_km", "pancake_length_m")

# Old -> new names, applied on load so pre-rename CSVs still open.
_LEGACY_RENAMES = {
    "f_sc": "f_tape_min",
    "length_sc": "length_tape",
    "je_sc": "je_tape",
    "a_sc": "a_tape",
    "rebco_pancake_length": "tape_pancake_length",
}


def save_grids_long(grids, path, solenoid_length=None):
    """
    Save grids by directly dumping the exact variables from the dictionary,
    while appending the specific length metrics requested.
    """
    shape = grids["ri"].shape
    n = grids["ri"].size

    ii, jj = np.indices(shape)
    data = {
        "row_i": ii.ravel(),
        "col_j": jj.ravel(),
        "solenoid_length_m": np.full(n, solenoid_length if solenoid_length is not None else np.nan),
    }

    # Pass through the exact raw variables
    for f in _GRID_FIELDS:
        if f in grids:
            data[f] = np.asarray(grids[f]).ravel()

    # --- Add the requested length metrics ---
    if "length_tape" in grids:
        len_tape_m = np.asarray(grids["length_tape"]).ravel()

        # Total tape length in kilometers
        data["length_tape_km"] = len_tape_m / 1000.0

        # Single pancake length in meters
        if solenoid_length is not None and not np.isnan(solenoid_length):
            num_pancakes = solenoid_length / W_TAPE_M
            data["pancake_length_m"] = len_tape_m / num_pancakes
        else:
            data["pancake_length_m"] = np.full(n, np.nan)

    # Save to disk
    df = pd.DataFrame(data)
    df.to_csv(path, index=False)
    print(f"Saved raw grids -> {path}  ({n} cells)")
    return df


def load_grids_long(path):
    """
    Re-load the CSV back into a dict of 2-D numpy arrays. Legacy column names
    (f_sc, length_sc, je_sc, ...) are mapped to the current ones.
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

        arr = np.full(shape, np.nan)
        arr[rows, cols] = df[col].to_numpy()

        if name == "converged":
            arr = arr.astype(bool)

        grids[name] = arr

    if renamed:
        print(f"[!] {path}: legacy field names remapped ({', '.join(renamed)})")
    print(f"Loaded raw grids from {path}  shape={shape}, fields={list(grids)}")
    return grids


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
        # converged is boolean -> save as int for clean CSV
        out = arr.astype(int) if arr.dtype == bool else arr
        np.savetxt(fname, out, delimiter=",", fmt="%.8g")
        saved.append(fname)

    print(f"Saved {len(saved)} matrix CSVs -> {out_dir}/")
    return saved