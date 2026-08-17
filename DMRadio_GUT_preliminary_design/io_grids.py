# io_grids.py
# ─────────────────────────────────────────────────────────────────────────────
# Save / load result grids to disk as flat CSV (long format) and/or per-field
# 2-D CSV matrices.
#
# Naming follows grid.py: "tape" means full tape cross-section (w_tape ×
# t_tape), so f_tape_min is the minimum tape fraction of the winding pack and
# length_tape is physical tape length, not REBCO-layer length.
#
# Winding, since it decides what several columns MEAN: n_par tapes are stacked
# radially and paralleled into one electrical turn. n_tp counts TAPES per
# pancake, n_bund counts TURNS per pancake, n_tp = n_par * n_bund. i_margin is
# per tape. length_tape is physical tape and therefore carries a factor n_par.
# n_par is not a per-cell quantity but the file cannot be read without it, so
# it is written as a constant column.
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
# to disk beyond n_par. It lives in config.py / the params object that produced
# the grid.
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


# ── schema stamp ─────────────────────────────────────────────────────────────
# Bump ONLY when an existing field changes MEANING. Adding a field does not
# count: readers keyed by name still get what they asked for.
#   1 : one tape per electrical turn. n_tp = turns per pancake.
#   2 : n_par tapes paralleled into one turn. n_tp now counts TAPES and
#       n_bund counts turns; i_margin is per tape; length_tape scales with
#       n_par. Nothing was renamed, so an unstamped file cannot be told from
#       a current one by inspection — hence this column.
SCHEMA = 2


# ── geometry / axis-derived fields, written first and in this order ──────────
_GEOM_FIELDS = [
    "ri", "rf", "th", "a", "a_tape", "v", "valid",
]

# ── results, used when grid.py cannot be imported ────────────────────────────
_RESULT_FIELDS_FALLBACK = [
    # field / figure of merit
    "b0", "scan",
    # current densities
    "je", "je_coil", "je_tape", "je_max", "j_crit",
    # mechanics
    "stress", "stress_hoop", "sigma_struct", "sigma_tape",
    "util", "f_struct", "f_struct_tape", "f_filler",
    # winding — n_tp counts TAPES, n_bund counts TURNS, n_tp = n_par * n_bund
    "f_tape_min", "f_tape_built", "fill",
    "n_tp_min", "n_tp", "n_bund", "n_tape", "n_tot", "n_par_min",
    "pitch", "pitch_turn",
    "i0", "i_tape", "i_max", "i_margin",
    "length_tape", "tape_pancake_length",
    # circuit / quench
    "em_tot", "em_dump", "l_self", "l_dump", "r_ee", "u_ee", "tau_ee",
    "miits", "j_cu_max", "j_cu", "gamma_op",
    "f_cu_req", "f_cu_have", "f_cu_add", "t_cu_add", "m_cu",
    # flags
    "n_eval", "binding", "binding_code", "feasible", "converged",
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


# Kept as a module-level name because other code imports it. Do NOT use it
# inside this module: RESULT_KEYS can change under a reloaded grid.py while
# io_grids stays cached, and a stale list drops columns silently. The save
# functions call _grid_fields() themselves.
_GRID_FIELDS = _grid_fields()

# Columns written by this module that are metadata or derived from
# _GRID_FIELDS, and must not be re-inflated into 2-D grids on load.
_DERIVED_COLS = ("row_i", "col_j", "schema", "solenoid_length_m",
                 "n_pc", "n_par",
                 "length_tape_km", "pancake_length_m", "piece_length_m")

# Grids that are not plain floats. Everything else round-trips as float64.
_BOOL_FIELDS = ("converged", "feasible", "valid")
_INT8_FIELDS = ("binding_code",)
_TEXT_FIELDS = ("binding",)

# Old -> new names, applied on load so pre-rename CSVs still open. Note that
# the schema-1 -> 2 change renamed NOTHING; it changed meanings, which is why
# the schema column exists and why there is no entry for it here.
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
    fields = _grid_fields()               # fresh: grid.py may have been reloaded
    shape = grids["ri"].shape
    n = grids["ri"].size

    if solenoid_length is None:
        solenoid_length = grids.get("l_built")
        if solenoid_length is None and solenoid_lib is not None:
            solenoid_length = getattr(solenoid_lib, "solenoid_length", None)
    l_val = float(solenoid_length) if solenoid_length is not None else np.nan

    # n_par is not per-cell but the file is unreadable without it: n_tp,
    # i_margin and length_tape all scale with it.
    par = grids.get("params")
    n_par = int(grids.get("n_par", getattr(par, "n_par", 1)))
    n_pc_val = grids.get("n_pc")
    if n_pc_val is None and np.isfinite(l_val):
        n_pc_val = round(l_val / W_TAPE_M)
    n_pc_val = int(n_pc_val) if n_pc_val else 0

    ii, jj = np.indices(shape)
    data = {
        "row_i": ii.ravel(),
        "col_j": jj.ravel(),
        "schema": np.full(n, SCHEMA, dtype=np.int16),
        "solenoid_length_m": np.full(n, l_val),
        "n_pc": np.full(n, n_pc_val, dtype=np.int32),
        "n_par": np.full(n, n_par, dtype=np.int32),
    }

    # Pass through the exact raw variables
    skipped = []
    for f in fields:
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

        if n_pc_val:
            # All the tape in one pancake, n_par pieces' worth.
            data["pancake_length_m"] = len_tape_m / n_pc_val
            # One CONTINUOUS piece = n_bund turns, and there are n_par pieces
            # per pancake. This is the deliverable-length constraint, and it
            # is total/(n_pc*n_par), NOT total/n_pc.
            data["piece_length_m"] = len_tape_m / (n_pc_val * n_par)
        else:
            data["pancake_length_m"] = np.full(n, np.nan)
            data["piece_length_m"] = np.full(n, np.nan)

    # Save to disk
    df = pd.DataFrame(data)
    df.to_csv(path, index=False)
    n_ok = int(np.count_nonzero(np.isfinite(np.asarray(
        grids.get("b0", np.full(shape, np.nan)), dtype=float))))
    print(f"Saved raw grids    -> {path}  ({n} cells, {n_ok} feasible, "
          f"{len(df.columns)} columns, schema {SCHEMA}, n_par={n_par})")
    if skipped:
        print(f"  not per-cell, not saved: {', '.join(skipped)}")
    return df


def load_grids_long(path):
    """
    Re-load the CSV back into a dict of 2-D numpy arrays. Legacy column names
    (f_sc, length_sc, je_sc, ...) are mapped to the current ones, and the
    boolean / int8 / text grids are restored to their original dtype.

    Metadata columns (schema, n_par, n_pc, solenoid_length_m) come back as
    scalars under l_built / n_pc / n_par / schema, not as maps.
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

    # constant columns: metadata, not maps
    for col, key in (("solenoid_length_m", "l_built"), ("n_pc", "n_pc"),
                     ("n_par", "n_par"), ("schema", "schema")):
        if col in df.columns:
            v = pd.to_numeric(df[col], errors="coerce").iloc[0]
            if np.isfinite(v):
                grids[key] = float(v) if key == "l_built" else int(v)

    s = grids.get("schema", 1)
    if s < SCHEMA:
        print(f"[!] {path}: schema {s}, current is {SCHEMA}. In schema 1 "
              f"n_tp counted TURNS per pancake (it now counts TAPES), "
              f"i_margin was per terminal current (now per tape), and "
              f"length_tape had no n_par factor. b0/Je/stress are unaffected; "
              f"re-run the scan before trusting anything winding-related.")

    if renamed:
        print(f"[!] {path}: legacy field names remapped ({', '.join(renamed)})")
    print(f"Loaded raw grids from {path}  shape={shape}, "
          f"n_par={grids.get('n_par', '?')}, fields={list(grids)}")
    return grids


# ═════════════════════════════════════════════════════════════════════════════
# Per-field 2-D matrices
# ═════════════════════════════════════════════════════════════════════════════
def save_grids_matrices(grids, out_dir, prefix=""):
    """
    Save each 2-D field as its own CSV matrix (rows x cols).

    No metadata is carried here — a bare matrix has nowhere to put it — so
    these files are for eyeballing and for feeding other tools, not for
    round-tripping. Use save_grids_long / load_grids_long for that.
    """
    fields = _grid_fields()               # fresh: grid.py may have been reloaded
    os.makedirs(out_dir, exist_ok=True)
    shape = grids["ri"].shape
    saved = []

    for f in fields:
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