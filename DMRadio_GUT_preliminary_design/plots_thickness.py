# plots_thickness.py
# ─────────────────────────────────────────────────────────────────────────────
# All plots for the Ri-vs-Th scan: individual field maps + combined maps.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D

import config
import plot_helpers as ph

import matplotlib.ticker as ticker

from plots_area import plot_combined_scan_log

def _save(fig, name):
    plt.tight_layout()
    fig.savefig(name, dpi=config.SAVE_DPI, bbox_inches="tight")
    plt.show()


def plot_all(grids):
    x = grids["ri"] * 1e3   # mm
    y = grids["th"] * 1e3   # mm
    suffix = ph.title_suffix_th()

    #_plot_b0(grids, x, y, suffix)
    #_plot_je(grids, x, y, suffix)
    #_plot_scan(grids, x, y, suffix)
    #_plot_stress(grids, x, y, suffix)
    #_plot_jc(grids, x, y, suffix)
    plot_combined_scan_log(grids, x, y, suffix)
    plot_combined_b2v(grids, x, y, suffix)
    plot_combined_scan_log_contour(grids, x, y, suffix)
    plot_combined_b2v_contour(grids, x, y, suffix)


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
    scan_log = np.where(scan > 0, scan, np.nan)
    fig, ax = plt.subplots(figsize=(10, 7))
    levels = ph.safe_levels(np.nanmin(scan_log), np.nanmax(scan_log), n=20)
    ph.make_contour_plot(
        fig, ax, x, y, scan_log, levels, "jet", "%.1f",
        "log₁₀(Scan rate)  [log₁₀((T²·m^(5/3))²)]",
        r"Scan rate  $$(B_0^2 \cdot V_{\rm bore}^{5/3})^2$$"
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
        r"Hoop stress  $$\sigma_{\rm hoop}$$"
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
def plot_combined_scan_log(grids, x, y, suffix):
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

    # Scan time iso-lines [years]
    MAX_SCAN_YEARS = 30
    scan_masked = np.where((scan > 0) & (scan < MAX_SCAN_YEARS), scan, np.nan)
    sr_lvls = [1, 3, 6, 10, 15, 20]
    if np.isfinite(scan_masked).any():
        cs = ax.contour(x, y, scan_masked, levels=sr_lvls,
                        colors="steelblue", linewidths=1.2, linestyles="--")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} yr", fontsize=8,
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
        Line2D([0], [0], color="steelblue",   lw=1.2, ls="--", label="Scan time  (yr)"),
        Line2D([0], [0], color="saddlebrown", lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")

    ax.set_title(f"Combined map — B₀ / Scan time / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    ph.set_mm_axes(ax)
    ph.style_axes(ax)

    _add_ref_magnets(ax, show_je=True, show_h=True)

    _save(fig, "contour_combined_sc.png")


def plot_combined_b2v(grids, x, y, suffix):
    b0, b2v, stress = grids["b0"], grids["b0"]**2 * grids["v"], grids["stress"]
    b2v = np.where(np.isfinite(b2v) & (b2v > 0), b2v, np.nan)
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

    # B2V iso-lines
    if np.isfinite(b2v).any():
        bv_min, bv_max = np.nanmin(b2v), np.nanmax(b2v)
        lvls = ticker.MaxNLocator(nbins=8, prune=None).tick_values(bv_min, bv_max)
        lvls = [lv for lv in lvls if bv_min < lv < bv_max]
        if lvls:
            cs = ax.contour(x, y, b2v, levels=lvls,
                            colors="steelblue", linewidths=1.2, linestyles="--")
            ax.clabel(cs, fmt=lambda v: f"{v:.3g}", fontsize=8,
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
        Line2D([0], [0], color="steelblue",   lw=1.2, ls="--", label="B²V  (T²·m³)"),
        Line2D([0], [0], color="saddlebrown", lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")

    ax.set_title(f"Combined map — B₀ / B²V / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    ph.set_mm_axes(ax)
    ph.style_axes(ax)

    _add_ref_magnets(ax, show_je=True, show_h=True)

    _save(fig, "contour_combined_b2v.png")


def plot_combined_scan_log_contour(grids, x, y, suffix):
    b0, scan, stress = grids["b0"], grids["scan"], grids["stress"]
    fig, ax = plt.subplots(figsize=(10, 7))

    # Background: scan time
    MAX_SCAN_YEARS = 30
    scan_masked = np.where((scan > 0) & (scan < MAX_SCAN_YEARS), scan, np.nan)

    if np.isfinite(scan_masked).any():
        lvl_sc = ph.safe_levels(np.nanmin(scan_masked), np.nanmax(scan_masked), n=30)
        cf = ax.contourf(x, y, scan_masked, levels=lvl_sc, cmap="viridis", alpha=0.8)
        cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                            pad=0.02, aspect=40, shrink=0.95)
        cbar.set_label("Scan time  (yr)", fontsize=10, labelpad=4)
        cbar.ax.tick_params(labelsize=9, direction="in")

    # Stress iso-lines
    st_lvls = [lv for lv in config.STRESS_ISO_LEVELS
               if np.nanmin(stress) <= lv <= np.nanmax(stress)]
    if st_lvls:
        cs = ax.contour(x, y, stress, levels=st_lvls,
                        colors="crimson", linewidths=1.2, linestyles="-.")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} MPa", fontsize=8,
                  inline=True, inline_spacing=4, colors="crimson")

    # Scan time iso-lines [years]
    sr_lvls = [1, 3, 6, 10, 15, 20]
    if np.isfinite(scan_masked).any():
        cs = ax.contour(x, y, scan_masked, levels=sr_lvls,
                        colors="steelblue", linewidths=1.2, linestyles="--")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} yr", fontsize=8,
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
        Line2D([0], [0], color="steelblue",   lw=1.2, ls="--", label="Scan time  (yr)"),
        Line2D([0], [0], color="crimson",     lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")
    ax.set_title(f"Combined map — B₀ / Scan time / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    ph.set_mm_axes(ax)
    ph.style_axes(ax)

    _add_ref_magnets(ax, show_je=True, show_h=True)

    _save(fig, "contour_combined_sc_contour.png")


def plot_combined_b2v_contour(grids, x, y, suffix):
    b0, b2v, stress = grids["b0"], grids["b0"]**2 * grids["v"], grids["stress"]
    b2v = np.where(np.isfinite(b2v) & (b2v > 0), b2v, np.nan)
    fig, ax = plt.subplots(figsize=(10, 7))

    # Background: B2V
    if np.isfinite(b2v).any():
        bv_min, bv_max = np.nanmin(b2v), np.nanmax(b2v)
        lvl_bv = ph.safe_levels(bv_min, bv_max, n=30)
        cf = ax.contourf(x, y, b2v, levels=lvl_bv, cmap="viridis", alpha=0.8)
        cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                            pad=0.02, aspect=40, shrink=0.95)
        cbar.set_label("B²V  (T²·m³)", fontsize=10, labelpad=4)
        cbar.ax.tick_params(labelsize=9, direction="in")

    # Stress iso-lines
    st_lvls = [lv for lv in config.STRESS_ISO_LEVELS
               if np.nanmin(stress) <= lv <= np.nanmax(stress)]
    if st_lvls:
        cs = ax.contour(x, y, stress, levels=st_lvls,
                        colors="crimson", linewidths=1.2, linestyles="-.")
        ax.clabel(cs, fmt=lambda v: f"{v:.0f} MPa", fontsize=8,
                  inline=True, inline_spacing=4, colors="crimson")

    # B2V iso-lines
    if np.isfinite(b2v).any():
        lvls = ticker.MaxNLocator(nbins=8, prune=None).tick_values(bv_min, bv_max)
        lvls = [lv for lv in lvls if bv_min < lv < bv_max]
        if lvls:
            cs = ax.contour(x, y, b2v, levels=lvls,
                            colors="steelblue", linewidths=1.2, linestyles="--")
            ax.clabel(cs, fmt=lambda v: f"{v:.3g}", fontsize=8,
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
        Line2D([0], [0], color="steelblue",   lw=1.2, ls="--", label="B²V  (T²·m³)"),
        Line2D([0], [0], color="crimson",     lw=1.2, ls="-.", label="Hoop stress  (MPa)"),
    ]
    ax.legend(handles=handles, fontsize=10, loc="upper left",
              framealpha=0.85, edgecolor="grey")
    ax.set_title(f"Combined map — B₀ / B²V / Hoop stress  "
                 f"[self-consistent Je]\n{suffix}", fontsize=10, pad=52)
    ph.set_mm_axes(ax)
    ph.style_axes(ax)

    _add_ref_magnets(ax, show_je=True, show_h=True)

    _save(fig, "contour_combined_b2v_contour.png")


# ── Reference magnets ─────────────────────────────────────────────────────────
_REF_MAGNETS = [
    {
        "label":  "UHF Solenoid\n(Muon Collider, CERN)",
        "ri_mm":  30.0,
        "th_mm":  60.0,
        "b0":     40.0,
        "je":     632.0,
        "h_mm":   700.0,
        "marker": "D",
        "color":  "lime",
        "zorder": 10,
    },
    {
        "label":  "ASIPP/Tsinghua\n32.4 T",
        "ri_mm":  8.5,
        "th_mm":  37.7,
        "b0":     32.4,
        "je":     None,
        "h_mm":   None,
        "marker": "^",
        "color":  "cyan",
        "zorder": 10,
    },
    #{
    #    "label":  "DMRadio Target\n16 T, 10 m³\n6.2 yr scan",
    #    "ri_mm":  1261.0,
    #    "th_mm":  160.0,
    #    "b0":     16.0,
    #    "je":     600.0,
    #    "h_mm":   2000.0,
    #    "marker": "*",
    #    "color":  "gold",
    #    "zorder": 10,
    #},
]

def _add_ref_magnets(ax, show_je=False, show_h=False):
    for m in _REF_MAGNETS:
        ri = m["ri_mm"]
        th = m["th_mm"]
        ax.scatter(ri, th,
                   marker=m["marker"], s=120,
                   color=m["color"], edgecolors="black", linewidths=0.8,
                   zorder=m["zorder"])
        lines = [m["label"], f"B₀ = {m['b0']:.1f} T"]
        if show_je and m["je"] is not None:
            lines.append(f"Je = {m['je']:.0f} A/mm²")
        if show_h and m["h_mm"] is not None:
            lines.append(f"H = {m['h_mm']:.0f} mm")
        ax.annotate(
            "\n".join(lines),
            xy=(ri, th),
            xytext=(ri + 4, th + 4),
            fontsize=7,
            color="white",
            bbox=dict(boxstyle="round,pad=0.3",
                      fc="black", ec=m["color"], alpha=0.75, lw=0.8),
            arrowprops=dict(arrowstyle="-", color=m["color"], lw=0.8),
            zorder=m["zorder"] + 1,
        )