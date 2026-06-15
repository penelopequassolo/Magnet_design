# plot_helpers.py
# ─────────────────────────────────────────────────────────────────────────────
# Reusable styling and contour-plotting utilities.
# ─────────────────────────────────────────────────────────────────────────────
import numpy as np
import matplotlib.ticker as ticker

import config


def style_axes(ax):
    ax.tick_params(which="both", top=True, bottom=True, left=True, right=True,
                   direction="in", labelsize=10)
    for spine in ax.spines.values():
        spine.set_linewidth(1.2)


def style_axes_grid(ax):
    style_axes(ax)
    ax.minorticks_on()
    ax.set_axisbelow(True)
    ax.grid(which="major", color="grey", lw=0.6, alpha=0.5, zorder=0)
    ax.grid(which="minor", color="grey", lw=0.3, alpha=0.3, zorder=0)


def set_mm_axes(ax):
    ax.set_xlabel("Inner radius  Ri  (mm)", fontsize=12)
    ax.set_ylabel("Coil thickness  Th  (mm)", fontsize=12)
    ax.xaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))
    ax.yaxis.set_major_formatter(ticker.FormatStrFormatter("%.0f"))


def safe_levels(vmin, vmax, step=None, n=20):
    """Always return at least 2 monotonically increasing levels."""
    if step is not None:
        levels = np.arange(np.floor(vmin / step) * step, vmax + step, step)
        levels = levels[(levels >= vmin) & (levels <= vmax + step)]
        if len(levels) >= 2:
            return levels
    if np.isclose(vmin, vmax):
        span = max(1.0, 0.01 * abs(vmin), 1e-6)
        return np.array([vmin - span, vmax + span])
    return np.linspace(vmin, vmax, n)


def add_b0_contours(ax, x_data, y_data, b0_grid, label_color="white"):
    levels = [lv for lv in config.B0_ISO_LEVELS
              if np.nanmin(b0_grid) <= lv <= np.nanmax(b0_grid)]
    if not levels:
        return
    cs = ax.contour(x_data, y_data, b0_grid, levels=levels, colors=label_color,
                    linewidths=config.B0_ISO_LW, linestyles=config.B0_ISO_LS)
    ax.clabel(cs, fmt=lambda v: f"{v:.0f} T", fontsize=9,
              inline=True, inline_spacing=4, colors=label_color)


def make_contour_plot(fig, ax, x_data, y_data, z, levels, cmap,
                      label_fmt, cbar_label, title,
                      line_color="k", line_width=0.8, label_fontsize=8):
    cf = ax.contourf(x_data, y_data, z, levels=levels, cmap=cmap)
    cs = ax.contour(x_data, y_data, z, levels=levels,
                    colors=line_color, linewidths=line_width, alpha=0.9)
    ax.clabel(cs, fmt=label_fmt, fontsize=label_fontsize,
              inline=True, colors=line_color)
    cbar = fig.colorbar(cf, ax=ax, orientation="horizontal", location="top",
                        pad=0.02, aspect=40, shrink=0.95)
    cbar.set_label(cbar_label, fontsize=10, labelpad=4)
    cbar.ax.tick_params(labelsize=9, direction="in")
    ax.set_title(title, fontsize=10, pad=52)
    style_axes(ax)
    return cf, cs


def title_suffix_th():
    from solenoid_lib import solenoid_length, margin
    return (f"L = {solenoid_length*1e3:.0f} mm (fixed)  |  "
            f"Je = self-consistent (scaling law)  |  "
            f"margin = {margin*100:.0f}%  |  "
            f"Ri : 10 → 1000 mm  |  Th : 10 → 100 mm")


def title_suffix_area():
    from solenoid_lib import solenoid_length, margin
    return (f"L = {solenoid_length*1e3:.0f} mm (fixed)  |  "
            f"Je = self-consistent (scaling law)  |  "
            f"margin = {margin*100:.0f}%  |  "
            f"Ri : 10 → 1000 mm  |  A : 0 → 700000 mm²")