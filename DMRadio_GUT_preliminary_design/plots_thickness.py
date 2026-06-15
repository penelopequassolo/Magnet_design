# plots_thickness.py
# ─────────────────────────────────────────────────────────────────────────────
# All plots for the Ri-vs-Th scan: individual field maps + combined maps.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D

import config
import plot_helpers as ph


def _save(fig, name):
    plt.tight_layout()
    fig.savefig(name, dpi=config.SAVE_DPI, bbox_inches="tight")
    plt.show()


def plot_all(grids):
    x = grids["ri"] * 1e3   # mm
    y = grids["th"] * 1e3   # mm
    suffix = ph.title_suffix_th()

    _plot_b0(grids, x, y, suffix)
    _plot_je(grids, x, y, suffix)
    _plot_scan(grids, x, y, suffix)
    _plot_stress(grids, x, y, suffix)
    _plot_jc(grids, x, y, suffix)
    plot_combined(grids, x, y, suffix)


# ── Individual maps ──────────────────────────────────────────────────────────
def _plot_b0(grids, x, y, suffix):
    b0 = grids["b0"]
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(b0), np.nanmax(b0), step=2.0)
    ph.make_contour_plot(fig, ax, x, y, b0, levels, "jet",
                         "%.0f T", "Central field  B₀  (T)",
                         f"Central field B₀  [self-consistent Je]\n{suffix}")
    ph.add_b0_contours(ax, x, y, b0)
    ph.set_mm_axes(ax)
    _save(fig, "contour_B0_sc.png")


def _plot_je(grids, x, y, suffix):
    je = grids["je_sc"]
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(je), np.nanmax(je), n=20)
    ph.make_contour_plot(fig, ax, x, y, je, levels, "plasma",
                         "%.0f", "Self-consistent Je  (A/mm²)",
                         f"Self-consistent operating Je\n{suffix}")
    ph.add_b0_contours(ax, x, y, grids["b0"])
    ph.set_mm_axes(ax)
    _save(fig, "contour_Je_sc.png")


def _plot_scan(grids, x, y, suffix):
    scan = grids["scan"]
    scan_log = np.log10(np.where(scan > 0, scan, np.nan))
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(scan_log), np.nanmax(scan_log), n=20)
    ph.make_contour_plot(
        fig, ax, x, y, scan_log, levels, "jet", "%.1f",
        "log₁₀(Scan rate)  [log₁₀((T²·m^(5/3))²)]",
        r"Scan rate  $(B_0^2 \cdot V_{\rm bore}^{5/3})^2$"
        + f"  [self-consistent Je]\n{suffix}")
    ph.add_b0_contours(ax, x, y, grids["b0"])
    ph.set_mm_axes(ax)
    _save(fig, "contour_scanrate_sc.png")


def _plot_stress(grids, x, y, suffix):
    st = grids["stress"]
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(st), np.nanmax(st), n=20)
    ph.make_contour_plot(
        fig, ax, x, y, st, levels, "jet", "%.0f MPa",
        "Hoop stress  σ_hoop  (MPa)",
        r"Hoop stress  $\sigma_{\rm hoop}$"
        + f"  [self-consistent Je]\n{suffix}")
    ph.add_b0_contours(ax, x, y, grids["b0"])
    ph.set_mm_axes(ax)
    _save(fig, "contour_stress_sc.png")


def _plot_jc(grids, x, y, suffix):
    jc = grids["j_crit"]
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(jc), np.nanmax(jc), n=20)
    ph.make_contour_plot(fig, ax, x, y, jc, levels, "plasma",
                         "%.0f", "Tape Jc  (A/mm²)",
                         f"Tape Jc at self-consistent operating field\n{suffix}")
    ph.add_b0_contours(ax, x, y, grids["b0"])
    ph.set_mm_axes(ax)
    _save(fig, "contour_je_crit_sc.png")


# ── Combined map (B0 + scan rate + hoop stress) ──────────────────────────────
def plot_combined(grids, x, y, suffix):
    b0, scan, stress = grids["b0"], grids["scan"], grids["stress"]
    fig, ax = plt.subplots(figsize=(10, 7))

    # Background: hoop stress
    lvl_st = ph.safe_levels(np.nanmin(stress), np.nanmax(stress), n=30)
    cf = ax.contourf(x, y, stress, levels=lvl_st, cmap="YlOrRd", alpha=0.6)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label("Hoop stress  σ_hoop  (MPa)", fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")

    # Stress iso-lines
    st_lvls = [lv for lv in config.STRESS_ISO_LEVELS
               if np.nanmin(stress) <= lv <= np.nanmax(stress)]
    if st_lvls:
        cs = ax.contour(x, y, stress, levels=st_lvls,
                        colors="saddlebrown", linewidths=1.2, linestyles="-.")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} MPa", fontsize=8,
                  inline=True, inline_spacing=4, colors="saddlebrown")

    # Scan-rate iso-lines (log10)
    scan_log = np.log10(np.where(scan > 0, scan, np.nan))
    sr_lvls = np.arange(np.floor(np.nanmin(scan_log)),
                        np.nanmax(scan_log) + 1, 1)
    if len(sr_lvls) >= 2:
        cs = ax.contour(x, y, scan_log, levels=sr_lvls,
                        colors="steelblue", linewidths=1.2, linestyles="--")
        ax.clabel(cs, fmt=lambda v: f"{v:.1f}", fontsize=8,
                  inline=True, inline_spacing=4, colors="steelblue")

    # B0 iso-lines
    b0_lvls = [lv for lv in config.B0_ISO_LEVELS
               if np.nanmin(b0) <= lv <= np.nanmax(b0)]
    if b0_lvls:
        cs = ax.contour(x, y, b0, levels=b0_lvls,
                        colors="black", linewidths=1.8, linestyles="-")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} T", fontsize=9,
                  inline=True, inline_spacing=4, colors="black")

    handles = [
        Line2D([0], [0], color="black",       lw=1.8, ls="-",  label="B₀  (T)"),
        Line2D([0], [0], color="steelblue",   lw=1.2, ls="--", label="Scan rate  (log₁₀)"),
        Line2D([0], [0], color="saddlebrown", lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")

    ax.set_title(f"Combined map — B₀ / Scan rate / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    ph.set_mm_axes(ax)
    ph.style_axes(ax)
    _save(fig, "contour_combined_sc.png")