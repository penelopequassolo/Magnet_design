# plots_area.py
# ─────────────────────────────────────────────────────────────────────────────
# Combined maps for the Ri-vs-A (conductor cross-section) scan.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
from matplotlib.lines import Line2D

import config
import solenoid_lib
import plot_helpers as ph


def _save(fig, name):
    plt.tight_layout()
    fig.savefig(name, dpi=config.SAVE_DPI, bbox_inches="tight")
    plt.show()


def _setup_area_axes(ax, y):
    ax.set_xlabel("Inner radius  Ri  (mm)", fontsize=12)
    ax.set_ylabel("Superconductor total length  L_SC  (m)", fontsize=12)
    ax.xaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.yaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.set_ylim(0, np.nanmax(y))


def _draw_background_stress(fig, ax, x, y, stress):
    lvl = ph.safe_levels(np.nanmin(stress), np.nanmax(stress), n=30)
    cf = ax.contourf(x, y, stress, levels=lvl, cmap="YlOrRd", alpha=0.6)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label("Hoop stress  σ_hoop  (MPa)", fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")


def _draw_stress_iso(ax, x, y, stress):
    lvls = [lv for lv in config.STRESS_ISO_LEVELS
            if np.nanmin(stress) <= lv <= np.nanmax(stress)]
    if lvls:
        cs = ax.contour(x, y, stress, levels=lvls,
                        colors="saddlebrown", linewidths=1.2, linestyles="-.")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} MPa", fontsize=8,
                  inline=True, inline_spacing=4, colors="saddlebrown")


def _draw_b0_iso(ax, x, y, b0):
    lvls = [lv for lv in config.B0_ISO_LEVELS
            if np.nanmin(b0) <= lv <= np.nanmax(b0)]
    if lvls:
        cs = ax.contour(x, y, b0, levels=lvls,
                        colors="black", linewidths=1.8, linestyles="-")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} T", fontsize=9,
                  inline=True, inline_spacing=4, colors="black")


def _draw_thickness_iso(ax, x, y, th_grid):
    th_iso_mm = [10, 25, 50, 75, 100, 125, 150]
    th_data = th_grid * 1e3
    plot = [lv for lv in th_iso_mm
            if np.nanmin(th_data) <= lv <= np.nanmax(th_data)]
    if plot:
        cs = ax.contour(x, y, th_data, levels=plot,
                        colors="dimgrey", linewidths=0.8, linestyles=":")
        ax.clabel(cs, fmt=lambda v: f"Th={v:.0f} mm", fontsize=7,
                  inline=True, inline_spacing=4, colors="dimgrey")


# ─────────────────────────────────────────────────────────────────────────────
def plot_all(grids):
    x = grids["ri"] * 1e3                              # mm
    y = grids["rebco_total_length"]                           # m (SC total length)
    suffix = ph.title_suffix_area()

    plot_combined_scan_log(grids, x, y, suffix)
    plot_combined_b2v(grids, x, y, suffix)


def _finish(ax, suffix, title_metric, legend_metric_label, legend_metric_color):
    handles = [
        Line2D([0], [0], color="black",       lw=1.8, ls="-",  label="B₀  (T)"),
        Line2D([0], [0], color=legend_metric_color, lw=1.2, ls="--",
               label=legend_metric_label),
        Line2D([0], [0], color="saddlebrown", lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
        Line2D([0], [0], color="dimgrey",     lw=0.8, ls=":",  label="Thickness  (mm)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")
    ax.set_title(f"Combined map — B₀ / {title_metric} / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    _setup_area_axes(ax)
    ph.style_axes_grid(ax)


def plot_combined_scan_log(grids, x, y, suffix):
    fig, ax = plt.subplots(figsize=(10, 7))
    _draw_background_stress(fig, ax, x, y, grids["stress"])
    _draw_stress_iso(ax, x, y, grids["stress"])

    scan_log = np.log10(np.where(grids["scan"] > 0, grids["scan"], np.nan))
    if np.isfinite(scan_log).any():
        lvls = np.arange(np.floor(np.nanmin(scan_log)),
                         np.nanmax(scan_log) + 1, 1)
        if len(lvls) >= 2:
            cs = ax.contour(x, y, scan_log, levels=lvls,
                            colors="steelblue", linewidths=1.2, linestyles="--")
            ax.clabel(cs, fmt=lambda v: f"{v:.1f}", fontsize=8,
                      inline=True, inline_spacing=4, colors="steelblue")

    _draw_b0_iso(ax, x, y, grids["b0"])
    _draw_thickness_iso(ax, x, y, grids["th"])
    _finish(ax, suffix, "Scan rate", "Scan rate  (log₁₀)", "steelblue")
    _save(fig, "contour_combined_Ascan_sc.png")


def plot_combined_b2v(grids, x, y, suffix):
    fig, ax = plt.subplots(figsize=(10, 7))
    _draw_background_stress(fig, ax, x, y, grids["stress"])
    _draw_stress_iso(ax, x, y, grids["stress"])

    b2v = grids["b0"]**2 * grids["v"]   # T²·m³  (bore volume)
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
    _finish(ax, suffix, "B²V", "B²V  (T²·m³)", "steelblue")
    _save(fig, "contour_combined_Ascan_b2v.png")