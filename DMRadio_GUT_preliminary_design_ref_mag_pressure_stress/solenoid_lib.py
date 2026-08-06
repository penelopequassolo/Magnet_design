# solenoid_lib.py
# ─────────────────────────────────────────────────────────────────────────────
# Physics library for REBCO solenoid sizing.
#
# Contains:
#   1. Ic_tape            — empirical critical current scaling law  [A per unit width]
#   2. solenoid_field     — on-axis field calculation               [T]
#   3. magnetic_energy    — stored magnetic energy                  [J]
#   4. hoop_stress        — hoop stress in the winding pack         [Pa]
# ─────────────────────────────────────────────────────────────────────────────

import numpy as np
from typing import Tuple

# ─────────────────────────────────────────────────────────────────────────────
# Physical constants
# ─────────────────────────────────────────────────────────────────────────────
mu0              = 4 * np.pi * 1e-7   # [H/m]
geometric_factor = 0.2235

# ─────────────────────────────────────────────────────────────────────────────
# Fixed solenoid parameters
# ─────────────────────────────────────────────────────────────────────────────
solenoid_length = None   # [m]  (set by the calling script)
je_nominal      = 630e6    # [A/m²]  tape engineering Je (630 A/mm²)

# ─────────────────────────────────────────────────────────────────────────────
# REBCO tape geometry
# ─────────────────────────────────────────────────────────────────────────────
t_tape_mm = 0.15     # Total tape thickness [mm]
t_sc_mm   = 0.002   # SC layer thickness   [mm]  (2 µm)
w_tape_mm = 4.0     # Tape width           [mm]


margin      = (1-0.4)     # [—]      margin applied to obtain the operating current density Je_op from the engineering current density Je
# ─────────────────────────────────────────────────────────────────────────────
# Field angle
# ─────────────────────────────────────────────────────────────────────────────
theta_solenoid = np.pi / 2   # B // ab-plane — worst case for a solenoid

# ─────────────────────────────────────────────────────────────────────────────
# Scan time parameters
# ─────────────────────────────────────────────────────────────────────────────
SCAN_MODEL  = 'DFSZ'
SCAN_SNR    = 3.0
SCAN_C_PU   = 0.1
SCAN_Q      = 20e6
SCAN_ETA_A  = 0.1
SCAN_T      = 10e-3
SCAN_RHO_DM = 0.45
SCAN_G_AYY  = 1e-19
SCAN_NU_MIN = 0.1e6
SCAN_NU_MAX = 30e6

# ─────────────────────────────────────────────────────────────────────────────
# 1.  Ic_tape  —  empirical critical current scaling law
# ─────────────────────────────────────────────────────────────────────────────

def Ic_tape(b: float, theta: float) -> float:
    """
    Critical current per unit tape width  [A].

    Parameters
    ----------
    b     : field magnitude [T]  — float or np.ndarray
    theta : field angle [rad] w.r.t. c-axis
            0    = B // c-axis
            pi/2 = B // ab-plane  (solenoid worst case)

    Returns
    -------
    Ic per unit tape width  [A]
    """
    k0     = 8870.0
    k1     = 18500.0
    alpha0 = 1.3
    alpha1 = 0.809
    beta0  = 13.8
    beta1  = 13.8
    phi1   = -0.180 * np.pi / 180.0
    c1     = 2.15

    omega = c1 * (b + (1.0 / c1)**(5.0 / 3.0))**(3.0 / 5.0)

    return (
        k0 / (b + beta0)**alpha0
        + k1 / (b + beta1)**alpha1
        * (omega**2 * np.cos(theta - phi1)**2
           + np.sin(theta - phi1)**2)**(-0.5)
    )


def Je_tape(b: float, theta: float) -> float:
    """
    Engineering current density [A/mm²] at field b and angle theta,
    derated by 90% fill factor.

    Parameters
    ----------i want a table
    b     : field magnitude [T]
    theta : field angle [rad] w.r.t. c-axis

    Returns
    -------
    je : engineering current density [A/mm²]
    """
    ic          = Ic_tape(b, theta)          # [A]      for tape of width w_tape_mm
    ic_w        = ic / w_tape_mm             # [A/mm]   per mm of tape width
    jc_tape_mm2 = ic_w / t_tape_mm         # [A/mm²]  over full tape cross-section
    je_max       = jc_tape_mm2 * (margin)   # [A/mm²]  derated by margin to get operating Je

    return jc_tape_mm2, je_max

def je_max_stress_limited(ri: float, rf: float,
                           l: float = solenoid_length,
                           sigma_limit: float = 500e6) -> float:
    """
    Maximum engineering current density [A/m²] such that the hoop stress
    does not exceed sigma_limit [Pa].

    Since B0 ∝ Je  and  σ = (B0² / 2μ0) * (ri / th),
    we have σ ∝ Je², so:

        Je_max_mech = Je_ref * sqrt(sigma_limit / sigma_ref)

    solved via a single reference evaluation.

    Parameters
    ----------
    ri          : inner radius [m]
    rf          : outer radius [m]
    l           : solenoid length [m]
    sigma_limit : hoop stress limit [Pa]  (default 500 MPa)

    Returns
    -------
    je_lim : stress-limited Je [A/m²]
    """
    # Use a reference Je to get the stress scaling
    je_ref    = 1e8          # [A/m²]  arbitrary reference (100 A/mm²)
    sigma_ref, _ = hoop_stress(ri, rf, je_ref, l)

    # σ ∝ Je²  →  Je_lim = Je_ref * sqrt(sigma_limit / sigma_ref)
    je_lim = je_ref * np.sqrt(sigma_limit / sigma_ref)

    return je_lim   # [A/m²]

# ─────────────────────────────────────────────────────────────────────────────
# 2.  Solenoid field
# ─────────────────────────────────────────────────────────────────────────────

def solenoid_field_center(ri: float, rf: float,
                          j: float,
                          l: float = solenoid_length) -> float:
    """
    Central axial field  [T] — exact analytical integral of Biot-Savart
    for a finite-length thick-walled solenoid.

    Parameters
    ----------
    ri : inner radius [m]
    rf : outer radius [m]
    j  : winding engineering current density Je [A/m²]
    l  : solenoid length [m]

    Returns
    -------
    b0 : central field [T]
    """
    if ri >= rf:
        raise ValueError("ri must be < rf.")
    if l <= 0:
        raise ValueError("l must be positive.")
    if j == 0:
        raise ValueError("j must be non-zero.")

    zp =  l / 2.0
    zm = -l / 2.0

    def log_term(z):
        return z * np.log(
            (np.sqrt(rf**2 + z**2) + rf) /
            (np.sqrt(ri**2 + z**2) + ri)
        )

    return (mu0 * j / 2.0) * (log_term(zp) - log_term(zm))


def solenoid_field_profile(z: np.ndarray,
                           ri: float, rf: float,
                           j: float,
                           l: float = solenoid_length) -> np.ndarray:
    """
    On-axis field profile  [T]  at positions z along the bore axis.

    Parameters
    ----------
    z  : axial positions [m]  (array)
    ri : inner radius [m]
    rf : outer radius [m]
    j  : winding engineering current density Je [A/m²]
    l  : solenoid length [m]

    Returns
    -------
    bz : field profile [T]  (same shape as z)
    """
    if ri >= rf or l <= 0 or j == 0:
        raise ValueError("Invalid parameters.")

    zp = z + l / 2.0
    zm = z - l / 2.0

    t1 = zp * np.log((np.sqrt(rf**2 + zp**2) + rf) /
                     (np.sqrt(ri**2 + zp**2) + ri))
    t2 = zm * np.log((np.sqrt(rf**2 + zm**2) + rf) /
                     (np.sqrt(ri**2 + zm**2) + ri))

    return (mu0 * j / 2.0) * (t1 - t2)


# ─────────────────────────────────────────────────────────────────────────────
# 3.  Magnetic stored energy
# ─────────────────────────────────────────────────────────────────────────────

def magnetic_energy(ri: float, rf: float,
                    j: float,
                    l: float = solenoid_length) -> float:
    """
    Stored magnetic energy  [J].

    Uses the Lorenz / Lontai semi-analytic approximation for a
    thick-walled solenoid.

    Parameters
    ----------
    ri : inner radius [m]
    rf : outer radius [m]
    j  : winding engineering current density Je [A/m²]
    l  : solenoid length [m]

    Returns
    -------
    em : stored energy [J]
    """
    if ri >= rf or l <= 0 or j == 0:
        raise ValueError("Invalid parameters.")

    th  = rf - ri
    rm  = ri + th / 2.0
    eta = geometric_factor * (l + th)
    lnt = np.log(8.0 * rm / eta)
    cor = 1.0 + 3.0 * eta**2 / (16.0 * rm**2)

    return (j**2 * mu0 * rm * (l * th)**2 / 2.0) * (
        lnt * cor - (2.0 + eta**2 / (16.0 * rm**2))
    )


# ─────────────────────────────────────────────────────────────────────────────
# 4.  Hoop stress
# ─────────────────────────────────────────────────────────────────────────────

def hoop_stress(ri: float, rf: float,
                j: float,
                l: float = solenoid_length) -> Tuple[float, float]:
    """
    Peak hoop (circumferential) stress  [Pa]  and central field  [T]
    using the magnetic-pressure thin-shell approximation.

        sigma_hoop = (b0² / 2µ0) × (ri / th)

    Parameters
    ----------
    ri : inner radius [m]
    rf : outer radius [m]
    j  : winding engineering current density Je [A/m²]
    l  : solenoid length [m]

    Returns
    -------
    sigma : hoop stress [Pa]
    b0    : central field [T]
    """
    if ri >= rf or l <= 0 or j == 0:
        raise ValueError("Invalid parameters.")
    th = rf - ri
    if th <= 0:
        raise ValueError("th must be positive.")

    b0    = solenoid_field_center(ri, rf, j, l)
    sigma = (b0**2 / (2.0 * mu0)) * (ri / th)

    return sigma, b0


# ─────────────────────────────────────────────────────────────────────────────
# solenoid_summary
# ─────────────────────────────────────────────────────────────────────────────

def solenoid_summary(ri: float, rf: float,
                     j: float = je_nominal,
                     l: float = solenoid_length,
                     theta: float = theta_solenoid) -> dict:
    """
    Collect all key solenoid outputs at a fixed engineering Je = j.

    Returns
    -------
    dict with geometry, field, energy, stress, and Ic/Jc reference values.
    """
    b0            = solenoid_field_center(ri, rf, j, l)
    em            = magnetic_energy(ri, rf, j, l)
    sigma, _      = hoop_stress(ri, rf, j, l)
    th            = rf - ri
    v_bore        = np.pi * ri**2 * l

    # Ic and Je at operating field
    ic_op  = Ic_tape(b0, theta)              # [A]     for tape of width w_tape_mm
    ic_abs = ic_op * (w_tape_mm * 1e-3)      # [A]     for one tape (SI width)
    je_op  = Je_tape(b0, theta)              # [A/mm²] engineering Je, 90% derated

    return {
        # Geometry
        "ri"             : ri,
        "rf"             : rf,
        "th"             : th,
        "l"              : l,
        "v_bore"         : v_bore,
        # Operating current density
        "je"             : j,
        "fill_factor"    : (t_tape_mm * 1e-3) / (t_tape_mm * 1e-3),  # placeholder λ
        # Field
        "b0"             : b0,
        # Energy
        "em"             : em,
        # Stress
        "sigma_hoop"     : sigma,
        "sigma_hoop_mpa" : sigma / 1e6,
        # Ic / Je reference at b0
        "ic_op"          : ic_op,            # [A]     for w_tape_mm wide tape
        "ic_op_abs"      : ic_abs,           # [A]     SI-width scaled
        "je_op"          : je_op,            # [A/mm²] engineering Je at b0
        "theta"          : theta,
    }
# ─────────────────────────────────────────────────────────────────────────────
# Axion model parameters
# ─────────────────────────────────────────────────────────────────────────────

KSVZ = 1.92
DFSZ = 0.75

s_per_year = 365.25 * 24 * 3600   # [s/yr]

def g_axion_photon(C_ag: float, m_a_eV: float) -> float:
    """
    Axion–photon coupling  [GeV⁻¹].

    Parameters
    ----------
    C_ag   : model coefficient  (DFSZ = 0.75, KSVZ = 1.92)
    m_a_eV : axion mass [eV]
    """
    return 2e-10 * C_ag * m_a_eV


def dB_to_eta(dB: float) -> float:
    """Convert dB backaction reduction to linear amplitude efficiency η_A."""
    return 10 ** (dB / 20.0)

# ─────────────────────────────────────────────────────────────────────────────
# Scan time
# ─────────────────────────────────────────────────────────────────────────────

def scan_time(b0: float,
              v: float,
              model:   str   = SCAN_MODEL,
              SNR:     float = SCAN_SNR,
              c_PU:    float = SCAN_C_PU,
              Q:       float = SCAN_Q,
              eta_A:   float = SCAN_ETA_A,
              T:       float = SCAN_T,
              rho_DM:  float = SCAN_RHO_DM,
              g_ayy:   float = SCAN_G_AYY,
              nu_min:  float = SCAN_NU_MIN,
              nu_max:  float = SCAN_NU_MAX) -> float:
    """
    Total scan time  [years]  to cover the frequency band [nu_min, nu_max].

    Parameters
    ----------
    B0      : central magnetic field        [T]
    V       : cavity volume                 [m³]
    model   : 'DFSZ', 'KSVZ', or None
              If None, g_ayy is used directly.
    SNR     : signal-to-noise threshold     [—]
    c_PU    : pick-up coupling coefficient  [—]
    Q       : cavity quality factor         [—]
    eta_A   : backaction amplitude efficiency η_A  [—]
    T       : system noise temperature      [K]
    rho_DM  : local dark-matter density     [GeV cm⁻³]
    g_ayy   : fixed coupling (model=None)   [GeV⁻¹]
    nu_min  : lower frequency bound         [Hz]
    nu_max  : upper frequency bound         [Hz]

    Returns
    -------
    t_scan : total scan time [years]
    """
    from scipy import integrate
    from astropy import constants

    C_ag = DFSZ if model == 'DFSZ' else KSVZ

    prefactor = (np.pi*(6.4e5)/ (6*np.sqrt(3)*constants.k_B.to('J/K').value)
                 * (SNR             ) ** -2
                 * (rho_DM          ) **  2
                 * (c_PU            ) **  4
                 * (b0              ) **  4
                 * (v               ) ** (10 / 3)
                 * (Q               )
                 * (1      / T      )
                 * (1.     / eta_A  ))

    def integrand(nu: float) -> float:
        if model is None:
            g = g_ayy
        else:
            m_a_eV = constants.h.to('eV/Hz').value * nu
            g      = g_axion_photon(C_ag, m_a_eV)

        dnu_dt = prefactor * (g ) ** 4 * (nu ) * 1e27
        return 1.0 / dnu_dt

    result, _ = integrate.quad(integrand, nu_min, nu_max)
    return result/ s_per_year



# # ─────────────────────────────────────────────────────────────────────────────
# # Scan time
# # ─────────────────────────────────────────────────────────────────────────────

# def scan_time(b0: float,
#               v: float,
#               model:   str   = SCAN_MODEL,
#               SNR:     float = SCAN_SNR,
#               c_PU:    float = SCAN_C_PU,
#               Q:       float = SCAN_Q,
#               eta_A:   float = SCAN_ETA_A,
#               T:       float = SCAN_T,
#               rho_DM:  float = SCAN_RHO_DM,
#               g_ayy:   float = SCAN_G_AYY,
#               nu_min:  float = SCAN_NU_MIN,
#               nu_max:  float = SCAN_NU_MAX) -> float:
#     """
#     Total scan time  [years]  to cover the frequency band [nu_min, nu_max].

#     Parameters
#     ----------
#     B0      : central magnetic field        [T]
#     V       : cavity volume                 [m³]
#     model   : 'DFSZ', 'KSVZ', or None
#               If None, g_ayy is used directly.
#     SNR     : signal-to-noise threshold     [—]
#     c_PU    : pick-up coupling coefficient  [—]
#     Q       : cavity quality factor         [—]
#     eta_A   : backaction amplitude efficiency η_A  [—]
#     T       : system noise temperature      [K]
#     rho_DM  : local dark-matter density     [GeV cm⁻³]
#     g_ayy   : fixed coupling (model=None)   [GeV⁻¹]
#     nu_min  : lower frequency bound         [Hz]
#     nu_max  : upper frequency bound         [Hz]

#     Returns
#     -------
#     t_scan : total scan time [years]
#     """
#     from scipy import integrate
#     from astropy import constants

#     C_ag = DFSZ if model == 'DFSZ' else KSVZ

#     prefactor = (41e3 / s_per_year
#                  * (SNR    / 3      ) ** -2
#                  * (rho_DM / 0.45   ) **  2
#                  * (c_PU   / 0.1    ) **  4
#                  * (b0     / 16     ) **  4
#                  * (v      / 10     ) ** (10 / 3)
#                  * (Q      / 2e7    )
#                  * (10e-3  / T      )
#                  * (0.1    / eta_A  ))

#     def integrand(nu: float) -> float:
#         if model is None:
#             g = g_ayy
#         else:
#             m_a_eV = constants.h.to('eV/Hz').value * nu
#             g      = g_axion_photon(C_ag, m_a_eV)

#         dnu_dt = prefactor * (g / 1e-19) ** 4 * (nu / 100e3)
#         return 1.0 / dnu_dt

#     result, _ = integrate.quad(integrand, nu_min, nu_max)
#     return result / s_per_year

# # ─────────────────────────────────────────────────────────────────────────────
# # Scan time baseline
# # ─────────────────────────────────────────────────────────────────────────────
# SCAN_BASE_FIELD_T  = 18.0   # [T]   reference field
# SCAN_BASE_TIME_YR  = 2.24   # [yr]  scan time at reference field

# # ─────────────────────────────────────────────────────────────────────────────
# # 5.  Scan time  —  empirical B^4 scaling
# # ─────────────────────────────────────────────────────────────────────────────

# def scan_time(field_T: float,
#               base_field: float = SCAN_BASE_FIELD_T,
#               base_time_yr: float = SCAN_BASE_TIME_YR
#               ) -> float:
#     """
#     Total scan time [years] to cover the target frequency band.

#     Scan time scales inversely with the fourth power of the field:

#         t(B) = t_ref × (B_ref / B)^4

#     Anchored to the empirical baseline:
#         B_ref = 18 T  →  t_ref = 2.24 yr

#     Parameters
#     ----------
#     field_T      : operating magnetic field [T]
#     base_field   : reference field          [T]   (default 18 T)
#     base_time_yr : scan time at base_field  [yr]  (default 2.24 yr)

#     Returns
#     -------
#     t_scan : total scan time [years]
#     """
#     return base_time_yr * (base_field / field_T) ** 4