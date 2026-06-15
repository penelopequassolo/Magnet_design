# io_grids.py
# ─────────────────────────────────────────────────────────────────────────────
# Save / load result grids to disk as flat CSV (long format) and/or per-field
# 2-D CSV matrices. Long format is the most robust for re-loading + analysis.
# ─────────────────────────────────────────────────────────────────────────────
import os
import numpy as np
import pandas as pd


# Fields that are 2-D grids we want to persist.
_GRID_FIELDS = [
    "ri", "th", "rf", "v", "a",          # geometry (a only present in A-scan)
    "b0", "scan", "stress", "j_crit",
    "je_max", "je_sc", "margin", "converged",
]


def save_grids_long(grids, path):
    """
    Save all grids in a single tidy/long CSV: one row per cell, one column
    per field. This is the easiest format to re-load and to inspect.
    """
    present = [f for f in _GRID_FIELDS if f in grids]
    shape = grids["ri"].shape
    n = grids["ri"].size

    data = {}
    # Cell indices help you reshape back to 2-D later.
    ii, jj = np.indices(shape)
    data["row_i"] = ii.ravel()
    data["col_j"] = jj.ravel()

    for f in present:
        arr = np.asarray(grids[f])
        if arr.shape != shape:
            # skip anything that isn't a full grid (defensive)
            continue
        data[f] = arr.ravel()

    df = pd.DataFrame(data)
    df.attrs["n_rows"] = shape[0]
    df.attrs["n_cols"] = shape[1]
    df.to_csv(path, index=False)
    print(f"Saved long-format grids -> {path}  ({n} cells, {len(present)} fields)")
    return df


def save_grids_matrices(grids, out_dir, prefix=""):
    """
    Save each 2-D field as its own CSV matrix (rows x cols). Useful if you want
    to open a single quantity directly in Excel as a grid.
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


def load_grids_long(path):
    """
    Re-load a long-format CSV back into a dict of 2-D numpy arrays.
    Reconstructs shape from the row_i / col_j columns.
    """
    df = pd.read_csv(path)
    n_rows = int(df["row_i"].max()) + 1
    n_cols = int(df["col_j"].max()) + 1
    shape = (n_rows, n_cols)

    grids = {}
    for col in df.columns:
        if col in ("row_i", "col_j"):
            continue
        arr = np.full(shape, np.nan)
        arr[df["row_i"].to_numpy(), df["col_j"].to_numpy()] = df[col].to_numpy()
        if col == "converged":
            arr = arr.astype(bool)
        grids[col] = arr
    print(f"Loaded grids from {path}  shape={shape}, fields={list(grids)}")
    return grids