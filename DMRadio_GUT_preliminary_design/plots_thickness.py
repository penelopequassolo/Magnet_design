import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
from matplotlib.lines import Line2D

import config
import plot_helpers as ph

def _save(fig, name, label=""):
    plt.tight_layout()
    suffix = f"_{label}" if label else ""
    fig.savefig(f"{name}{suffix}.png", dpi=config.SAVE_DPI, bbox_inches="tight")
    plt.show()

def _setup_thickness_axes(ax, y):
    ax.set_xlabel("Inner radius  Ri  (mm)", fontsize=12)
    ax.set_ylabel("Coil thickness  Th  (mm)", fontsize=12)
    ax.xaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.yaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.set_ylim(np.nanmin(y), np.nanmax(y))

def _finish_thickness_plot(ax, y, title_metric, legend_metric_label=None, legend_metric_color=None):
    handles = [
        Line2D([0], [0], color="black", lw=1.8, ls="-",  label="B₀  (T)"),
        Line2D([0], [0], color="crimson", lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
        Line2D([0], [0], color="purple", lw=0.8, ls=":",  label="SC Tape Length (km)")
    ]
    
    if legend_metric_label:
        handles.insert(1, Line2D([0], [0], color=legend_metric_color, lw=1.2, ls="--", label=legend_metric_label))
        
    ax.legend(handles=handles, fontsize=10, loc="upper left", framealpha=0.85, edgecolor="grey")
    ax.set_title(f"Thickness Map — B₀ / {title_metric} / Hoop stress\n[self-consistent Je]", fontsize=10, pad=15)
    _setup_thickness_axes(ax, y)
    ph.style_axes_grid(ax)

def _draw_length_iso(ax, x, y, length_sc):
    # Convert length to km
    len_km = length_sc / 1000.0
    if not np.isfinite(len_km).any(): return
    
    # Generate some reasonable contour levels for length
    min_len, max_len = np.nanmin(len_km), np.nanmax(len_km)
    lvls = ticker.MaxNLocator(nbins=10).tick_values(min_len, max_len)
    plot_lvls = [lv for lv in lvls if min_len <= lv <= max_len]
    
    if plot_lvls:
        cs = ax.contour(x, y, len_km, levels=plot_lvls, colors="purple", linewidths=0.8, linestyles=":")
        ax.clabel(cs, fmt=lambda v: f"{v:.1f} km", fontsize=7, inline=True, inline_spacing=4, colors="purple")

# ─────────────────────────────────────────────────────────────────────────────
# Plot 1: Scan Time Background (Thickness vs Radius)
# ─────────────────────────────────────────────────────────────────────────────
def plot_thickness_scan(grids, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    
    x = grids["ri"] * 1e3
    y = grids["th"] * 1e3
    
    # 1. Background Fill: Scan Time
    MAX_SCAN_YEARS = 30
    scan_masked = np.where((grids["scan"] > 0) & (grids["scan"] < MAX_SCAN_YEARS), grids["scan"], np.nan)
    if np.isfinite(scan_masked).any():
        lvl = ph.safe_levels(np.nanmin(scan_masked), np.nanmax(scan_masked), n=30)
        cf = ax.contourf(x, y, scan_masked, levels=lvl, cmap="viridis", alpha=0.8)
        cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top", pad=0.02, aspect=40, shrink=0.95)
        cbar.set_label("Scan time  (yr)", fontsize=10, labelpad=4)
        cbar.ax.tick_params(labelsize=9, direction="in")

    # 2. Contours
    from plots_area import _draw_stress_iso, _draw_b0_iso, _draw_scan_iso
    _draw_stress_iso(ax, x, y, grids["stress"])
    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_scan_iso(ax, x, y, scan_masked)
    
    # Plot the actual tape length as a contour layer
    if "length_sc" in grids:
        _draw_length_iso(ax, x, y, grids["length_sc"])

    _finish_thickness_plot(ax, y, "Scan time", "Scan time (yr)", "steelblue")
    _save(fig, "contour_thickness_scan", label)

# ─────────────────────────────────────────────────────────────────────────────
# Plot 2: Coil Current Density Background (Thickness vs Radius)
# ─────────────────────────────────────────────────────────────────────────────
def plot_thickness_je_coil(grids, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    
    x = grids["ri"] * 1e3
    y = grids["th"] * 1e3
    
    f_sc = grids.get("f_sc", np.ones_like(grids["je_sc"]))
    je_coil = grids["je_sc"] * f_sc
    converged = grids.get("converged", np.ones_like(je_coil, dtype=bool))
    je_coil[~converged] = np.nan
    
    # 1. Background Fill: Coil Je
    if np.isfinite(je_coil).any():
        lvl = ph.safe_levels(np.nanmin(je_coil), np.nanmax(je_coil), n=30)
        cf = ax.contourf(x, y, je_coil, levels=lvl, cmap="plasma", alpha=0.8)
        cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top", pad=0.02, aspect=40, shrink=0.95)
        cbar.set_label("Coil Winding Current Density  Je_coil  (A/mm²)", fontsize=10, labelpad=4)
        cbar.ax.tick_params(labelsize=9, direction="in")

    # Space Limit boundary
    f_sc_clean = np.clip(f_sc, 0, 1.0)
    cs_f = ax.contour(x, y, f_sc_clean, levels=[0.999], colors='black', linewidths=3, linestyles='--')
    ax.clabel(cs_f, inline=True, fmt="Space Limit (f_sc=1)", fontsize=10)

    # 2. Contours
    from plots_area import _draw_stress_iso, _draw_b0_iso
    _draw_stress_iso(ax, x, y, grids["stress"])
    _draw_b0_iso(ax, x, y, grids["b0"])
    
    if "length_sc" in grids:
        _draw_length_iso(ax, x, y, grids["length_sc"])

    _finish_thickness_plot(ax, y, "Coil Je")
    _save(fig, "contour_thickness_je_coil", label)

# ─────────────────────────────────────────────────────────────────────────────
# Plot 3: Tape Current Density Background (Thickness vs Radius)
# ─────────────────────────────────────────────────────────────────────────────
def plot_thickness_je_sc(grids, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    
    x = grids["ri"] * 1e3
    y = grids["th"] * 1e3
    
    je_sc = grids["je_sc"].copy()
    converged = grids.get("converged", np.ones_like(je_sc, dtype=bool))
    je_sc[~converged] = np.nan
    
    # 1. Background Fill: Tape Je
    if np.isfinite(je_sc).any():
        lvl = ph.safe_levels(np.nanmin(je_sc), np.nanmax(je_sc), n=30)
        cf = ax.contourf(x, y, je_sc, levels=lvl, cmap="magma", alpha=0.8)
        cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top", pad=0.02, aspect=40, shrink=0.95)
        cbar.set_label("SC Tape Current Density  Je_tape  (A/mm²)", fontsize=10, labelpad=4)
        cbar.ax.tick_params(labelsize=9, direction="in")

    # Space Limit boundary (shows where the coil becomes mechanically limited)
    if "f_sc" in grids:
        f_sc_clean = np.clip(grids["f_sc"], 0, 1.0)
        cs_f = ax.contour(x, y, f_sc_clean, levels=[0.999], colors='black', linewidths=3, linestyles='--')
        ax.clabel(cs_f, inline=True, fmt="Space Limit (f_sc=1)", fontsize=10)

    # 2. Contours
    from plots_area import _draw_stress_iso, _draw_b0_iso
    _draw_stress_iso(ax, x, y, grids["stress"])
    _draw_b0_iso(ax, x, y, grids["b0"])
    
    if "length_sc" in grids:
        _draw_length_iso(ax, x, y, grids["length_sc"])

    _finish_thickness_plot(ax, y, "Tape Je")
    _save(fig, "contour_thickness_je_sc", label)


# ─────────────────────────────────────────────────────────────────────────────
# Main wrapper function expected by the execution loop
# ─────────────────────────────────────────────────────────────────────────────
def plot_all(grids, label="", solenoid_length_m=1.0):
    """
    Generate all thickness-based contour plots.
    Matches the signature expected by the main execution loop.
    """
    plot_thickness_scan(grids, label=label)
    plot_thickness_je_coil(grids, label=label)
    plot_thickness_je_sc(grids, label=label)
