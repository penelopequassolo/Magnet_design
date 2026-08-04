# io_grids.py
# ─────────────────────────────────────────────────────────────────────────────
# Save / load result grids to disk as flat CSV (long format) and/or per-field
# 2-D CSV matrices. 
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
    "ri", "rf", "th", "a", "a_sc", "v", "v_bore",
    "b0", "b_center", "scan", "je", "je_sc", "je_max", "je_coil", "je_mech",
    "stress", "stress_hoop", "rebco_pancake_length", "length_sc",
    "j_crit", "margin", "converged", "f_sc"
]

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
    if "length_sc" in grids:
        len_sc_m = np.asarray(grids["length_sc"]).ravel()
        
        # Total SC length in kilometers
        data["length_sc_km"] = len_sc_m / 1000.0
        
        # Single pancake length in meters
        if solenoid_length is not None and not np.isnan(solenoid_length):
            num_pancakes = solenoid_length / W_TAPE_M
            data["pancake_length_m"] = len_sc_m / num_pancakes
        else:
            data["pancake_length_m"] = np.full(n, np.nan)

    # Save to disk
    df = pd.DataFrame(data)
    df.to_csv(path, index=False)
    print(f"Saved raw grids -> {path}  ({n} cells)")
    return df


def load_grids_long(path):
    """
    Re-load the CSV back into a dict of 2-D numpy arrays.
    """
    df = pd.read_csv(path)
    n_rows = int(df["row_i"].max()) + 1
    n_cols = int(df["col_j"].max()) + 1
    shape = (n_rows, n_cols)

    grids = {}
    for col in df.columns:
        if col in ("row_i", "col_j", "solenoid_length_m", "length_sc_km", "pancake_length_m"):
            continue
            
        arr = np.full(shape, np.nan)
        arr[df["row_i"].to_numpy(), df["col_j"].to_numpy()] = df[col].to_numpy()
        
        if col == "converged":
            arr = arr.astype(bool)
            
        grids[col] = arr
        
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