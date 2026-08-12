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

import math

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
solenoid_length   = None     # [m]  default solenoid length
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


def Je_tape(b: float, theta: float) -> tuple[float, float]:
    """
    Engineering current density [A/mm²] at field b and angle theta,
    derated by margin.

    Parameters
    ----------
    b       : field magnitude [T]
    theta   : field angle [rad] w.r.t. c-axis

    Returns
    -------
    jc_tape_mm2 : critical current density [A/mm²]
    je_max      : engineering current density [A/mm²]
    """
    
    ic          = Ic_tape(b, theta)                   # [A]      for tape of width w_tape_mm
    ic_w        = ic / w_tape_mm                      # [A/mm]   per mm of tape width
    jc_tape_mm2 = (ic_w / t_tape_mm)     # [A/mm²]  over full tape cross-section
    je_max      = jc_tape_mm2 * margin                # [A/mm²]  derated by margin

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

#  Peak conductor field


P0 = {0: 1.0, 2: -1/2, 4: 3/8, 6: -5/16, 8: 35/128, 10: -63/256}   # P_n(0)


def _br(g, a, b):
    """[ g ]_{r=1}^{r=alpha}"""
    return g(a, b) - g(1.0, b)


def F(a, b):
    return b * math.log((a + math.hypot(a, b)) / (1.0 + math.hypot(1.0, b)))


def FE2(a, b):
    g = lambda r, b: r**3 / (r*r + b*b)**1.5
    return -_br(g, a, b) / (2 * b)


def FE4(a, b):
    g = lambda r, b: r**3*(2*r**4 + 7*r**2*b**2 + 20*b**4) / (r*r + b*b)**3.5
    return -_br(g, a, b) / (24 * b**3)


def FE6(a, b):
    g = lambda r, b: r**3*(8*r**8 + 44*r**6*b**2 + 99*r**4*b**4
                           + 28*r**2*b**6 + 280*b**8) / (r*r + b*b)**5.5
    return -_br(g, a, b) / (240 * b**5)


def FE8(a, b):
    g = lambda r, b: r**3*(16*r**12 + 120*r**10*b**2 + 390*r**8*b**4 + 715*r**6*b**6
                           + 1080*r**4*b**8 - 1008*r**2*b**10
                           + 1344*b**12) / (r*r + b*b)**7.5
    return -_br(g, a, b) / (896 * b**7)


def FE10(a, b):
    g = lambda r, b: r**3*(128*r**16 + 1216*r**14*b**2 + 5168*r**12*b**4
                           + 12920*r**10*b**6 + 20995*r**8*b**8 + 19976*r**6*b**10
                           + 49632*r**4*b**12 - 46464*r**2*b**14
                           + 21120*b**16) / (r*r + b*b)**9.5
    return -_br(g, a, b) / (11520 * b**9)


TERMS = {0: F, 2: FE2, 4: FE4, 6: FE6, 8: FE8, 10: FE10}



def b_peak(ri: float, rf: float,
           j: float,
           l: float = None,
           nmax: int = 10,
           warn: bool = True) -> float:
    """
    Peak conductor field at (r, z) = (ri, 0)  [T].

    Legendre expansion of the thick-walled solenoid field evaluated on the
    bore surface (xi = 1), in the dimensionless variables

        alpha = rf / ri        (radial build ratio)
        beta  = l / (2 ri)     (aspect ratio)

    Parameters
    ----------
    ri, rf : inner / outer radius [m]
    j      : winding engineering current density Je [A/m²]
    l      : solenoid length [m]
    nmax   : highest expansion order retained (0, 2, ... 10)
    warn   : flag slow convergence of the truncated series

    Returns
    -------
    b1 : peak conductor field [T]
    """
    if l is None:
        l = solenoid_length
    if ri <= 0:
        raise ValueError("ri must be positive.")
    if ri >= rf:
        raise ValueError("ri must be < rf.")
    if l <= 0:
        raise ValueError("l must be positive.")

    alpha = rf / ri
    beta  = 0.5 * l / ri

    terms = [P0[n] * TERMS[n](alpha, beta) for n in sorted(TERMS) if n <= nmax]
    s = math.fsum(terms)


    return mu0 * j * ri * s
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


def wilson_hoop(ri: float, rf: float,
                j: float,
                l: float = None,
                nu: float = 0.3,
                kappa: float = 0,
                npts: int = 201,
                b1: float = None) -> Tuple[float, float, float]:
    """
    Peak Wilson hoop stress across the winding pack  [Pa].

        sigma(p) = S * (C0 + C2/p^2 + C1 p + C3 p^2),   p = r / ri in [1, alpha]
        S        = j B1 ri / (alpha - 1)

    The coefficients carry the Poisson ratio nu and the field-decay parameter
    kappa = B(rf)/B(ri); kappa = 0 assumes the field falls to zero at the OD.

    Returns
    -------
    sigma : peak hoop stress [Pa]
    p_max : radius of the peak, in units of ri [—]
    b1    : peak conductor field used [T]
    """
    if l is None:
        l = solenoid_length
    if ri <= 0 or ri >= rf or l <= 0:
        raise ValueError("Invalid geometry.")
    if j == 0:
        raise ValueError("j must be non-zero.")

    alpha = rf / ri
    if alpha - 1.0 < 1e-12:
        raise ValueError("winding is degenerate (alpha -> 1).")

    if b1 is None:
        b1 = b_peak(ri, rf, j, l)

    S = j * b1 * ri / (alpha - 1.0)

    kA = (2 + nu) / 3 * (alpha - kappa)
    kB = (3 + nu) / 8 * (1 - kappa)

    C0 = kA * (alpha**2 + alpha + 1) / (alpha + 1) - kB * (alpha**2 + 1)
    C2 = alpha**2 * (kA / (alpha + 1) - kB)
    C1 = -(1 + 2*nu) / 3 * (alpha - kappa)
    C3 = (1 + 3*nu) / 8 * (1 - kappa)

    p     = np.linspace(1.0, alpha, npts)
    sigma = S * (C0 + C2 / p**2 + C1 * p + C3 * p**2)
    k     = int(np.argmax(sigma))

    return float(sigma[k]), float(p[k]), float(b1)


def hoop_stress(ri: float, rf: float,
                j: float,
                l: float = None,
                nu: float = 0.3,
                kappa: float = 0,
                npts: int = 201) -> Tuple[float, float]:
    """
    Peak Wilson hoop stress [Pa] and central field [T].

    Return convention matches the original v1 hoop_stress — (sigma, b0) —
    so existing callers (grid sweeps, check_stress.py) work unchanged.
    The stress is the thick-wall peak over the radial build, driven by the
    peak conductor field B1; b0 is returned for reporting only.

    The thin-shell magnetic-pressure model is no longer reachable here;
    see hoop_stress_thin_shell() or stress_pair() if you need it.
    """
    if l is None:
        l = solenoid_length
    if ri >= rf or l <= 0 or j == 0:
        raise ValueError("Invalid parameters.")

    b0 = solenoid_field_center(ri, rf, j, l)
    sigma, _, _ = wilson_hoop(ri, rf, j, l, nu, kappa, npts)

    return sigma, b0

# def hoop_stress(ri: float, rf: float,
#                 j: float,
#                 l: float = solenoid_length) -> Tuple[float, float]:
#     """
#     Peak hoop (circumferential) stress  [Pa]  and central field  [T]
#     using the magnetic-pressure thin-shell approximation.

#         sigma_hoop = (b0² / 2µ0) × (ri / th)

#     Parameters
#     ----------
#     ri : inner radius [m]
#     rf : outer radius [m]
#     j  : winding engineering current density Je [A/m²]
#     l  : solenoid length [m]

#     Returns
#     -------
#     sigma : hoop stress [Pa]
#     b0    : central field [T]
#     """
#     if ri >= rf or l <= 0 or j == 0:
#         raise ValueError("Invalid parameters.")
#     th = rf - ri
#     if th <= 0:
#         raise ValueError("th must be positive.")

#     b0    = solenoid_field_center(ri, rf, j, l)
#     sigma = (b0**2 / (2.0 * mu0)) * (ri / th)

#     return sigma, b0


# ─────────────────────────────────────────────────────────────────────────────
# solenoid_summary
# ─────────────────────────────────────────────────────────────────────────────

def solenoid_summary(ri: float, rf: float,
                     j: float = je_nominal,
                     l: float = solenoid_length,
                     theta: float = theta_solenoid,) -> dict:
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
    ic_op  = Ic_tape(b0, theta)              
    ic_abs = ic_op * (w_tape_mm * 1e-3)      
    je_op  = Je_tape(b0, theta) # Pass variable here


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
# 5.  Winding-pack design point — hoop stress + quench protection
# ─────────────────────────────────────────────────────────────────────────────
# The operating point of a fixed pack (Ri, Th, L) is the largest packing
# current density Je that simultaneously satisfies
#
#     tape        : I_turn <= I_c(B0)                (via f_tape <= f_built)
#     mechanics   : sigma_hoop / f_struct <= u_target * sigma_limit
#     packing     : f_tape_built + f_Cu_cowound <= fill_target
#
# Copper carries no load, so protection and mechanics compete for the SAME
# radial build: every % of Th given to stabiliser is a % that cannot react the
# Lorentz load.  Every residual is monotone increasing in Je (the ceil() jumps
# at each new layer raise f_built AND tau, so they also go up), hence the
# feasible set is the interval (0, Je*] and Je* is found by bisection — no
# secant, no relaxation factor, no tolerance tuning.
# ─────────────────────────────────────────────────────────────────────────────

from dataclasses import dataclass, asdict

RHO_CU = 8960.0          # [kg/m3]


@dataclass(frozen=True)
class DesignParams:
    sigma_limit: float = 750e6      # hoop limit of the structure          [Pa]
    u_target:    float = 0.995      # structural utilisation to sit at     [-]
    fill_target: float = 0.98       # tape + co-wound Cu packing ceiling   [-]
    fCu:         float = 0.50       # copper fraction of the tape itself   [-]
    gamma_cu:    float = 5.0e16     # copper action limit at the hot spot  [A2 s m-4]
    sf_quench:   float = 1.00       # safety factor on required Cu area    [-]
    topology:    str   = "isolated"   # "series" | "isolated"
    dump_mode:   str   = "resistance"
    R_EE:        float = 4.0        # dump resistance                      [ohm]

    @property
    def gamma_max(self) -> float:
        """Action limit referred to the copper cross-section [A2 s m-4]."""
        return self.gamma_cu * self.fCu

    def as_dict(self) -> dict:
        d = asdict(self)
        d["gamma_max"] = self.gamma_max
        return d


DEFAULT_DESIGN = DesignParams()

# binding-constraint labels, kept in one place so grids/plots agree
BINDING_CODES = {"mechanical": 0, "packing": 1, "EM/tape": 2, "none": 3}


# ── geometry helpers ─────────────────────────────────────────────────────────
def quantize_length(l_req: float) -> Tuple[float, int]:
    """
    Axial pitch is the tape width, so the length quantises exactly.

    Returns (l_built, n_pancakes).
    """
    w = w_tape_mm * 1e-3
    n_pc = max(1, int(round(l_req / w)))
    return n_pc * w, n_pc


def pack_coefficients(ri: float, rf: float,
                      l: float = None,
                      quantize: bool = True) -> dict:
    """
    Power-law coefficients of the pack, evaluated ONCE per geometry.

        B0    = k_b * Je          (exact, Biot-Savart is linear in Je)
        sigma = k_s * Je^2        (Wilson: S ~ j*B1*ri and B1 ~ j)
        E     = k_e * Je^2        (Lorenz-Lontai)
        E_pc  = k_e_pc * Je^2     (one pancake, length = w_tape)

    Everything downstream is algebra on these four numbers, so the expensive
    library calls happen three times per cell instead of once per iteration.
    """
    if l is None:
        l = solenoid_length
    if l is None:
        raise ValueError("coil length is undefined (solenoid_lib.solenoid_length "
                         "is None and no l was given)")
    if ri >= rf:
        raise ValueError("ri must be < rf.")

    if quantize:
        l, n_pc = quantize_length(l)
    else:
        n_pc = max(1, int(round(l / (w_tape_mm * 1e-3))))

    je_ref = 1e8
    k_b    = solenoid_field_center(ri, rf, je_ref, l) / je_ref
    k_s    = hoop_stress(ri, rf, je_ref, l)[0] / je_ref ** 2
    k_e    = magnetic_energy(ri, rf, je_ref, l) / je_ref ** 2
    k_e_pc = magnetic_energy(ri, rf, je_ref, w_tape_mm * 1e-3) / je_ref ** 2

    th = rf - ri
    return {
        "ri": ri, "rf": rf, "th": th, "l": l, "n_pc": n_pc,
        "a_wind":  th * l,
        "v_build": np.pi * (rf ** 2 - ri ** 2) * l,
        "k_b": k_b, "k_s": k_s, "k_e": k_e, "k_e_pc": k_e_pc,
    }


def je_stress_only(coef: dict, par: DesignParams = DEFAULT_DESIGN,
                   u: float = 1.0) -> float:
    """
    Stress-limited Je with NO copper [A/m2] — identical to
    je_max_stress_limited(), but free because k_s is already known.
    Use u = par.u_target for the derated value.
    """
    return np.sqrt(u * par.sigma_limit / coef["k_s"])


def je_hoop_quench_closed_form(coef: dict, tau_ee: float,
                               par: DesignParams = DEFAULT_DESIGN) -> float:
    """
    Exact hoop+quench limit [A/m2] at a FROZEN turn count, where tau is a
    constant (L = 2E/I0^2 is pure geometry, Je cancels):

        util = k_s Je^2 / ((1 - a Je) u sigma_lim) = 1,   a = sf sqrt(tau/2 gamma_max)
        =>   b Je^2 + a Je - 1 = 0,                       b = k_s / (u sigma_lim)

    a = 0 returns je_stress_only(), which is the sanity check.
    """
    a = par.sf_quench * np.sqrt(0.5 * tau_ee / par.gamma_max)
    b = coef["k_s"] / (par.u_target * par.sigma_limit)
    return (-a + np.sqrt(a * a + 4.0 * b)) / (2.0 * b)


# ── the model: one pure function, everything derived ─────────────────────────
def evaluate_design(coef: dict, je_mm2: float,
                    par: DesignParams = DEFAULT_DESIGN) -> dict:
    """Complete state of the pack at packing current density je_mm2 [A/mm2]."""
    d  = {"je": je_mm2}
    je = je_mm2 * 1e6

    th, l, n_pc = coef["th"], coef["l"], coef["n_pc"]
    a_wind = coef["a_wind"]

    # ── EM ───────────────────────────────────────────────────────────────
    b0       = coef["k_b"] * je
    sigma_pa = coef["k_s"] * je ** 2
    jc_tape_mm2, je_tape_mm2 = Je_tape(b0, theta_solenoid)
    d.update(b0=b0, sigma_pa=sigma_pa, jc_tape=jc_tape_mm2, je_tape=je_tape_mm2)

    if not np.isfinite(je_tape_mm2) or je_tape_mm2 <= 0.0:
        d.update(r=np.inf, binding="EM/tape", feasible=False)
        return d                                    # tape carries nothing here

    # ── winding layout: integer turns, rounded up, pack is never short ───
    t_tape = t_tape_mm * 1e-3
    a_tape = t_tape * (w_tape_mm * 1e-3)
    f_tape  = je_mm2 / je_tape_mm2                  # minimum tape fraction
    n_tp    = max(1, int(np.ceil(f_tape * th / t_tape)))
    n_tot   = n_tp * n_pc
    f_built = n_tot * a_tape / a_wind               # = n_tp * t_tape / th
    pitch   = th / n_tp
    i0      = je * a_wind / n_tot                   # current per tape [A]
    i_max   = je_tape_mm2 * t_tape_mm * w_tape_mm   # tape limit at B0 [A]
    d.update(f_tape=f_tape, n_tp=n_tp, n_tot=n_tot, f_built=f_built,
             pitch=pitch, i0=i0, i_max=i_max, i_margin=i_max / i0,
             len_tape=n_tot * np.pi * (coef["ri"] + coef["rf"]))

    # ── inductance from the stored energy ────────────────────────────────
    em_tot = coef["k_e"]    * je ** 2
    em_iso = coef["k_e_pc"] * je ** 2
    l_tot  = 2.0 * em_tot / i0 ** 2                 # Je cancels: geometry x N^2
    l_iso  = 2.0 * em_iso / i0 ** 2

    # "isolated": one resistor per pancake, the dump sees l_iso only.
    # "series"  : one resistor for the stack, the dump sees l_tot.
    isolated = par.topology == "isolated"
    l_dump, em_dump = (l_iso, em_iso) if isolated else (l_tot, em_tot)

    # ── dump circuit ─────────────────────────────────────────────────────
    r_ee   = par.R_EE
    tau_ee = l_dump / r_ee
    d.update(em_tot=em_tot, em_iso=em_iso, em_dump=em_dump,
             l_tot=l_tot, l_iso=l_iso, l_dump=l_dump,
             r_ee=r_ee, tau_ee=tau_ee, u_ee=r_ee * i0)

    # ── hot spot, adiabatic: exponential dump only ───────────────────────
    # per turn ql = i0^2 tau/2 = E/R.  Smeared over the build j_cu = Je/f_Cu,
    # so the turn count cancels and only tau matters.
    ql_dump   = 0.5 * i0 ** 2 * tau_ee
    with np.errstate(invalid='ignore', divide='ignore'):
        j_cu_max = np.sqrt(par.gamma_max / (0.5 * tau_ee))
    f_cu_req  = par.sf_quench * je / j_cu_max       # total Cu fraction of build
    f_cu_have = f_built * par.fCu                   # Cu already inside the tape
    f_cu_add  = max(f_cu_req - f_cu_have, 0.0)      # co-wound Cu to add
    f_cu_eff  = max(f_cu_req, f_cu_have)            # Cu actually in the build
    s_cu      = f_cu_eff * a_wind / n_tot           # copper area per turn [m2]

    d.update(ql_dump=ql_dump, j_cu_max=j_cu_max, f_cu_req=f_cu_req,
             f_cu_have=f_cu_have, f_cu_add=f_cu_add, f_cu_eff=f_cu_eff,
             s_cu=s_cu, j_cu=je / f_cu_eff,
             gamma_op=(je / f_cu_eff) ** 2 * (0.5 * tau_ee),
             m_cu=RHO_CU * f_cu_eff * coef["v_build"])

    # ── mechanics: copper carries nothing, tape and filler are structure ─
    # Ri, Th, L are fixed, so copper changes neither the Lorentz load nor
    # sigma_hoop.  It changes what is left to carry it: th*(1 - f_Cu).
    f_struct     = 1.0 - f_cu_eff
    sigma_struct = sigma_pa / f_struct if f_struct > 0.0 else np.inf
    fill         = f_built + f_cu_add
    d.update(f_struct=f_struct, sigma_struct=sigma_struct, fill=fill,
             util=sigma_struct / par.sigma_limit)

    # ── binding constraint ───────────────────────────────────────────────
    r_mech = d["util"] / par.u_target
    r_fill = fill / par.fill_target
    d["r"] = max(r_mech, r_fill)
    d["binding"] = ("EM/tape" if f_tape > 1.0 else
                    "mechanical" if r_mech >= r_fill else "packing")
    d["feasible"] = d["r"] <= 1.0
    return d


def solve_design(ri: float = None, rf: float = None, l: float = None,
                 par: DesignParams = DEFAULT_DESIGN,
                 coef: dict = None,
                 rtol: float = 1e-12,
                 max_halve: int = 200):
    """
    Largest feasible Je, i.e. the highest field this pack can hold while
    staying below the hoop limit AND remaining protectable.

    Returns the full design dict (feasible, with 'coef', 'n_eval', 'je_seed',
    'je_closed_form' attached) or None if no Je is feasible.
    """
    if coef is None:
        coef = pack_coefficients(ri, rf, l)

    n_ev = 0

    def probe(x):
        nonlocal n_ev
        n_ev += 1
        return evaluate_design(coef, x, par)

    # upper bound: all of Th as structure, zero copper -> infeasible by
    # construction, since any f_Cu > 0 pushes the utilisation past 1.
    je_hi = je_stress_only(coef, par, u=1.0) / 1e6
    d_hi  = probe(je_hi)
    for _ in range(40):                       # guard: only if quench is free
        if not d_hi["feasible"]:
            break
        je_hi *= 2.0
        d_hi = probe(je_hi)

    # lower bound: halve until it fits
    je_lo, d_lo = None, None
    je = je_hi
    for _ in range(max_halve):
        je *= 0.5
        d = probe(je)
        if d["feasible"]:
            je_lo, d_lo = je, d
            break
        je_hi = je
    if d_lo is None:
        return None                           # infeasible for every Je

    # bisection on a monotone predicate: ~50 evaluations, cannot fail
    while je_hi - je_lo > rtol * je_lo:
        je_mid = 0.5 * (je_lo + je_hi)
        d = probe(je_mid)
        if d["feasible"]:
            je_lo, d_lo = je_mid, d
        else:
            je_hi = je_mid

    d_lo["coef"]   = coef
    d_lo["n_eval"] = n_ev
    d_lo["je_seed"] = je_stress_only(coef, par, u=1.0) / 1e6      # no copper
    d_lo["b0_seed"] = coef["k_b"] * d_lo["je_seed"] * 1e6
    d_lo["je_closed_form"] = je_hoop_quench_closed_form(
        coef, d_lo["tau_ee"], par) / 1e6      # exact when mechanics binds
    return d_lo






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