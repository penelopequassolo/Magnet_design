# plots_area.py
import importlib

import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
from matplotlib.lines import Line2D
from scipy.interpolate import griddata

import config
import solenoid_lib
import plot_helpers as ph
importlib.reload(ph)

# ─────────────────────────────────────────────────────────────────────────────
# Helpers
# ─────────────────────────────────────────────────────────────────────────────

def _save(fig, name, label=""):
    plt.tight_layout()
    suffix = f"_{label}" if label else ""
    fig.savefig(f"{name}{suffix}.png", dpi=config.SAVE_DPI, bbox_inches="tight")
    plt.show()


def _setup_area_axes(ax, grids):
    y = grids["rebco_pancake_length"]
    ax.set_xlabel("Inner radius  Ri  (mm)", fontsize=12)
    ax.set_ylabel("REBCO pancake length  L_SC  (m)", fontsize=12)
    ax.xaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.yaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.set_ylim(np.nanmin(y), np.nanmax(y))


def _regrid(x, y, z, nx=400, ny=400):
    xf, yf, zf = x.flatten(), y.flatten(), z.flatten()
    mask = np.isfinite(xf) & np.isfinite(yf) & np.isfinite(zf)
    xi = np.linspace(np.nanmin(x), np.nanmax(x), nx)
    yi = np.linspace(np.nanmin(y), np.nanmax(y), ny)
    xi2d, yi2d = np.meshgrid(xi, yi)
    zi2d = griddata((xf[mask], yf[mask]), zf[mask],
                    (xi2d, yi2d), method="linear")
    return xi2d, yi2d, zi2d


def _finish(ax, grids, suffix, title_metric, legend_metric_label,
            legend_metric_color):
    handles = [
        Line2D([0], [0], color="black",             lw=1.8, ls="-",  label="B₀  (T)"),
        Line2D([0], [0], color=legend_metric_color, lw=1.2, ls="--", label=legend_metric_label),
        Line2D([0], [0], color="crimson",           lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
        Line2D([0], [0], color="dimgrey",           lw=0.8, ls=":",  label="Thickness  (mm)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")
    ax.set_title(f"Combined map — B₀ / {title_metric} / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    _setup_area_axes(ax, grids)
    ph.style_axes_grid(ax)


# ─────────────────────────────────────────────────────────────────────────────
# Background fills
# ─────────────────────────────────────────────────────────────────────────────

def _draw_background_stress(fig, ax, x, y, stress):
    lvl = ph.safe_levels(np.nanmin(stress), np.nanmax(stress), n=30)
    cf = ax.contourf(x, y, stress, levels=lvl, cmap="YlOrRd", alpha=0.6)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label("Hoop stress  σ_hoop  (MPa)", fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")


def _draw_background_scanlog(fig, ax, x, y, scan):
    MAX_SCAN_YEARS = 30
    scan_masked = np.where((scan > 0) & (scan < MAX_SCAN_YEARS), scan, np.nan)
    if not np.isfinite(scan_masked).any():
        return scan_masked
    xi, yi, zi = _regrid(x, y, scan_masked)
    lvl = ph.safe_levels(np.nanmin(zi[np.isfinite(zi)]),
                         np.nanmax(zi[np.isfinite(zi)]), n=30)
    cf = ax.contourf(xi, yi, zi, levels=lvl, cmap="viridis", alpha=0.8)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label("Scan time  (yr)", fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")
    return scan_masked


def _draw_background_b2v(fig, ax, x, y, b2v):
    lvl = ph.safe_levels(np.nanmin(b2v), np.nanmax(b2v), n=30)
    cf = ax.contourf(x, y, b2v, levels=lvl, cmap="viridis", alpha=0.8)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label("B²V  (T²·m³)", fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")


# ─────────────────────────────────────────────────────────────────────────────
# Iso-contour overlays
# ─────────────────────────────────────────────────────────────────────────────

def _draw_stress_iso(ax, x, y, stress):
    xi, yi, zi = _regrid(x, y, stress)
    lvls = [lv for lv in config.STRESS_ISO_LEVELS
            if np.nanmin(zi[np.isfinite(zi)]) <= lv <= np.nanmax(zi[np.isfinite(zi)])]
    if lvls:
        cs = ax.contour(xi, yi, zi, levels=lvls,
                        colors="crimson", linewidths=1.2, linestyles="-.")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} MPa", fontsize=8,
                  inline=True, inline_spacing=4, colors="crimson")


def _draw_b0_iso(ax, x, y, b0):
    xi, yi, zi = _regrid(x, y, b0)
    lvls = [lv for lv in config.B0_ISO_LEVELS
            if np.nanmin(zi[np.isfinite(zi)]) <= lv <= np.nanmax(zi[np.isfinite(zi)])]
    if lvls:
        cs = ax.contour(xi, yi, zi, levels=lvls,
                        colors="black", linewidths=1.8, linestyles="-")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} T", fontsize=9,
                  inline=True, inline_spacing=4, colors="black")


def _draw_thickness_iso(ax, x, y, th_grid):
    th_data = th_grid * 1e3
    xi, yi, zi = _regrid(x, y, th_data)
    th_iso_mm = [10, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500]
    plot = [lv for lv in th_iso_mm
            if np.nanmin(zi[np.isfinite(zi)]) <= lv <= np.nanmax(zi[np.isfinite(zi)])]
    if plot:
        cs = ax.contour(xi, yi, zi, levels=plot,
                        colors="dimgrey", linewidths=0.8, linestyles=":")
        ax.clabel(cs, fmt=lambda v: f"Th={v:.0f} mm", fontsize=7,
                  inline=True, inline_spacing=4, colors="dimgrey")


def _draw_scan_iso(ax, x, y, scan_masked):
    xi, yi, zi = _regrid(x, y, scan_masked)
    sr_lvls = [1, 3, 6, 10, 15, 20]
    lvls = [lv for lv in sr_lvls
            if np.nanmin(zi[np.isfinite(zi)]) <= lv <= np.nanmax(zi[np.isfinite(zi)])]
    if lvls and np.isfinite(zi).any():
        cs = ax.contour(xi, yi, zi, levels=lvls,
                        colors="steelblue", linewidths=1.2, linestyles="--")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} yr", fontsize=8,
                  inline=True, inline_spacing=4, colors="steelblue")


# ─────────────────────────────────────────────────────────────────────────────
# Plot functions
# ─────────────────────────────────────────────────────────────────────────────

def plot_all(grids, label=""):
    x = grids["ri"] * 1e3
    y = grids["rebco_pancake_length"]
    suffix = ph.title_suffix_area()

    #plot_combined_scan_log(grids, x, y, suffix, label)
    #plot_combined_b2v(grids, x, y, suffix, label)
    plot_combined_scan_log_contour(grids, x, y, suffix, label)
    #plot_combined_b2v_contour(grids, x, y, suffix, label)


def plot_combined_scan_log(grids, x, y, suffix, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    _draw_background_stress(fig, ax, x, y, grids["stress"])
    _draw_stress_iso(ax, x, y, grids["stress"])
    MAX_SCAN_YEARS = 30
    scan_masked = np.where(
        (grids["scan"] > 0) & (grids["scan"] < MAX_SCAN_YEARS), grids["scan"], np.nan)
    _draw_scan_iso(ax, x, y, scan_masked)
    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_thickness_iso(ax, x, y, grids["th"])
    _finish(ax, grids, suffix, "Scan time", "Scan time  (yr)", "steelblue")
    _save(fig, "contour_combined_Ascan_sc_stress_bg", label)


def plot_combined_b2v(grids, x, y, suffix, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    _draw_background_stress(fig, ax, x, y, grids["stress"])
    _draw_stress_iso(ax, x, y, grids["stress"])
    b2v = grids["b0"]**2 * grids["v"]
    b2v = np.where(np.isfinite(b2v) & (b2v > 0), b2v, np.nan)
    if np.isfinite(b2v).any():
        bv_min, bv_max = np.nanmin(b2v), np.nanmax(b2v)
        lvls = ticker.MaxNLocator(nbins=8, prune=None).tick_values(bv_min, bv_max)
        lvls = [lv for lv in lvls if bv_min < lv < bv_max]
        if lvls:
            cs = ax.contour(x, y, b2v, levels=lvls,
                            colors="steelblue", linewidths=1.2, linestyles="--")
            ax.clabel(cs, fmt=lambda v: f"{v:.3g}", fontsize=8,
                      inline=True, inline_spacing=4, colors="steelblue")
    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_thickness_iso(ax, x, y, grids["th"])
    _finish(ax, grids, suffix, "B²V", "B²V  (T²·m³)", "steelblue")
    _save(fig, "contour_combined_Ascan_b2v_stress_bg", label)


def plot_combined_scan_log_contour(grids, x, y, suffix, label=""):
    fig, ax = plt.subplots(figsize=(10, 7))
    scan_masked = _draw_background_scanlog(fig, ax, x, y, grids["scan"])
    _draw_stress_iso(ax, x, y, grids["stress"])
    _draw_scan_iso(ax, x, y, scan_masked)
    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_thickness_iso(ax, x, y, grids["th"])
    _finish(ax, grids, suffix, "Scan time", "Scan time  (yr)", "steelblue")
    _save(fig, "contour_combined_Ascan_sc_filled", label)


def plot_combined_b2v_contour(grids, x, y, suffix, label=""):
    b2v = grids["b0"]**2 * grids["v"]
    b2v = np.where(np.isfinite(b2v) & (b2v > 0), b2v, np.nan)
    fig, ax = plt.subplots(figsize=(10, 7))
    if np.isfinite(b2v).any():
        _draw_background_b2v(fig, ax, x, y, b2v)
        _draw_stress_iso(ax, x, y, grids["stress"])
        bv_min, bv_max = np.nanmin(b2v), np.nanmax(b2v)
        lvls = ticker.MaxNLocator(nbins=8, prune=None).tick_values(bv_min, bv_max)
        lvls = [lv for lv in lvls if bv_min < lv < bv_max]
        if lvls:
            cs = ax.contour(x, y, b2v, levels=lvls,
                            colors="steelblue", linewidths=1.2, linestyles="--")
            ax.clabel(cs, fmt=lambda v: f"{v:.3g}", fontsize=8,
                      inline=True, inline_spacing=4, colors="steelblue")
    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_thickness_iso(ax, x, y, grids["th"])
    _finish(ax, grids, suffix, "B²V", "B²V  (T²·m³)", "steelblue")
    _save(fig, "contour_combined_Ascan_b2v_filled", label)