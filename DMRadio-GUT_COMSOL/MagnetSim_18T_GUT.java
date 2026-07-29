/*
 * MagnetSim_18T_GUT.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Jul 28 2026, 15:43 by COMSOL 6.3.0.420. */
public class MagnetSim_18T_GUT {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("/home/penelope_quassolo/Downloads/DMRadio-CoreGUT-20260727T171715Z-1-001/DMRadio-CoreGUT");

    model.component().create("comp1", true);

    model.component("comp1").geom().create("geom1", 2);
    model.component("comp1").geom("geom1").axisymmetric(true);

    model.component("comp1").mesh().create("mesh1");

    model.component("comp1").physics().create("emw", "ElectromagneticWaves", "geom1");

    model.study().create("std1");
    model.study("std1").create("freq", "Frequency");
    model.study("std1").feature("freq").set("solnum", "auto");
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("ngen", "5");
    model.study("std1").feature("freq").activate("emw", true);

    model.component("comp1").geom("geom1").create("pc1", "ParametricCurve");
    model.component("comp1").geom("geom1").feature().remove("pc1");
    model.component("comp1").geom("geom1").create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "0.00", 0, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.65, 0, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0.085, 1, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.65, 1, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0.085, 2, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.6, 2, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "(a+Th)*radscaling", 3, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "(H-Th)", 3, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "(a+Th)*radscaling", 4, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "+Th+(1-vertscaling)*H", 4, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "(b-Th)*radscaling", 5, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "+Th+(1-vertscaling)*H", 5, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "(b-Th)*radscaling", 6, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "H-Th", 6, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0.16, 7, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.6, 7, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0.16, 8, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.65, 8, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0.16, 9, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.9, 9, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 0, 10, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", 1.9, 10, 1);

    model.param().set("b", "1.26[m]/2");
    model.param().descr("b", "");
    model.param().set("a", "0.336[m]");
    model.param().descr("a", "");
    model.param().set("H", "1.4[m]");
    model.param().descr("H", "");
    model.param().set("Th", "0.000");
    model.param().descr("Th", "");
    model.param().set("TurtW", "0.23 [m]");
    model.param().descr("TurtW", "");
    model.param().set("TurtOutH", "0.184 [m]");
    model.param().descr("TurtOutH", "");
    model.param().set("TurtInH", "0.17 [m]");
    model.param().descr("TurtInH", "");
    model.param().set("NeckH", "0.10 [m]");
    model.param().descr("NeckH", "");
    model.param().set("LipOut", "0.13[m]");
    model.param().descr("LipOut", "");
    model.param().set("LipIn", "0.05 [m]");
    model.param().descr("LipIn", "");
    model.param().set("LipH", "0.254[m]/2");
    model.param().descr("LipH", "");
    model.param().set("TopH", "0.6[m]");
    model.param().descr("TopH", "");
    model.param().set("TopW", "0.15 [m]");
    model.param().descr("TopW", "");
    model.param().set("MidH", "0.43 [m]");
    model.param().descr("MidH", "");
    model.param().set("Move", "0.5");
    model.param().descr("Move", "");
    model.param().set("radscaling", "1");
    model.param().descr("radscaling", "");
    model.param().set("vertscaling", "1");
    model.param().descr("vertscaling", "");
    model.param().set("freqscaling_up", "(1/vertscaling)^(1/2)");
    model.param().descr("freqscaling_up", "");

    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").run("pol1");
    model.component("comp1").geom("geom1").create("ls1", "LineSegment");
    model.component("comp1").geom("geom1").feature("ls1").selection("vertex1").set("pol1", 4);
    model.component("comp1").geom("geom1").feature("ls1").selection("vertex2").set("pol1", 6);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("Cp", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("rho", "Analytic");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("k", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("cs", "Analytic");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("an1", "Analytic");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("an2", "Analytic");
    model.component("comp1").material("mat1").propertyGroup().create("RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat1").propertyGroup().create("NonlinearModel", "Nonlinear model");
    model.component("comp1").material("mat1").label("Air");
    model.component("comp1").material("mat1").set("family", "air");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("argunit", "K");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("fununit", "Pa*s");
    model.component("comp1").material("mat1").propertyGroup("def").func("Cp").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("Cp").set("argunit", "K");
    model.component("comp1").material("mat1").propertyGroup("def").func("Cp").set("fununit", "J/(kg*K)");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho")
         .set("expr", "pA*0.02897/R_const[K*mol/J]/T");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho").set("args", new String[]{"pA", "T"});
    model.component("comp1").material("mat1").propertyGroup("def").func("rho").set("dermethod", "manual");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho")
         .set("argders", new String[][]{{"pA", "d(pA*0.02897/R_const/T,pA)"}, {"T", "d(pA*0.02897/R_const/T,T)"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("rho").set("argunit", "Pa,K");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho").set("fununit", "kg/m^3");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho")
         .set("plotargs", new String[][]{{"pA", "0", "1"}, {"T", "0", "1"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("argunit", "K");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("fununit", "W/(m*K)");
    model.component("comp1").material("mat1").propertyGroup("def").func("cs")
         .set("expr", "sqrt(1.4*R_const[K*mol/J]/0.02897*T)");
    model.component("comp1").material("mat1").propertyGroup("def").func("cs").set("args", new String[]{"T"});
    model.component("comp1").material("mat1").propertyGroup("def").func("cs").set("dermethod", "manual");
    model.component("comp1").material("mat1").propertyGroup("def").func("cs").set("argunit", "K");
    model.component("comp1").material("mat1").propertyGroup("def").func("cs").set("fununit", "m/s");
    model.component("comp1").material("mat1").propertyGroup("def").func("cs")
         .set("plotargs", new String[][]{{"T", "273.15", "373.15"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("an1").set("funcname", "alpha_p");
    model.component("comp1").material("mat1").propertyGroup("def").func("an1")
         .set("expr", "-1/rho(pA,T)*d(rho(pA,T),T)");
    model.component("comp1").material("mat1").propertyGroup("def").func("an1").set("args", new String[]{"pA", "T"});
    model.component("comp1").material("mat1").propertyGroup("def").func("an1").set("argunit", "Pa,K");
    model.component("comp1").material("mat1").propertyGroup("def").func("an1").set("fununit", "1/K");
    model.component("comp1").material("mat1").propertyGroup("def").func("an1")
         .set("plotargs", new String[][]{{"pA", "101325", "101325"}, {"T", "273.15", "373.15"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("an2").set("funcname", "muB");
    model.component("comp1").material("mat1").propertyGroup("def").func("an2").set("expr", "0.6*eta(T)");
    model.component("comp1").material("mat1").propertyGroup("def").func("an2").set("args", new String[]{"T"});
    model.component("comp1").material("mat1").propertyGroup("def").func("an2").set("argunit", "K");
    model.component("comp1").material("mat1").propertyGroup("def").func("an2").set("fununit", "Pa*s");
    model.component("comp1").material("mat1").propertyGroup("def").func("an2")
         .set("plotargs", new String[][]{{"T", "200", "1600"}});
    model.component("comp1").material("mat1").propertyGroup("def").set("thermalexpansioncoefficient", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("molarmass", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("bulkviscosity", "");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("thermalexpansioncoefficient", new String[]{"alpha_p(pA,T)", "0", "0", "0", "alpha_p(pA,T)", "0", "0", "0", "alpha_p(pA,T)"});
    model.component("comp1").material("mat1").propertyGroup("def").set("molarmass", "0.02897[kg/mol]");
    model.component("comp1").material("mat1").propertyGroup("def").set("bulkviscosity", "muB(T)");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat1").propertyGroup("def").descr("relpermeability_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat1").propertyGroup("def").descr("relpermittivity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("dynamicviscosity", "eta(T)");
    model.component("comp1").material("mat1").propertyGroup("def").descr("dynamicviscosity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("ratioofspecificheat", "1.4");
    model.component("comp1").material("mat1").propertyGroup("def").descr("ratioofspecificheat_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat1").propertyGroup("def").descr("electricconductivity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("heatcapacity", "Cp(T)");
    model.component("comp1").material("mat1").propertyGroup("def").descr("heatcapacity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("density", "rho(pA,T)");
    model.component("comp1").material("mat1").propertyGroup("def").descr("density_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("thermalconductivity", new String[]{"k(T)", "0", "0", "0", "k(T)", "0", "0", "0", "k(T)"});
    model.component("comp1").material("mat1").propertyGroup("def").descr("thermalconductivity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").set("soundspeed", "cs(T)");
    model.component("comp1").material("mat1").propertyGroup("def").descr("soundspeed_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").descr("thermalexpansioncoefficient_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").descr("molarmass_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").descr("bulkviscosity_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat1").propertyGroup("def").addInput("pressure");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").set("n", "");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").set("ki", "");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").set("n", "");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").set("ki", "");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex")
         .set("ki", new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"});
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").descr("n_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex").descr("ki_symmetry", "");
    model.component("comp1").material("mat1").propertyGroup("NonlinearModel").set("BA", "(def.gamma+1)/2");
    model.component("comp1").material("mat1").propertyGroup("NonlinearModel").descr("BA_symmetry", "");
    model.component("comp1").material("mat1").set("groups", new String[][]{});
    model.component("comp1").material("mat1").set("family", "air");
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material("mat2").propertyGroup().create("Enu", "Young's modulus and Poisson's ratio");
    model.component("comp1").material("mat2").propertyGroup().create("linzRes", "Linearized resistivity");
    model.component("comp1").material("mat2").label("Copper");
    model.component("comp1").material("mat2").set("family", "copper");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat2").propertyGroup("def").descr("relpermeability_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("electricconductivity", new String[]{"5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]"});
    model.component("comp1").material("mat2").propertyGroup("def").descr("electricconductivity_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def").set("heatcapacity", "385[J/(kg*K)]");
    model.component("comp1").material("mat2").propertyGroup("def").descr("heatcapacity_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat2").propertyGroup("def").descr("relpermittivity_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def").set("emissivity", "0.5");
    model.component("comp1").material("mat2").propertyGroup("def").descr("emissivity_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def").set("density", "8940[kg/m^3]");
    model.component("comp1").material("mat2").propertyGroup("def").descr("density_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("thermalconductivity", new String[]{"400[W/(m*K)]", "0", "0", "0", "400[W/(m*K)]", "0", "0", "0", "400[W/(m*K)]"});
    model.component("comp1").material("mat2").propertyGroup("def").descr("thermalconductivity_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").set("youngsmodulus", "126e9[Pa]");
    model.component("comp1").material("mat2").propertyGroup("Enu").descr("youngsmodulus_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").set("poissonsratio", "0.34");
    model.component("comp1").material("mat2").propertyGroup("Enu").descr("poissonsratio_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("rho0", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("alpha", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("Tref", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("rho0", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("alpha", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("Tref", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("rho0", "1.667e-8[ohm*m]");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("alpha", "3.862e-3[1/K]");
    model.component("comp1").material("mat2").propertyGroup("linzRes").set("Tref", "293.15[K]");
    model.component("comp1").material("mat2").propertyGroup("linzRes").descr("rho0_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").descr("alpha_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").descr("Tref_symmetry", "");
    model.component("comp1").material("mat2").propertyGroup("linzRes").addInput("temperature");
    model.component("comp1").material("mat2").set("groups", new String[][]{});
    model.component("comp1").material("mat2").set("family", "copper");
    model.component("comp1").material("mat2").selection().geom("geom1", 1);
    model.component("comp1").material("mat2").selection().all();
    model.component("comp1").material().create("mat3", "Common");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("eta", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("Cp", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("rho", "Analytic");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("k", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("cs", "Analytic");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("an1", "Analytic");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("an2", "Analytic");
    model.component("comp1").material("mat3").propertyGroup().create("RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat3").propertyGroup().create("NonlinearModel", "Nonlinear model");
    model.component("comp1").material("mat3").label("Air 1");
    model.component("comp1").material("mat3").set("family", "air");
    model.component("comp1").material("mat3").propertyGroup("def").func("eta").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("eta").set("argunit", "K");
    model.component("comp1").material("mat3").propertyGroup("def").func("eta").set("fununit", "Pa*s");
    model.component("comp1").material("mat3").propertyGroup("def").func("Cp").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("Cp").set("argunit", "K");
    model.component("comp1").material("mat3").propertyGroup("def").func("Cp").set("fununit", "J/(kg*K)");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho")
         .set("expr", "pA*0.02897/R_const[K*mol/J]/T");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho").set("args", new String[]{"pA", "T"});
    model.component("comp1").material("mat3").propertyGroup("def").func("rho").set("dermethod", "manual");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho")
         .set("argders", new String[][]{{"pA", "d(pA*0.02897/R_const/T,pA)"}, {"T", "d(pA*0.02897/R_const/T,T)"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("rho").set("argunit", "Pa,K");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho").set("fununit", "kg/m^3");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho")
         .set("plotargs", new String[][]{{"pA", "0", "1"}, {"T", "0", "1"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("k").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("k").set("argunit", "K");
    model.component("comp1").material("mat3").propertyGroup("def").func("k").set("fununit", "W/(m*K)");
    model.component("comp1").material("mat3").propertyGroup("def").func("cs")
         .set("expr", "sqrt(1.4*R_const[K*mol/J]/0.02897*T)");
    model.component("comp1").material("mat3").propertyGroup("def").func("cs").set("args", new String[]{"T"});
    model.component("comp1").material("mat3").propertyGroup("def").func("cs").set("dermethod", "manual");
    model.component("comp1").material("mat3").propertyGroup("def").func("cs").set("argunit", "K");
    model.component("comp1").material("mat3").propertyGroup("def").func("cs").set("fununit", "m/s");
    model.component("comp1").material("mat3").propertyGroup("def").func("cs")
         .set("plotargs", new String[][]{{"T", "273.15", "373.15"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("an1").set("funcname", "alpha_p");
    model.component("comp1").material("mat3").propertyGroup("def").func("an1")
         .set("expr", "-1/rho(pA,T)*d(rho(pA,T),T)");
    model.component("comp1").material("mat3").propertyGroup("def").func("an1").set("args", new String[]{"pA", "T"});
    model.component("comp1").material("mat3").propertyGroup("def").func("an1").set("argunit", "Pa,K");
    model.component("comp1").material("mat3").propertyGroup("def").func("an1").set("fununit", "1/K");
    model.component("comp1").material("mat3").propertyGroup("def").func("an1")
         .set("plotargs", new String[][]{{"pA", "101325", "101325"}, {"T", "273.15", "373.15"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("an2").set("funcname", "muB");
    model.component("comp1").material("mat3").propertyGroup("def").func("an2").set("expr", "0.6*eta(T)");
    model.component("comp1").material("mat3").propertyGroup("def").func("an2").set("args", new String[]{"T"});
    model.component("comp1").material("mat3").propertyGroup("def").func("an2").set("argunit", "K");
    model.component("comp1").material("mat3").propertyGroup("def").func("an2").set("fununit", "Pa*s");
    model.component("comp1").material("mat3").propertyGroup("def").func("an2")
         .set("plotargs", new String[][]{{"T", "200", "1600"}});
    model.component("comp1").material("mat3").propertyGroup("def").set("thermalexpansioncoefficient", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("molarmass", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("bulkviscosity", "");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("thermalexpansioncoefficient", new String[]{"alpha_p(pA,T)", "0", "0", "0", "alpha_p(pA,T)", "0", "0", "0", "alpha_p(pA,T)"});
    model.component("comp1").material("mat3").propertyGroup("def").set("molarmass", "0.02897[kg/mol]");
    model.component("comp1").material("mat3").propertyGroup("def").set("bulkviscosity", "muB(T)");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat3").propertyGroup("def").descr("relpermeability_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat3").propertyGroup("def").descr("relpermittivity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("dynamicviscosity", "eta(T)");
    model.component("comp1").material("mat3").propertyGroup("def").descr("dynamicviscosity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("ratioofspecificheat", "1.4");
    model.component("comp1").material("mat3").propertyGroup("def").descr("ratioofspecificheat_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat3").propertyGroup("def").descr("electricconductivity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("heatcapacity", "Cp(T)");
    model.component("comp1").material("mat3").propertyGroup("def").descr("heatcapacity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("density", "rho(pA,T)");
    model.component("comp1").material("mat3").propertyGroup("def").descr("density_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("thermalconductivity", new String[]{"k(T)", "0", "0", "0", "k(T)", "0", "0", "0", "k(T)"});
    model.component("comp1").material("mat3").propertyGroup("def").descr("thermalconductivity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").set("soundspeed", "cs(T)");
    model.component("comp1").material("mat3").propertyGroup("def").descr("soundspeed_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").descr("thermalexpansioncoefficient_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").descr("molarmass_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").descr("bulkviscosity_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat3").propertyGroup("def").addInput("pressure");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").set("n", "");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").set("ki", "");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").set("n", "");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").set("ki", "");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex")
         .set("ki", new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"});
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").descr("n_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex").descr("ki_symmetry", "");
    model.component("comp1").material("mat3").propertyGroup("NonlinearModel").set("BA", "(def.gamma+1)/2");
    model.component("comp1").material("mat3").propertyGroup("NonlinearModel").descr("BA_symmetry", "");
    model.component("comp1").material("mat3").set("groups", new String[][]{});
    model.component("comp1").material("mat3").set("family", "air");
    model.component("comp1").material("mat3").selection().geom("geom1", 1);
    model.component("comp1").material("mat3").selection().set(1, 6);

    model.component("comp1").physics("emw").create("lport1", "LumpedPort", 1);
    model.component("comp1").physics("emw").feature("lport1").selection().set(6);
    model.component("comp1").physics("emw").create("imp1", "Impedance", 1);
    model.component("comp1").physics("emw").feature("imp1").selection().all();
    model.component("comp1").physics("emw").feature("lport1").set("TerminalType", "Current");
    model.component("comp1").physics("emw").create("ecd1", "ExternalCurrentDensity", 2);

    model.param().remove("LipH");
    model.param().set("LipOut", "0.16[m]");
    model.param().remove("TurtW");
    model.param().remove("TurtOutH");
    model.param().remove("TurtInH");
    model.param().remove("NeckH");
    model.param().remove("LipOut");
    model.param().remove("LipIn");
    model.param().remove("TopH");
    model.param().remove("TopW");
    model.param().remove("MidH");
    model.param().remove("Move");
    model.param().remove("freqscaling_up");
    model.param().set("box_out", "0.16 [m]");
    model.param().set("Th", "0.000 [m]");

    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "box_out", 7, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "boc_out", 8, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "box+out", 8, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "box_out", 8, 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "box_out", 9, 0);

    model.param().set("Tot_H", "1.65 [m]");
    model.param().set("Box_H", "0.25 [m]");

    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H", 8, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H-0.05", 7, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H +Box_H", 9, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H +Box_H", 10, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H", 1, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H", 0, 1);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H", 2, 1);

    return model;
  }

  public static Model run2(Model model) {

    model.param().rename("box_out", "NeckdownOut");
    model.param().set("NeckdownIn", "0.085");

    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "NeckdownOut", 7, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "NeckdownOut", 9, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "NeckdownOut", 8, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "NeckdownIn", 2, 0);
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "NeckdownIn", 1, 0);
    model.component("comp1").geom("geom1").run("pol1");
    model.component("comp1").geom("geom1").feature("pol1").setIndex("table", "Tot_H-0.05", 2, 1);
    model.component("comp1").geom("geom1").run("pol1");
    model.component("comp1").geom("geom1").feature("ls1").selection("vertex1").set("pol1", 4);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("emw").feature("ecd1").active(false);
    model.component("comp1").physics("emw").feature("ecd1").selection().set(1, 2);

    model.study("std1").feature("freq").set("punit", "MHz");
    model.study("std1").feature("freq").set("plist", "range(5,0.1,300)");

    model.sol().create("sol1");
    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "Default");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.result().create("pg1", "PlotGroup2D");
    model.result("pg1").label("Electric Field (emw)");
    model.result("pg1").set("dataisaxisym", "off");
    model.result("pg1").set("frametype", "spatial");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("surf1", "Surface");
    model.result("pg1").feature("surf1").label("Surface");
    model.result("pg1").feature("surf1").set("colortable", "RainbowLight");
    model.result("pg1").feature("surf1").set("smooth", "internal");
    model.result("pg1").feature("surf1").set("data", "parent");
    model.result().dataset().create("rev1", "Revolve2D");
    model.result().dataset("rev1").label("Revolution 2D");
    model.result().dataset("rev1").set("startangle", -90);
    model.result().dataset("rev1").set("revangle", 225);
    model.result().dataset("rev1").set("data", "dset1");

    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().numerical().create("gev1", "EvalGlobal");
    model.result().numerical("gev1").setIndex("expr", "real(emw.Zport_1)", 0);
    model.result().numerical("gev1").setIndex("expr", "imag(emw.Zport_1)", 1);
    model.result().table().create("tbl1", "Table");
    model.result().table("tbl1").comments("Global Evaluation 1");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().create("pg2", "PlotGroup1D");
    model.result("pg2").set("data", "none");
    model.result("pg2").create("tblp1", "Table");
    model.result("pg2").feature("tblp1").set("source", "table");
    model.result("pg2").feature("tblp1").set("table", "tbl1");
    model.result("pg2").run();

    model.component("comp1").physics("emw").feature("ecd1").active(true);
    model.component("comp1").physics("emw").feature("lport1").active(false);
    model.component("comp1").physics("emw").feature("ecd1")
         .set("Je", new String[]{"0", "0", "6.636e-24*emw.freq[1/Hz]*10"});

    model.result().table("tbl1").clearTableData();

    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().numerical().create("int1", "IntLine");
    model.result().numerical("int1").set("intsurface", true);
    model.result().numerical("int1").selection().set(6);
    model.result().numerical("int1").setIndex("expr", "emw.Er", 0);
    model.result().numerical("int1").set("intsurface", false);
    model.result().table().create("tbl2", "Table");
    model.result().table("tbl2").comments("Line Integration 1");
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();
    model.result().numerical("int1").setIndex("expr", "abs(emw.Er)", 0);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").appendResult();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();

    model.component("comp1").physics("emw").feature("ecd1").set("Je", new int[]{0, 0, 1});

    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();

    model.sol("sol1").clearSolutionData();

    model.param().set("b", "0.6425 [m]");

    model.component("comp1").geom("geom1").run("fin");

    model.param().set("NeckdownIn", "0.085 [m]");

    model.study("std1").create("param", "Parametric");
    model.study("std1").feature("param").setIndex("pname", "b", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "b", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "vertscaling", 0);
    model.study("std1").feature("param").setIndex("plistarr", "range(0.2,0.2,1)", 0);

    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch().create("p1", "Parametric");
    model.batch("p1").study("std1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "none");
    model.batch("p1").set("pname", new String[]{"vertscaling"});
    model.batch("p1").set("plistarr", new String[]{"range(0.2,0.2,1)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.sol().create("sol2");
    model.sol("sol2").study("std1");
    model.sol("sol2").label("Parametric Solutions 1");

    model.batch("p1").feature("so1").set("psol", "sol2");

    model.result().create("pg3", "PlotGroup2D");
    model.result("pg3").label("Electric Field (emw) 1");
    model.result("pg3").set("showlooplevel", new String[]{"off", "off", "off"});
    model.result("pg3").set("dataisaxisym", "off");
    model.result("pg3").set("frametype", "spatial");
    model.result("pg3").set("data", "dset2");
    model.result("pg3").feature().create("surf1", "Surface");
    model.result("pg3").feature("surf1").label("Surface");
    model.result("pg3").feature("surf1").set("colortable", "RainbowLight");
    model.result("pg3").feature("surf1").set("smooth", "internal");
    model.result("pg3").feature("surf1").set("data", "parent");
    model.result().dataset().create("rev2", "Revolve2D");
    model.result().dataset("rev2").label("Revolution 2D 1");
    model.result().dataset("rev2").set("startangle", -90);
    model.result().dataset("rev2").set("revangle", 225);
    model.result().dataset("rev2").set("data", "dset2");

    model.batch("p1").run();

    model.result("pg3").run();
    model.result("pg3").set("data", "dset2");
    model.result().dataset("rev2").set("data", "dset2");
    model.result("pg3").run();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").set("data", "dset2");
    model.result().numerical("int1").setIndex("looplevelinput", "manual", 1);
    model.result().numerical("int1").setIndex("looplevel", new int[]{1}, 1);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").setIndex("looplevel", new int[]{2}, 1);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").setIndex("looplevel", new int[]{3}, 1);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").setIndex("looplevel", new int[]{4}, 1);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();
    model.result().table("tbl2").clearTableData();
    model.result().numerical("int1").setIndex("looplevel", new int[]{5}, 1);
    model.result().numerical("int1").set("table", "tbl2");
    model.result().numerical("int1").setResult();

    model.component("comp1").physics("emw").feature("ecd1").active(false);
    model.component("comp1").physics("emw").feature("lport1").active(true);

    model.result().table("tbl2").clearTableData();

    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"vertscaling"});
    model.batch("p1").set("plistarr", new String[]{"range(0.2,0.2,1)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run();

    model.result("pg3").run();
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").set("data", "dset2");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").setIndex("looplevelinput", "first", 1);
    model.result().numerical("gev1").setIndex("looplevelinput", "manual", 1);
    model.result().numerical("gev1").setIndex("looplevel", new int[]{1}, 1);
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().numerical("gev1").setIndex("looplevel", new int[]{2}, 1);
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").setIndex("looplevel", new int[]{3}, 1);
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().numerical("gev1").setIndex("looplevel", new int[]{4}, 1);
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().numerical("gev1").setIndex("looplevel", new int[]{5}, 1);
    model.result().table("tbl1").clearTableData();
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();

    model.param().set("vertscaling", "0.6");

    model.component("comp1").geom("geom1").run("fin");

    model.param().set("vertscaling", "0.2");

    model.component("comp1").geom("geom1").run("fin");

    model.study().create("std2");
    model.study("std2").create("eig", "Eigenfrequency");
    model.study("std2").feature("eig").set("solnum", "auto");
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("ngen", "5");
    model.study("std2").feature("eig").activate("emw", true);

    model.component("comp1").physics("emw").feature("lport1").active(false);

    model.study("std2").feature("eig").set("eigunit", "MHz");
    model.study("std2").feature("eig").set("shift", "100");

    model.sol().create("sol8");
    model.sol("sol8").study("std2");

    model.study("std2").feature("eig").set("notlistsolnum", 1);
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("listsolnum", 1);
    model.study("std2").feature("eig").set("solnum", "auto");

    model.sol("sol8").create("st1", "StudyStep");
    model.sol("sol8").feature("st1").set("study", "std2");
    model.sol("sol8").feature("st1").set("studystep", "eig");
    model.sol("sol8").create("v1", "Variables");
    model.sol("sol8").feature("v1").set("control", "eig");
    model.sol("sol8").create("e1", "Eigenvalue");
    model.sol("sol8").feature("e1").set("eigref", "100");
    model.sol("sol8").feature("e1").set("control", "eig");
    model.sol("sol8").feature("e1").feature("aDef").set("complexfun", true);
    model.sol("sol8").feature("e1").create("d1", "Direct");
    model.sol("sol8").feature("e1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol8").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol8").attach("std2");

    model.result().create("pg4", "PlotGroup2D");
    model.result("pg4").label("Electric Field (emw) 2");
    model.result("pg4").set("showlooplevel", new String[]{"off", "off", "off"});
    model.result("pg4").set("dataisaxisym", "off");
    model.result("pg4").set("frametype", "spatial");
    model.result("pg4").set("data", "dset3");
    model.result("pg4").feature().create("surf1", "Surface");
    model.result("pg4").feature("surf1").label("Surface");
    model.result("pg4").feature("surf1").set("colortable", "RainbowLight");
    model.result("pg4").feature("surf1").set("smooth", "internal");
    model.result("pg4").feature("surf1").set("data", "parent");
    model.result().dataset().create("rev3", "Revolve2D");
    model.result().dataset("rev3").label("Revolution 2D 2");
    model.result().dataset("rev3").set("startangle", -90);
    model.result().dataset("rev3").set("revangle", 225);
    model.result().dataset("rev3").set("data", "dset3");

    model.sol("sol8").runAll();

    model.result("pg4").run();
    model.result("pg4").set("data", "dset3");
    model.result().dataset("rev3").set("data", "dset3");
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();

    model.param().set("vertscaling", "0.4");

    model.component("comp1").geom("geom1").run("fin");

    model.sol("sol1").study("std1");

    model.study("std1").feature("freq").set("notlistsolnum", 1);
    model.study("std1").feature("freq").set("notsolnum", "auto");
    model.study("std1").feature("freq").set("listsolnum", 1);
    model.study("std1").feature("freq").set("solnum", "auto");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "freq");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "freq");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("stol", 0.01);
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature().remove("pDef");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{"range(5,0.1,300)"});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{"MHz"});
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol1").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol1").feature("s1").set("control", "freq");
    model.sol("sol1").feature("s1").feature("aDef").set("complexfun", true);
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"vertscaling"});
    model.batch("p1").set("plistarr", new String[]{"range(0.2,0.2,1)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.component("comp1").geom("geom1").run("fin");

    model.sol("sol8").study("std2");

    model.study("std2").feature("eig").set("notlistsolnum", 1);
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("listsolnum", 1);
    model.study("std2").feature("eig").set("solnum", "auto");

    model.sol("sol8").feature().remove("e1");
    model.sol("sol8").feature().remove("v1");
    model.sol("sol8").feature().remove("st1");
    model.sol("sol8").create("st1", "StudyStep");
    model.sol("sol8").feature("st1").set("study", "std2");
    model.sol("sol8").feature("st1").set("studystep", "eig");
    model.sol("sol8").create("v1", "Variables");
    model.sol("sol8").feature("v1").set("control", "eig");
    model.sol("sol8").create("e1", "Eigenvalue");
    model.sol("sol8").feature("e1").set("eigref", "100");
    model.sol("sol8").feature("e1").set("control", "eig");
    model.sol("sol8").feature("e1").feature("aDef").set("complexfun", true);
    model.sol("sol8").feature("e1").create("d1", "Direct");
    model.sol("sol8").feature("e1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol8").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol8").attach("std2");
    model.sol("sol8").runAll();

    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{6});
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{6});
    model.result("pg4").run();

    model.param().set("vertscaling", "0.6");

    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol8").study("std2");

    model.study("std2").feature("eig").set("notlistsolnum", 1);
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("listsolnum", 1);
    model.study("std2").feature("eig").set("solnum", "auto");

    model.sol("sol8").feature().remove("e1");

    return model;
  }

  public static Model run3(Model model) {
    model.sol("sol8").feature().remove("v1");
    model.sol("sol8").feature().remove("st1");
    model.sol("sol8").create("st1", "StudyStep");
    model.sol("sol8").feature("st1").set("study", "std2");
    model.sol("sol8").feature("st1").set("studystep", "eig");
    model.sol("sol8").create("v1", "Variables");
    model.sol("sol8").feature("v1").set("control", "eig");
    model.sol("sol8").create("e1", "Eigenvalue");
    model.sol("sol8").feature("e1").set("eigref", "100");
    model.sol("sol8").feature("e1").set("control", "eig");
    model.sol("sol8").feature("e1").feature("aDef").set("complexfun", true);
    model.sol("sol8").feature("e1").create("d1", "Direct");
    model.sol("sol8").feature("e1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol8").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol8").attach("std2");
    model.sol("sol8").runAll();

    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();

    model.param().set("vertscaling", "0.8");

    model.component("comp1").geom("geom1").run("fin");

    model.sol("sol8").study("std2");

    model.study("std2").feature("eig").set("notlistsolnum", 1);
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("listsolnum", 1);
    model.study("std2").feature("eig").set("solnum", "auto");

    model.sol("sol8").feature().remove("e1");
    model.sol("sol8").feature().remove("v1");
    model.sol("sol8").feature().remove("st1");
    model.sol("sol8").create("st1", "StudyStep");
    model.sol("sol8").feature("st1").set("study", "std2");
    model.sol("sol8").feature("st1").set("studystep", "eig");
    model.sol("sol8").create("v1", "Variables");
    model.sol("sol8").feature("v1").set("control", "eig");
    model.sol("sol8").create("e1", "Eigenvalue");
    model.sol("sol8").feature("e1").set("eigref", "100");
    model.sol("sol8").feature("e1").set("control", "eig");
    model.sol("sol8").feature("e1").feature("aDef").set("complexfun", true);
    model.sol("sol8").feature("e1").create("d1", "Direct");
    model.sol("sol8").feature("e1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol8").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol8").attach("std2");
    model.sol("sol8").runAll();

    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{6});
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();

    model.param().set("vertscaling", "1");

    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol8").study("std2");

    model.study("std2").feature("eig").set("notlistsolnum", 1);
    model.study("std2").feature("eig").set("notsolnum", "auto");
    model.study("std2").feature("eig").set("listsolnum", 1);
    model.study("std2").feature("eig").set("solnum", "auto");

    model.sol("sol8").feature().remove("e1");
    model.sol("sol8").feature().remove("v1");
    model.sol("sol8").feature().remove("st1");
    model.sol("sol8").create("st1", "StudyStep");
    model.sol("sol8").feature("st1").set("study", "std2");
    model.sol("sol8").feature("st1").set("studystep", "eig");
    model.sol("sol8").create("v1", "Variables");
    model.sol("sol8").feature("v1").set("control", "eig");
    model.sol("sol8").create("e1", "Eigenvalue");
    model.sol("sol8").feature("e1").set("eigref", "100");
    model.sol("sol8").feature("e1").set("control", "eig");
    model.sol("sol8").feature("e1").feature("aDef").set("complexfun", true);
    model.sol("sol8").feature("e1").create("d1", "Direct");
    model.sol("sol8").feature("e1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol8").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
    model.sol("sol8").attach("std2");
    model.sol("sol8").runAll();

    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{5});
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{6});
    model.result("pg4").run();
    model.result("pg4").set("looplevel", new int[]{7});
    model.result("pg4").run();

    model.component("comp1").physics("emw").active(false);
    model.component("comp1").physics().remove("emw");

    model.study().remove("std1");
    model.study().remove("std2");

    model.param().set("main_IR", "0.725 [m]");
    model.param().set("main_OR", "0.845 [m]");
    model.param().set("main_h", "1.2 [m]");
    model.param().set("main_bottomz", "-0.15 [m]");
    model.param().set("bucking2_IR", "0.725 [m]");
    model.param().set("bucking2_OR", "0.765[m]");
    model.param().set("bucking2_h", "0.3 [m]");
    model.param().set("bucking2_bottomz", "1.55 [m]");
    model.param().set("bucking2a_IR", "0.725 [m]");
    model.param().set("bucking2a_OR", "0.845 [m]");
    model.param().set("bucking2a_h", "0.1[m]");
    model.param().set("bucking2a_bottomz", "1.6 [m]");

    model.component("comp1").physics().create("mf", "InductionCurrents", "geom1");
    model.component("comp1").physics("mf").create("coil1", "Coil", 2);

    model.component("comp1").geom("geom1").run("ls1");
    model.component("comp1").geom("geom1").create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"main_IR", "0"});
    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"main_OR-main_IR", "1"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", "main_H", 1);
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"main_IR", "main_bottomz"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", "main_h", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r2", "r1");
    model.component("comp1").geom("geom1").feature("r2").set("size", new String[]{"main2_OR-main2_IR", "main_h"});
    model.component("comp1").geom("geom1").feature("r2").setIndex("size", "main2_h", 1);
    model.component("comp1").geom("geom1").feature("r2")
         .set("size", new String[]{"bucking2_OR-bucking2_IR", "main2_h"});
    model.component("comp1").geom("geom1").feature("r2").setIndex("size", "bucking2_h", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r2").set("pos", new String[]{"main_IR", "bucking2_bottomz"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r2").active(false);
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").feature("r2").active(true);
    model.component("comp1").geom("geom1").feature().duplicate("r3", "r2");
    model.component("comp1").geom("geom1").feature("r3")
         .set("size", new String[]{"bucking2a_OR-bucking2_IR", "bucking2_h"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r3")
         .set("size", new String[]{"bucking2a_OR-bucking2a_IR", "bucking2_h"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r3").setIndex("size", "bucking2a_h", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r3").set("pos", new String[]{"main_IR", "bucking2a_bottomz"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").run("r3");
    model.component("comp1").geom("geom1").create("r4", "Rectangle");
    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{1, 2});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new int[]{0, -1});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{0, -0.5});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{1, 3});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{0, -1.5});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{0, -0.5});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new double[]{1.5, 3});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(4);

    model.component("comp1").material().create("mat4", "Common");
    model.component("comp1").material("mat4").propertyGroup().create("Enu", "Young's modulus and Poisson's ratio");
    model.component("comp1").material("mat4").propertyGroup().create("linzRes", "Linearized resistivity");
    model.component("comp1").material("mat4").label("Copper 1");
    model.component("comp1").material("mat4").set("family", "copper");
    model.component("comp1").material("mat4").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat4").propertyGroup("def").descr("relpermeability_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def")
         .set("electricconductivity", new String[]{"5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]"});
    model.component("comp1").material("mat4").propertyGroup("def").descr("electricconductivity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").set("heatcapacity", "385[J/(kg*K)]");
    model.component("comp1").material("mat4").propertyGroup("def").descr("heatcapacity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat4").propertyGroup("def").descr("relpermittivity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").set("emissivity", "0.5");
    model.component("comp1").material("mat4").propertyGroup("def").descr("emissivity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").set("density", "8940[kg/m^3]");
    model.component("comp1").material("mat4").propertyGroup("def").descr("density_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def")
         .set("thermalconductivity", new String[]{"400[W/(m*K)]", "0", "0", "0", "400[W/(m*K)]", "0", "0", "0", "400[W/(m*K)]"});
    model.component("comp1").material("mat4").propertyGroup("def").descr("thermalconductivity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("Enu").set("youngsmodulus", "126e9[Pa]");
    model.component("comp1").material("mat4").propertyGroup("Enu").descr("youngsmodulus_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("Enu").set("poissonsratio", "0.34");
    model.component("comp1").material("mat4").propertyGroup("Enu").descr("poissonsratio_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("rho0", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("alpha", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("Tref", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("rho0", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("alpha", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("Tref", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("rho0", "1.667e-8[ohm*m]");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("alpha", "3.862e-3[1/K]");
    model.component("comp1").material("mat4").propertyGroup("linzRes").set("Tref", "293.15[K]");
    model.component("comp1").material("mat4").propertyGroup("linzRes").descr("rho0_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").descr("alpha_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").descr("Tref_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("linzRes").addInput("temperature");
    model.component("comp1").material("mat4").set("groups", new String[][]{});
    model.component("comp1").material("mat4").set("family", "copper");
    model.component("comp1").material("mat4").selection().set(4, 5, 6, 7, 8);

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "9600[kA]");
    model.component("comp1").physics("mf").feature("coil1").set("sigma_mat", "userdef");
    model.component("comp1").physics("mf").feature("coil1")
         .set("sigma", new String[]{"1e10", "0", "0", "0", "1e10", "0", "0", "0", "1e10"});
    model.component("comp1").physics("mf").feature().duplicate("coil2", "coil1");
    model.component("comp1").physics("mf").feature("coil2").selection().set(4, 5, 6, 7);
    model.component("comp1").physics("mf").feature("coil2").set("ICoil", "-1000[kA]");
    model.component("comp1").physics("mf").feature().duplicate("coil3", "coil2");
    model.component("comp1").physics("mf").feature("coil3").selection().set(4, 8);
    model.component("comp1").physics("mf").feature("coil2").selection().set(5, 6, 7);
    model.component("comp1").physics("mf").feature("coil3").set("ICoil", "-650[kA]");

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");
    model.study("std1").feature("stat").activate("mf", true);

    model.sol().create("sol1");
    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.result().create("pg3", "PlotGroup2D");
    model.result("pg3").label("Magnetic Flux Density Norm (mf)");
    model.result("pg3").set("dataisaxisym", "off");
    model.result("pg3").set("data", "dset1");
    model.result("pg3").feature().create("surf1", "Surface");
    model.result("pg3").feature("surf1").set("colortable", "RainbowLight");
    model.result("pg3").feature("surf1").set("data", "parent");
    model.result("pg3").feature().create("con1", "Contour");
    model.result("pg3").feature("con1").set("expr", "mf.Aphi*r");
    model.result("pg3").feature("con1").set("titletype", "none");
    model.result("pg3").feature("con1").set("number", 15);
    model.result("pg3").feature("con1").set("levelrounding", false);
    model.result("pg3").feature("con1").set("coloring", "uniform");
    model.result("pg3").feature("con1").set("colorlegend", false);
    model.result("pg3").feature("con1").set("color", "gray");
    model.result("pg3").feature("con1").set("data", "parent");
    model.result().dataset().create("rev1", "Revolve2D");
    model.result().dataset("rev1").set("startangle", -90);
    model.result().dataset("rev1").set("revangle", 225);
    model.result().dataset("rev1").set("data", "dset1");
    model.result().create("pg4", "PlotGroup3D");
    model.result("pg4").label("Magnetic Flux Density Norm, Revolved Geometry (mf)");
    model.result("pg4").set("data", "rev1");
    model.result("pg4").feature().create("surf1", "Surface");
    model.result("pg4").feature("surf1").set("colortable", "RainbowLight");
    model.result("pg4").feature("surf1").set("data", "parent");
    model.result("pg4").feature().create("con1", "Contour");
    model.result("pg4").feature("con1").set("expr", "mf.Aphi*r");
    model.result("pg4").feature("con1").set("titletype", "none");
    model.result("pg4").feature("con1").set("number", 15);
    model.result("pg4").feature("con1").set("levelrounding", false);
    model.result("pg4").feature("con1").set("coloring", "uniform");
    model.result("pg4").feature("con1").set("colorlegend", false);
    model.result("pg4").feature("con1").set("color", "gray");
    model.result("pg4").feature("con1").set("data", "parent");

    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil3").selection().set(8);

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{2, 3});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{10, 10});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new int[]{0, -3});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{2, 5});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{0, -1.5});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result().export().create("plot1", "Plot");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_oct20_drostermethod.txt");
    model.result().export("plot1").run();

    model.label("m3Neckdown_WithBox_Oct2022_2D_magnet.mph");

    model.result("pg3").feature("con1").active(false);
    model.result("pg3").feature("con1").active(true);
    model.result("pg3").run();
    model.result().duplicate("pg5", "pg3");
    model.result("pg5").run();
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").label("Magnetic Flux Density r(mf)");
    model.result("pg5").run();
    model.result("pg5").label("Magnetic Flux Density z(mf) 1");
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("expr", "mf.Br");
    model.result("pg3").run();
    model.result("pg3").feature("con1").set("expr", "mf.Aphi-r");
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").feature("surf1").set("expr", "mf.Bz");
    model.result("pg5").run();
    model.result("pg5").feature("con1").set("expr", "mf.Az*r");
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").feature().remove("con1");
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_oct24_drostermethod_r.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_oct24_drostermethod_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_oct24_drostermethod_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").run();

    model.label("m3Neckdown_WithBox_Oct2022_2D_magnet.mph");

    model.result("pg5").run();

    model.component("comp1").geom("geom1").feature("pol1").active(false);
    model.component("comp1").geom("geom1").feature("ls1").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");

    model.param().set("main_OR", "0.8582 [m]");

    model.component("comp1").geom("geom1").run();

    model.param()
         .remove(new String[]{"main_IR", "main_OR", "main_h", "main_bottomz", "bucking2_IR", "bucking2_OR", "bucking2_h", "bucking2_bottomz", "bucking2a_IR", "bucking2a_OR", 
         "bucking2a_h", "bucking2a_bottomz"});
    model.param().set("coil1_a", "0.7250 [m]");
    model.param().set("coil1_b", "0.8582 [m]");
    model.param().set("coil1_h", "0.2349 [m]");
    model.param().set("coil1_zb", "-0.2000 [m]");

    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"coil1b-coil1a", "main_h"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", "coil1_h", 1);
    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"coil1_b-coil1_a", "coil1_h"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "coil_a", 0);
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a", "coil1_bz"});
    model.component("comp1").geom("geom1").run("ls1");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a", "coil1_z"});
    model.component("comp1").geom("geom1").feature().remove("r2");
    model.component("comp1").geom("geom1").feature().remove("r3");
    model.component("comp1").geom("geom1").feature("r4").label("SPACE");
    model.component("comp1").geom("geom1").feature("r1").label("Coil1");
    model.component("comp1").geom("geom1").run("ls1");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a", "coil1_zb"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r5", "r1");
    model.component("comp1").geom("geom1").feature("r5").label("Coil2");
    model.component("comp1").geom("geom1").feature("r5").setIndex("size", "coil2_h", 1);
    model.component("comp1").geom("geom1").feature("r5").set("size", new String[]{"coil2_b-coil2_a", "coil2_h"});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"coil2_a", "coil2_zb"});

    model.param().set("coil2_a", "0.7250 [m]");
    model.param().set("coil2_b", "0.8546 [m]");
    model.param().set("coil2_h", "0.1944 [m]");
    model.param().set("coil2_zb", "0.1095 [m]");

    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r6", "r5");
    model.component("comp1").geom("geom1").feature("r6").label("Coil3");
    model.component("comp1").geom("geom1").feature("r6").set("size", new String[]{"coil3_b-coil3_a", "coil2_h"});
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", "coil3_h", 1);
    model.component("comp1").geom("geom1").feature("r6").set("pos", new String[]{"coil3_a", "coil3_zb"});

    model.param().set("coil3_a", "0.7250 [m]");
    model.param().set("coil3_b", "0.8474 [m]");
    model.param().set("coil3_h", "0.1944 [m]");
    model.param().set("coil3_zb", "0.3285 [m]");

    model.component("comp1").geom("geom1").runPre("fin");

    model.param().set("coil3_zb", "0.2785 [m]");
    model.param().set("coil2_zb", "0.0595 [m]");

    model.component("comp1").geom("geom1").runPre("fin");

    model.param().set("coil4_a", "0.7250 [m]");
    model.param().set("coil4_b", "0.8474 [m]");

    model.component("comp1").geom("geom1").feature().duplicate("r7", "r6");
    model.component("comp1").geom("geom1").feature("r7").set("size", new String[]{"coil4_b-coil4_a", "coil3_h"});
    model.component("comp1").geom("geom1").feature("r7").setIndex("size", "coil4_h", 1);
    model.component("comp1").geom("geom1").feature("r7").set("pos", new String[]{"coil4_a", "coil4_zb"});
    model.component("comp1").geom("geom1").feature("r7").label("Coil4");

    model.param().set("coil4_h", "0.1944 [m]");
    model.param().set("coil4_zb", "0.4969 [m]");

    model.component("comp1").geom("geom1").runPre("fin");

    model.param().set("coil5_a", "0.7250 [m]");
    model.param().set("coil5_b", "0.8546 [m]");
    model.param().set("coil5_h", "0.1944 [m]");
    model.param().set("coil5_zb", "0.7159 [m]");

    model.component("comp1").geom("geom1").feature().duplicate("r8", "r7");
    model.component("comp1").geom("geom1").feature("r8").label("Coil5");
    model.component("comp1").geom("geom1").feature("r8").set("size", new String[]{"coil5_b-coil5_a", "coil4_h"});
    model.component("comp1").geom("geom1").feature("r8").setIndex("size", "coil5_h", 1);
    model.component("comp1").geom("geom1").feature("r8").set("pos", new String[]{"coil5_a", "coil5_zb"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r9", "r8");
    model.component("comp1").geom("geom1").feature("r9").label("Coil6");
    model.component("comp1").geom("geom1").feature("r9").set("size", new String[]{"coil6_b-coil6_a", "coil5_h"});
    model.component("comp1").geom("geom1").feature("r9").setIndex("size", "coil6_h", 1);
    model.component("comp1").geom("geom1").feature("r9").set("pos", new String[]{"coil6_a", "coil6_zb"});

    model.param().set("coil6_a", "0.7250");
    model.param().set("coil6_b", "0.8582");
    model.param().set("coil6_a", "0.7250 [m]");
    model.param().set("coil6_b", "0.8582 [m]");
    model.param().set("coil6_h", "0.1940 [m]");
    model.param().set("coil6_zb", "0.9349 [m]");

    model.component("comp1").geom("geom1").runPre("fin");

    model.param().set("coilb_a", "0.7450 [m]");
    model.param().set("coilb_b", "0.8314 [m]");
    model.param().set("coilb_h", "0.2590 [m]");
    model.param().set("coilb_zb", "1.5000 [m]");

    model.component("comp1").geom("geom1").feature().duplicate("r10", "r9");
    model.component("comp1").geom("geom1").feature("r10").label("BuckCoil");
    model.component("comp1").geom("geom1").feature("r10").set("size", new String[]{"coilb_b-coil6_a", "coil6_h"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r10").set("size", new String[]{"coilb_b-coilb_a", "coil6_h"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r10").setIndex("size", "coilb_h", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r10").set("pos", new String[]{"coilb_a", "coilb_zb"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "2060.00[kA]");
    model.component("comp1").physics("mf").feature("coil2").selection().set(3);
    model.component("comp1").physics("mf").feature("coil2").set("ICoil", "1658.8[kA]");
    model.component("comp1").physics("mf").feature("coil3").selection().set(4);

    return model;
  }

  public static Model run4(Model model) {
    model.component("comp1").physics("mf").feature("coil3").set("ICoil", "1566.6[kA]");
    model.component("comp1").physics("mf").feature().duplicate("coil4", "coil3");
    model.component("comp1").physics("mf").feature("coil4").selection().set(5);
    model.component("comp1").physics("mf").feature().duplicate("coil5", "coil4");
    model.component("comp1").physics("mf").feature("coil5").selection().set(6);
    model.component("comp1").physics("mf").feature("coil5").set("ICoil", "1658.8[kA]");
    model.component("comp1").physics("mf").feature().duplicate("coil6", "coil5");
    model.component("comp1").physics("mf").feature("coil6").set("ICoil", "1701.4[kA]");
    model.component("comp1").physics("mf").feature().duplicate("coil7", "coil6");
    model.component("comp1").physics("mf").feature("coil7").label("BuckCoil");
    model.component("comp1").physics("mf").feature("coil7").set("ICoil", "-1864.7[kA]");

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("expr", "mf.Bz");
    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil7").selection().set(8);
    model.component("comp1").physics("mf").feature("coil6").selection().set(7);

    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result().table().create("evl2", "Table");
    model.result().table("evl2").comments("Interactive 2D values");
    model.result().table("evl2").label("Evaluation 2D");
    model.result().table("evl2")
         .addRow(new double[]{0.058731794357299805, 1.6779217720031738, 0.019686387073057372}, new double[]{0, 0, 0});
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_dec16_drostermethod_z.txt");
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("expr", "mf.Br");
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_dec16_drostermethod_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("expr", "mf.Bz");
    model.result().export("plot1")
         .set("filename", "C:\\Users\\Irwin_Lab\\Desktop\\Nicholas_COMSOL\\WorkingFiles_DoNotEdit\\m3multiplecoaxes\\Bfield_dec16_drostermethod_z.txt");
    model.result().export("plot1").run();

    model.label("m3MagnetSim.mph");

    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/m3Bfield_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/m3Bfield_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/m3Bfield_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("expr", "mf.Br");
    model.result("pg3").run();
    model.result().export("plot1").run();
    model.result("pg3").run();

    model.component("comp1").physics("mf").feature().remove("coil1");
    model.component("comp1").physics("mf").feature().remove("coil2");
    model.component("comp1").physics("mf").feature().remove("coil3");
    model.component("comp1").physics("mf").feature().remove("coil4");
    model.component("comp1").physics("mf").feature().remove("coil5");
    model.component("comp1").physics("mf").feature().remove("coil6");
    model.component("comp1").physics("mf").feature().remove("coil7");

    model.component("comp1").geom("geom1").feature().remove("r9");
    model.component("comp1").geom("geom1").feature().remove("r8");
    model.component("comp1").geom("geom1").feature().remove("r7");
    model.component("comp1").geom("geom1").feature().remove("r10");
    model.component("comp1").geom("geom1").feature().remove("r6");
    model.component("comp1").geom("geom1").feature().remove("r5");
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 2, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 2.5, 1);
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a-0.25", "coil1_zb"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a", "coil1_zb-0.25"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").create("coil1", "Coil", 2);
    model.component("comp1").physics("mf").feature("coil1").selection().set(2);
    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "1566[A]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();

    model.component("comp1").mesh("mesh1").autoMeshSize(4);
    model.component("comp1").mesh("mesh1").run();
    model.component("comp1").mesh("mesh1").autoMeshSize(2);
    model.component("comp1").mesh("mesh1").run();

    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 3, 1);
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"coil1_a", "coil1_zb-0.5"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg5").run();
    model.result("pg4").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "1700[A]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "1800[A]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg5").run();
    model.result("pg4").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg4").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "1800[kA]");

    model.result("pg3").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("sigma_mat", "userdef");
    model.component("comp1").physics("mf").feature("coil1")
         .set("sigma", new String[]{"1e10", "0", "0", "0", "1e10", "0", "0", "0", "1e10"});

    model.result("pg5").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "25670[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "20000[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.575110912322998, 0.8894124031066895, 6.811812752313767}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "17000[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.5052664279937744, 0.1909644603729248, 5.567460889392413}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.5052664279937744, 0.575110673904419, 5.746532000210371}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.412139892578125, 0.9592571258544922, 5.7354445049949945}, new double[]{0, 0, 0});
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.47034382820129395, 0.9243347644805907, 5.7560028611562615}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.5401885509490967, 0.7613637447357178, 5.781271316457191}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "coil1_a-0.6", 0);
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.041089385747909546, 0.9869040250778198, 7.030149383555953}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "coil1_a", 0);
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.result("pg3").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_z.txt");
    model.result().export("plot1").run();
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r4").set("size", new double[]{2, 5.5});
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{0, -1.75});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250721/pencilm3Bfield_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.5842545628547668, 0.9114865064620972, 5.794645969639248}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r4").set("size", new double[]{3, 5.5});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg4").run();
    model.result().move("pg4", 3);
    model.result("pg2").run();
    model.result().create("pg6", "PlotGroup2D");
    model.result("pg6").run();
    model.result().move("pg6", 3);
    model.result("pg5").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg6").create("surf1", "Surface");
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").label("Magnetic Flux Density norm");
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.365549087524414, 1.0448172092437744, 0.6153626962430163}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0326223373413086, 1.019207239151001, 0.7503895251513052}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.9301834106445312, 1.019207239151001, 0.7954655317780219}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.7893297672271729, 1.019207239151001, 2.702977974055485}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3795738220214844, 1.0704267024993896, 6.100942225977848}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4564027786254883, 1.1600611209869385, 6.0924148572377295}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4564027786254883, 1.1600611209869385, 6.0924148572377295}, new double[]{0, 0, 0});
    model.result("pg6").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250730/pencilm3Bfield_20250730_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250730/pencilm3Bfield_20250730_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);

    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"(coil1_b-coil1_a)*l", "3"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", "3*l", 1);
    model.component("comp1").geom("geom1").feature("r1").set("base", "center");
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "coil1_a+3*l/2", 0);
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "(coil1_a+3/2)*l", 0);
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "(coil1_a+(coil1_b-coil1_a)/2)*l", 0);
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "(coil1_zb-0.5+3/2)*l", 1);
    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"3*l", "5.5*l"});
    model.component("comp1").geom("geom1").feature("r4").set("base", "center");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new String[]{"3*l/2", "(-1.75+5.5/2)*l"});

    model.param()
         .remove(new String[]{"coil2_a", "coil2_b", "coil2_h", "coil2_zb", "coil3_a", "coil3_b", "coil3_h", "coil3_zb", "coil4_a", "coil4_b", 
         "coil4_h", "coil4_zb", "coil5_a", "coil5_b", "coil5_h", "coil5_zb", "coil6_a", "coil6_b", "coil6_h", "coil6_zb", 
         "coilb_a", "coilb_b", "coilb_h", "coilb_zb"});
    model.param().remove(new String[]{"b", "a", "Box_H"});
    model.param().remove(new String[]{"H", "Th", "radscaling", "vertscaling", "Tot_H", "NeckdownOut", "NeckdownIn"});
    model.param().set("l", "1");

    model.component("comp1").geom("geom1").run("fin");

    model.param().set("l", "0.5");

    model.component("comp1").geom("geom1").run("fin");

    model.param().set("l", "0.05");

    model.component("comp1").geom("geom1").runPre("fin");

    model.study("std1").create("param", "Parametric");
    model.study("std1").feature("param").set("sweeptype", "sparse");
    model.study("std1").feature("param").setIndex("pname", "coil1_a", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "coil1_a", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "l", 0);
    model.study("std1").feature("param").setIndex("plistarr", "10^{range(0,-0.2,-2)}", 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch().create("p1", "Parametric");
    model.batch("p1").study("std1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "none");
    model.batch("p1").set("pname", new String[]{"l"});
    model.batch("p1").set("plistarr", new String[]{"10^{range(0,-0.2,-2)}"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.sol().create("sol2");
    model.sol("sol2").study("std1");
    model.sol("sol2").label("Parametric Solutions 1");

    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").run("compute");

    model.result().create("pg7", "PlotGroup2D");
    model.result("pg7").label("Magnetic Flux Density (mf)");
    model.result("pg7").set("data", "dset2");
    model.result("pg7").setIndex("looplevel", 11, 0);
    model.result("pg7").set("dataisaxisym", "off");
    model.result("pg7").set("frametype", "spatial");
    model.result("pg7").set("showlegendsmaxmin", true);
    model.result("pg7").feature().create("surf1", "Surface");

    return model;
  }

  public static Model run5(Model model) {
    model.result("pg7").feature("surf1").set("showsolutionparams", "on");
    model.result("pg7").feature("surf1").set("solutionparams", "parent");
    model.result("pg7").feature("surf1").set("colortable", "Prism");
    model.result("pg7").feature("surf1").set("colortabletrans", "nonlinear");
    model.result("pg7").feature("surf1").set("colorcalibration", -0.8);
    model.result("pg7").feature("surf1").set("showsolutionparams", "on");
    model.result("pg7").feature("surf1").set("data", "parent");
    model.result("pg7").feature().create("str1", "Streamline");
    model.result("pg7").feature("str1").set("showsolutionparams", "on");
    model.result("pg7").feature("str1").set("solutionparams", "parent");
    model.result("pg7").feature("str1").set("titletype", "none");
    model.result("pg7").feature("str1").set("posmethod", "uniform");
    model.result("pg7").feature("str1").set("udist", 0.03);
    model.result("pg7").feature("str1").set("maxlen", 0.4);
    model.result("pg7").feature("str1").set("maxsteps", 5000);
    model.result("pg7").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg7").feature("str1").set("inheritcolor", false);
    model.result("pg7").feature("str1").set("showsolutionparams", "on");
    model.result("pg7").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg7").feature("str1").set("showsolutionparams", "on");
    model.result("pg7").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg7").feature("str1").set("showsolutionparams", "on");
    model.result("pg7").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg7").feature("str1").set("showsolutionparams", "on");
    model.result("pg7").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg7").feature("str1").set("data", "parent");
    model.result("pg7").feature("str1").selection().geom("geom1", 1);
    model.result("pg7").feature("str1").selection().set(1, 2, 3, 4, 5, 6, 7, 8);
    model.result("pg7").feature("str1").set("inheritplot", "surf1");
    model.result("pg7").feature("str1").feature().create("col1", "Color");
    model.result("pg7").feature("str1").feature("col1").set("colortable", "PrismDark");
    model.result("pg7").feature("str1").feature("col1").set("colorlegend", false);
    model.result("pg7").feature("str1").feature("col1").set("colortabletrans", "nonlinear");
    model.result("pg7").feature("str1").feature("col1").set("colorcalibration", -0.8);
    model.result("pg7").feature("str1").feature().create("filt1", "Filter");
    model.result("pg7").feature("str1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result("pg7").feature().create("con1", "Contour");
    model.result("pg7").feature("con1").set("showsolutionparams", "on");
    model.result("pg7").feature("con1").set("solutionparams", "parent");
    model.result("pg7").feature("con1").set("expr", "mf.Psi");
    model.result("pg7").feature("con1").set("titletype", "none");
    model.result("pg7").feature("con1").set("number", 10);
    model.result("pg7").feature("con1").set("levelrounding", false);
    model.result("pg7").feature("con1").set("coloring", "uniform");
    model.result("pg7").feature("con1").set("colorlegend", false);
    model.result("pg7").feature("con1").set("color", "custom");
    model.result("pg7").feature("con1")
         .set("customcolor", new double[]{0.3764705955982208, 0.3764705955982208, 0.3764705955982208});
    model.result("pg7").feature("con1").set("resolution", "fine");
    model.result("pg7").feature("con1").set("inheritcolor", false);
    model.result("pg7").feature("con1").set("showsolutionparams", "on");
    model.result("pg7").feature("con1").set("data", "parent");
    model.result("pg7").feature("con1").set("inheritplot", "surf1");
    model.result("pg7").feature("con1").feature().create("filt1", "Filter");
    model.result("pg7").feature("con1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result().dataset().create("rev2", "Revolve2D");
    model.result().dataset("rev2").set("data", "none");
    model.result().dataset("rev2").set("startangle", -90);
    model.result().dataset("rev2").set("revangle", 225);
    model.result().dataset("rev2").set("data", "dset2");
    model.result().create("pg8", "PlotGroup3D");
    model.result("pg8").label("Magnetic Flux Density, Revolved Geometry (mf)");
    model.result("pg8").set("data", "rev2");
    model.result("pg8").setIndex("looplevel", 11, 0);
    model.result("pg8").set("frametype", "spatial");
    model.result("pg8").set("showlegendsmaxmin", true);
    model.result("pg8").feature().create("vol1", "Volume");
    model.result("pg8").feature("vol1").set("showsolutionparams", "on");
    model.result("pg8").feature("vol1").set("solutionparams", "parent");
    model.result("pg8").feature("vol1").set("colortable", "Prism");
    model.result("pg8").feature("vol1").set("colortabletrans", "nonlinear");
    model.result("pg8").feature("vol1").set("colorcalibration", -0.8);
    model.result("pg8").feature("vol1").set("showsolutionparams", "on");
    model.result("pg8").feature("vol1").set("data", "parent");
    model.result("pg8").feature().create("con1", "Contour");
    model.result("pg8").feature("con1").set("showsolutionparams", "on");
    model.result("pg8").feature("con1").set("solutionparams", "parent");
    model.result("pg8").feature("con1").set("expr", "mf.Psi");
    model.result("pg8").feature("con1").set("titletype", "none");
    model.result("pg8").feature("con1").set("number", 10);
    model.result("pg8").feature("con1").set("levelrounding", false);
    model.result("pg8").feature("con1").set("coloring", "uniform");
    model.result("pg8").feature("con1").set("colorlegend", false);
    model.result("pg8").feature("con1").set("color", "custom");
    model.result("pg8").feature("con1")
         .set("customcolor", new double[]{0.3764705955982208, 0.3764705955982208, 0.3764705955982208});
    model.result("pg8").feature("con1").set("resolution", "fine");
    model.result("pg8").feature("con1").set("inheritcolor", false);
    model.result("pg8").feature("con1").set("showsolutionparams", "on");
    model.result("pg8").feature("con1").set("data", "parent");
    model.result("pg8").feature("con1").set("inheritplot", "vol1");
    model.result("pg8").feature("con1").feature().create("filt1", "Filter");
    model.result("pg8").feature("con1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result("pg8").feature("con1").feature("filt1").set("shownodespec", "on");
    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 1, 0);
    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 2, 0);
    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 3, 0);
    model.result("pg7").run();

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "17000[kA]*l");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"l"});
    model.batch("p1").set("plistarr", new String[]{"10^{range(0,-0.2,-2)}"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 1, 0);
    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 6, 0);
    model.result("pg7").run();
    model.result("pg7").setIndex("looplevel", 1, 0);
    model.result("pg7").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").set("data", "dset2");
    model.result("pg5").setIndex("looplevel", 1, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").set("data", "dset2");
    model.result("pg3").setIndex("looplevel", 1, 0);
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l100_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l100_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 2, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 2, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l063_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l063_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 3, 0);
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 3, 0);
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l039_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l039_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 4, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 4, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l025_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l025_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 5, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 5, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l015_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l015_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 6, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 6, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l010_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l010_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 7, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 7, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l006_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l006_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 8, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 8, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l003_r.txt");
    model.result("pg3").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l003_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 9, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 9, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l002_z.txt");
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l002_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 10, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 10, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0015_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0015_z.txt");
    model.result("pg5").run();
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0015_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 11, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 11, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l001_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l001_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);

    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"2.5*l", "3*l"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").setIndex("pos", "2.5*l/2", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new String[]{"2.5*l/2", "0.8*l"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"2.5*l", "4*l"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"l"});
    model.batch("p1").set("plistarr", new String[]{"10^{range(0,-0.2,-2)}"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l1000_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l1000_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 1, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 1, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l1000_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 6, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 6, 0);
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0100_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0100_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 11, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 11, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0010_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0010_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();

    model.study("std1").feature("param").setIndex("plistarr", "10^{range(0,0.2,2)}", 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"l"});
    model.batch("p1").set("plistarr", new String[]{"10^{range(0,0.2,2)}"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();

    model.study("std1").feature("param").setIndex("plistarr", "10^{range(-1,0.25,2)}", 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol2");
    model.batch("p1").set("pname", new String[]{"l"});
    model.batch("p1").set("plistarr", new String[]{"10^{range(-1,0.25,2)}"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 1, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 1, 0);
    model.result("pg5").run();
    model.result("pg5").run();
    model.result("pg3").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-1_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-1_z.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 2, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 2, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-075_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-075_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 1, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-1_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-1_r.txt");
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 1, 0);
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 2, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 3, 0);
    model.result("pg5").setIndex("looplevel", 2, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-075_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-075_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 3, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 3, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-050_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-050_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 4, 0);
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 5, 0);
    model.result("pg3").setIndex("looplevel", 4, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 4, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-025_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l-025_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 5, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 5, 0);
    model.result("pg5").run();
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l0_r.txt");

    return model;
  }

  public static Model run6(Model model) {
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 6, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 6, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l025_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l025_z.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 7, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 7, 0);
    model.result("pg5").run();
    model.result("pg6").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l050_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l050_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 8, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 8, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l075_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l075_z.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 9, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 9, 0);
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l1_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l1_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 10, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 10, 0);
    model.result("pg5").run();
    model.result("pg8").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l125_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l125_z.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 11, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 11, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l150_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l150_r.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 12, 0);
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 12, 0);
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l175_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l175_z.txt");
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg3").setIndex("looplevel", 13, 0);
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").setIndex("looplevel", 13, 0);
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l2_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250811/pencilm3Bfield_20250813_l2_r.txt");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.param().set("coil1_a", "0.30 [m]");
    model.param().set("coil1_b", "0.40 [m]");

    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", "", 0);

    model.param().remove("l");

    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"(coil1_b-coil1_a)", "3*l"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 3, 1);
    model.component("comp1").geom("geom1").run("ls1");
    model.component("comp1").geom("geom1").feature("r1").setIndex("pos", 0.35, 0);
    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{0.35, 0});
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"2.5", "4*l"});
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").feature("r4").set("size", new double[]{2.5, 4});
    model.component("comp1").geom("geom1").feature("r4").set("pos", new int[]{0, 0});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{3, 4});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("pos", new double[]{1.5, 0});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{3, 5});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("p1", "Parametric");
    model.sol("sol1").feature("s1").feature("p1").set("pname", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("plistarr", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("punit", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("sweeptype", "sparse");
    model.sol("sol1").feature("s1").feature("p1").set("preusesol", "no");
    model.sol("sol1").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol1").feature("s1").feature("p1").set("plot", "off");
    model.sol("sol1").feature("s1").feature("p1").set("plotgroup", "pg2");
    model.sol("sol1").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol1").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol1").feature("s1").feature("p1").set("control", "param");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch().remove("p1");

    model.study("std1").feature("param").active(false);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "17000[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.24757909774780273, 0.07394367456436157, 6.89877657145478}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.11817765235900879, 0.08318662643432617, 6.893286503105438}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "13000[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.1305016279220581, 0.11707746982574463, 5.270510112601002}, new double[]{0, 0, 0});
    model.result("pg5").run();
    model.result("pg5").set("data", "dset1");
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg3").set("data", "dset1");
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg6").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250826/m3Bfield_20250826_30cmbore_3mtall_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20250826/m3Bfield_20250826_30cmbore_3mtall_z.txt");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);

    model.label("m3pencilMagnetSim.mph");

    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);

    model.component("comp1").geom("geom1").feature("r4").set("size", new int[]{3, 10});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 6, 1);
    model.component("comp1").geom("geom1").feature("r1").set("size", new String[]{"(coil1_b-coil1_a)*2", "6"});
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"0.35*2", "0"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "2*13000[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg3").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "16*2*13000/5[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "(16/5)*2*13000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251027/m3Bfield_20251027_120cmbore_6mtall_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251027/m3Bfield_20251027_120cmbore_6mtall_r.txt");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"0.55*2", "0"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "(2/1.2)*(16/5)*2*13000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "(16/5)*2*13000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.46478891372680664, 0.2218308448791504, 14.850923132188809}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.5535211563110352, 0.1626758575439453, 14.86621858898252}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "(16/5)*2*13500 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.3464784622192383, 0.014788627624511719, 15.422214387442759}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4943661689758301, 0.3105630874633789, 15.415643323785908}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "(16/5)*2*13200 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{0.19859123229980466, 0.19225358963012695, 15.06193188742016}, new double[]{0, 0, 0});
    model.result("pg8").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251027/m3Bfield_20251027_200cmbore_6mtall_r.txt");

    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"3*1.2", "10"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"3*1.25", "10"});
    model.component("comp1").geom("geom1").feature("r4").set("pos", new String[]{"1.5*1.25", "0"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251027/m3Bfield_20251027_200cmbore_6mtall_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.label("m3pencilMagnetSim.mph");

    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result("pg7").run();
    model.result("pg8").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{2.0233049392700195, 0.17796659469604492, 1.5753525330764124}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.0529661178588867, 0.17796659469604492, 1.5685206895639658}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.0529661178588867, 0.17796659469604492, 1.5685206895639658}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.0529661178588867, 0.17796659469604492, 1.5685206895639658}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.0529661178588867, 0.17796659469604492, 1.5685206895639658}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.55720329284668, -0.4152536392211914, 1.4673773201959832}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.3100290298461914, -2.8898305892944336, 1.2469389766512697}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.8749999999999998, -1.2429380416870115, 1.6867910546224256}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.875, -0.37288141250610346, 1.6143584247862377}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.875, 0.21751403808593747, 1.6098962940028247}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature().duplicate("r5", "r1");
    model.component("comp1").geom("geom1").feature("r5").label("Iron");
    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.1, 6});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new double[]{2.4, 0});
    model.component("comp1").geom("geom1").run("r5");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").material().create("mat5", "Common");
    model.component("comp1").material("mat5").propertyGroup()
         .create("Enu", "Enu", "Young's modulus and Poisson's ratio");
    model.component("comp1").material("mat5").label("Iron");
    model.component("comp1").material("mat5").set("family", "iron");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("relpermeability", new String[]{"4000", "0", "0", "0", "4000", "0", "0", "0", "4000"});
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("electricconductivity", new String[]{"1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]"});
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("thermalexpansioncoefficient", new String[]{"12.2e-6[1/K]", "0", "0", "0", "12.2e-6[1/K]", "0", "0", "0", "12.2e-6[1/K]"});
    model.component("comp1").material("mat5").propertyGroup("def").set("heatcapacity", "440[J/(kg*K)]");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat5").propertyGroup("def").set("density", "7870[kg/m^3]");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("thermalconductivity", new String[]{"76.2[W/(m*K)]", "0", "0", "0", "76.2[W/(m*K)]", "0", "0", "0", "76.2[W/(m*K)]"});
    model.component("comp1").material("mat5").propertyGroup("Enu").set("E", "200[GPa]");
    model.component("comp1").material("mat5").propertyGroup("Enu").set("nu", "0.29");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.613687992095947, -0.5226244926452637, 1.6927105690760946}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.397624969482422, -0.0415724515914917, 1.4988514421558836}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.2016408443450928, -0.11877822875976562, 1.5366777185087828}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.8928172588348389, -0.14847290515899655, 1.6044991904432666}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{3.1518671512603755, -0.12471723556518552, 1.3988785949187927}, new double[]{0, 0, 0});

    model.component("comp1").material("mat5").selection().set(3);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");

    return model;
  }

  public static Model run7(Model model) {
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.30712699890136724, -0.39819002151489263, 16.99858807029858}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.8998870849609375, -0.1990952491760254, 0.18844261169803433}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7256789207458496, -0.2239818572998047, 0.25595730359630814}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.5265836715698242, 0.3733029365539551, 0.3608491364638843}, new double[]{0, 0, 0});
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangedataactive", true);
    model.result("pg6").feature("surf1").set("rangedatamax", 40.6356695214429);
    model.result("pg6").feature("surf1").set("rangecoloractive", true);
    model.result("pg6").feature("surf1").set("rangecolormax", 18);
    model.result().table("evl2")
         .addRow(new double[]{1.8003387451171875, -2.3393664360046387, 1.9404141455460415}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.8252263069152832, -2.538461446762085, 2.1876709368424816}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7754526138305664, -2.787330389022827, 2.5207120099773785}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7754526138305664, -2.787330389022827, 2.5207120099773785}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.1, 6.25});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangedataactive", false);
    model.result().table("evl2")
         .addRow(new double[]{1.8003387451171875, -2.712669610977173, 2.36551998609776}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.6759052276611328, -2.4638009071350098, 2.2699432755291653}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.6510181427001953, -1.0452489852905273, 0.6093240620069992}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.5265836715698244, -0.2737555503845215, 0.33212163519223137}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.5265836715698242, 0.298642635345459, 0.3372919215292336}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.501697063446045, 1.3190045356750488, 0.8817078316517973}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.6759052276611328, 1.44343900680542, 0.9029623314571902}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r5").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{0.8, 0});
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{0.85, 0});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.2833714485168457, -0.7364253997802734, 16.424631436763228}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3071269989013672, 0.26131200790405273, 16.516595326028348}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2833714485168457, 0.451357364654541, 16.49178153987437}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2596158981323242, 0.9264707565307617, 16.354641584981472}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4496612548828125, 0.5938916206359863, 16.476599034451553}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4496612548828125, -0.9264707565307617, 16.37561316437792}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3308830261230469, -1.7341628074645996, 15.682916757927309}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.37839412689208984, -0.38009071350097656, 16.508583692003416}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.37839412689208984, -0.38009071350097656, 16.508583692003416}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.37839412689208984, -0.38009071350097656, 16.508583692003416}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.40215015411376953, 0.5938916206359863, 16.47421409567717}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.6159505844116211, 0.09502267837524414, 16.552108450783624}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "82000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.37839412689208984, -0.5938916206359863, 15.987647196260001}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2596158981323242, -0.09502267837524414, 16.039342400757178}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2596158981323242, 0.3088235855102539, 16.024747004980497}, new double[]{0, 0, 0});
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_150cmbore_6mtall_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_150cmbore_6mtall_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{0.75, 0});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.3851580619812012, -0.7076749801635742, 16.222575538811935}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3603272438049317, -0.16139936447143555, 16.28859602467201}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3603272438049316, 0.18623018264770508, 16.287660569793584}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "80500 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.36032724380493164, -0.18623018264770508, 15.98971556191907}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2361736297607422, 0.08690738677978516, 15.988306362320511}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2361736297607422, 0.6331830024719238, 15.932531298458212}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.18651247024536133, 0.7076749801635742, 15.912860939057044}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "80550 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.21134328842163086, -0.26072216033935547, 15.98969487582881}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3106660842895508, -0.11173820495605469, 15.998773107465606}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3354969024658203, 0.13656902313232422, 15.99915363031204}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3354969024658203, 0.5586905479431152, 15.958763164355162}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3354969024658203, 1.1546273231506348, 15.781820437121203}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2610044479370117, 0.6331830024719238, 15.943938123138816}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2610044479370117, -0.18623018264770508, 15.99527524672166}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.43481969833374023, -0.23589181900024414, 16.0007282727717}, new double[]{0, 0, 0});
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_130cmbore_6mtall_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_130cmbore_6mtall_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{0.65, 0});
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "68000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.2075791358947754, 0.6221718788146973, 13.650299562375896}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2324662208557129, 0.2239818572998047, 13.683011735779402}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.1826920509338379, -0.1493210792541504, 13.684241459503482}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "78000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.2573528289794922, -0.3981900215148926, 15.684080449025945}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2324662208557129, -0.3733029365539551, 15.685320992333075}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79500 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.28223991394042974, -0.4728507995605468, 15.978310716338596}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.28223991394042974, -0.4728507995605468, 15.978310716338596}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2822399139404297, -0.4230771064758301, 15.983794597718454}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2822399139404297, 4.3368086899420177E-19, 16.00312894642888}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.1578054428100586, 0.6719455718994141, 15.949209081003781}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2075791358947754, 0.7466063499450684, 15.93644381055645}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2075791358947754, 0.7466063499450684, 15.93644381055645}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2573528289794922, 0.2488689422607422, 15.995617639685012}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79600 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.3071269989013672, -0.1493210792541504, 16.02182570960267}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3071269989013672, -0.3484163284301758, 16.011598070865677}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.30712699890136724, -0.6470589637756349, 15.977826733797503}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3071269989013672, -1.0203619003295898, 15.896806690879624}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.3320136070251465, -1.1696834564208984, 15.851329949216352}, new double[]{0, 0, 0});
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangecolormax", 18);
    model.result("pg6").feature("surf1").set("rangedataactive", true);
    model.result("pg6").feature("surf1").set("rangedatamax", 15.8);
    model.result("pg6").feature("surf1").set("rangedataactive", false);
    model.result("pg6").feature("surf1").set("rangecoloractive", false);

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79700 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.1826920509338379, -0.07466077804565431, 16.04088226803492}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2324662208557129, -6.938893903907228E-18, 16.042312621924985}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2324662208557129, -6.938893903907228E-18, 16.042312621924985}, new double[]{0, 0, 0});
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_110cmbore_6mtall_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251105/GUT_16T_110cmbore_6mtall_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r1").set("base", "corner");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"1.1/2", "0"});
    model.component("comp1").geom("geom1").run("r1");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"1.1/2", "0"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("size", 0.35, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"1.1/2", "-3"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r6", "r1");
    model.component("comp1").geom("geom1").feature().move("r6", 3);
    model.component("comp1").geom("geom1").feature("r6").label("Coil2");
    model.component("comp1").geom("geom1").feature("r6").set("pos", new String[]{"1.1/2", "-3+1*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r7", "r6");
    model.component("comp1").geom("geom1").feature().move("r7", 4);
    model.component("comp1").geom("geom1").feature("r7").label("Coil3");
    model.component("comp1").geom("geom1").feature("r7").set("pos", new String[]{"1.1/2", "-3+2*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79700/16 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79700 [kA]/16");
    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r8", "r7");
    model.component("comp1").geom("geom1").feature().move("r8", 5);
    model.component("comp1").geom("geom1").feature("r8").set("pos", new String[]{"1.1/2", "-3+4*0.375"});
    model.component("comp1").geom("geom1").feature("r8").label("Coil4");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r8").set("pos", new String[]{"1.1/2", "-3+3*0.375"});
    model.component("comp1").geom("geom1").feature().duplicate("r9", "r8");
    model.component("comp1").geom("geom1").feature().move("r9", 6);
    model.component("comp1").geom("geom1").feature("r9").label("Coil5");
    model.component("comp1").geom("geom1").feature("r9").set("pos", new String[]{"1.1/2", "-3+4*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4, 5, 6);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79700 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r10", "r9");
    model.component("comp1").geom("geom1").feature().move("r10", 7);
    model.component("comp1").geom("geom1").feature("r10").label("Coil6");
    model.component("comp1").geom("geom1").feature("r10").set("pos", new String[]{"1.1/2", "-3+5*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r11", "r10");
    model.component("comp1").geom("geom1").feature().move("r11", 8);
    model.component("comp1").geom("geom1").feature("r11").label("Coil7");
    model.component("comp1").geom("geom1").feature("r11").set("pos", new String[]{"1.1/2", "-3+6*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4, 5, 6, 7, 8);

    model.component("comp1").geom("geom1").feature().duplicate("r12", "r11");
    model.component("comp1").geom("geom1").feature().move("r12", 9);
    model.component("comp1").geom("geom1").feature("r12").label("Coil8");
    model.component("comp1").geom("geom1").feature("r12").set("pos", new String[]{"1.1/2", "-3+7*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r13", "r12");
    model.component("comp1").geom("geom1").feature().move("r13", 10);
    model.component("comp1").geom("geom1").feature("r13").label("Coil9");
    model.component("comp1").geom("geom1").feature("r13").set("pos", new String[]{"1.1/2", "-3+8*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r14", "r13");
    model.component("comp1").geom("geom1").feature().move("r14", 11);
    model.component("comp1").geom("geom1").feature("r14").label("Coil10");
    model.component("comp1").geom("geom1").feature("r14").set("pos", new String[]{"1.1/2", "-3+9*0.375"});

    return model;
  }

  public static Model run8(Model model) {
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r15", "r14");
    model.component("comp1").geom("geom1").feature().move("r15", 12);
    model.component("comp1").geom("geom1").feature("r15").label("Coil11");
    model.component("comp1").geom("geom1").feature("r15").set("pos", new String[]{"1.1/2", "-3+10*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r16", "r15");
    model.component("comp1").geom("geom1").feature().move("r16", 13);
    model.component("comp1").geom("geom1").feature("r16").label("Coil12");
    model.component("comp1").geom("geom1").feature("r16").set("pos", new String[]{"1.1/2", "-3+11*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection().set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r17", "r16");
    model.component("comp1").geom("geom1").feature().move("r17", 14);
    model.component("comp1").geom("geom1").feature("r17").label("Coil13");
    model.component("comp1").geom("geom1").feature("r17").set("pos", new String[]{"1.1/2", "-3+12*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r18", "r17");
    model.component("comp1").geom("geom1").feature().move("r18", 15);
    model.component("comp1").geom("geom1").feature("r18").label("Coil14");
    model.component("comp1").geom("geom1").feature("r18").set("pos", new String[]{"1.1/2", "-3+13*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg5").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r19", "r18");
    model.component("comp1").geom("geom1").feature("r19").label("Coil15");
    model.component("comp1").geom("geom1").feature("r19").set("pos", new String[]{"1.1/2", "-3+14*0.375"});
    model.component("comp1").geom("geom1").feature().duplicate("r20", "r19");
    model.component("comp1").geom("geom1").feature("r20").set("pos", new String[]{"1.1/2", "-3+15*0.375"});
    model.component("comp1").geom("geom1").feature("r20").label("Coil16");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().move("r20", 16);
    model.component("comp1").geom("geom1").feature().move("r19", 16);
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.27617233991622925, 0.892731785774231, 15.951742445584504}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.39551109075546265, 0.7733929753303528, 15.948867795522816}, new double[]{0, 0, 0});
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251106/GUT_16T_110cmbore_segmented_6mtall_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251106/GUT_16T_110cmbore_segmented_6mtall_z.txt");
    model.result().export("plot1").run();
    model.result("pg5").run();

    model.component("comp1").geom("geom1").feature("r5").active(true);
    model.component("comp1").geom("geom1").feature("r5").set("base", "corner");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new int[]{3, 0});
    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.15, 6.25});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new double[]{3, -1.5});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new int[]{3, -3});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-6.25/2"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{1.8274893760681152, 0.42760181427001953, -0.5342897830178887}, new double[]{0, 0, 0});
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.8512449264526365, 0.23755645751953125, 0.5314673437959374}, new double[]{0, 0, 0});
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangedataactive", true);
    model.result("pg6").feature("surf1").set("rangedatamax", 0.17);
    model.result("pg4").run();
    model.result("pg7").run();

    model.component("comp1").material("mat5").selection().set(18);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature().duplicate("r21", "r5");
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{3, 0.15});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new int[]{3, 3});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new int[]{1, 3});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.5, 0.15});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.25});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();

    model.component("comp1").material("mat5").selection().set(18, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg8").run();
    model.result("pg7").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangecoloractive", true);
    model.result("pg6").feature("surf1").set("rangecolormax", 3);
    model.result("pg6").feature("surf1").set("rangedatamax", 3);
    model.result("pg6").feature("surf1").set("rangecolormax", 2);
    model.result("pg6").feature("surf1").set("rangedatamax", 2);

    model.component("comp1").geom("geom1").feature().duplicate("r22", "r21");
    model.component("comp1").geom("geom1").feature("r22").label("Iron 2");
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"1", "-3..25"});
    model.component("comp1").geom("geom1").run("r21");
    model.component("comp1").geom("geom1").feature("r22").set("pos", new double[]{1, -3.25});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").create("con1", "Contour");
    model.result("pg6").feature("con1").set("levelmethod", "levels");
    model.result("pg6").feature("con1").set("levels", "0.170");
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.8308234214782717, -0.3313255310058593, 0.14757944173305534}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.6836557388305662, -0.7534170150756836, 0.21196092987885196}, new double[]{0, 0, 0});

    model.component("comp1").material("mat5").selection().set(18, 19, 20);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg8").run();
    model.result("pg6").run();
    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("surf1").set("rangedataactive", false);
    model.result("pg6").feature("surf1").set("rangecoloractive", false);

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.25, 6.25});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.75, 0.15});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.75, 0.2});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.75, 0.2});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").geom("geom1").feature("r22").set("pos", new double[]{1, -3.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").material("mat5").propertyGroup("def").set("relpermeability", new String[]{"200000"});

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").material("mat5").propertyGroup("def").set("relpermeability", new String[]{"1"});

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").material("mat5").propertyGroup("def").set("relpermeability", new String[]{"200000"});

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", "0.170, 0.1");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", "0.1,0.17");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("colortable", "ThermalLight");

    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.15});
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.75, 0.3});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").active(false);
    model.component("comp1").geom("geom1").feature("r22").active(false);
    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.25, 6.6});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-6.6/2"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature().duplicate("r23", "r5");
    model.component("comp1").geom("geom1").feature("r23").set("pos", new String[]{"1.1/2", "-6.6/2"});
    model.component("comp1").geom("geom1").feature("r23").set("size", new String[]{"(coil1_b-coil1_a)*2", "6.6"});
    model.component("comp1").geom("geom1").feature("r23").setIndex("size", 0.5, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r23").setIndex("size", 0.85, 1);
    model.component("comp1").geom("geom1").run("r23");
    model.component("comp1").geom("geom1").feature("r23").set("pos", new String[]{"1.1/2", "3"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r23").set("pos", new String[]{"1.1/2", "3.1"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").material("mat5").selection().set(18, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg7").run();
    model.result("pg4").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.35, 6.6});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-7/2"});
    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.35, 7});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg4").run();

    model.component("comp1").geom("geom1").feature("r21").active(true);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.25});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.65});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");

    return model;
  }

  public static Model run9(Model model) {
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg4").run();

    model.component("comp1").material("mat5").selection().set(18, 19, 20);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"2.5", "-7/2"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new int[]{1, 7});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg7").run();
    model.result("pg4").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.5, 7});
    model.component("comp1").geom("geom1").run("r5");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-7/2"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", "0.4,0.17");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", "0.45,0.17");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", "0.17, 0.5");
    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r23").active(false);
    model.component("comp1").geom("geom1").feature("r22").active(true);
    model.component("comp1").geom("geom1").run("r22");
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.75, 0.3});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.15});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").material("mat5").selection().set(18, 19, 20);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", ".17");
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg6").create("arws1", "ArrowSurface");
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("surf1").active(false);
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("surf1").active(true);
    model.result("pg6").run();
    model.result("pg6").feature("arws1").active(false);

    model.component("comp1").material().create("mat6", "Common");
    model.component("comp1").material("mat6").propertyGroup().create("BHCurve", "BHCurve", "B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.component("comp1").material("mat6").propertyGroup()
         .create("EffectiveBHCurve", "EffectiveBHCurve", "Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func()
         .create("BHeff", "Interpolation");
    model.component("comp1").material("mat6").label("Soft Iron (Without Losses)");
    model.component("comp1").material("mat6").set("family", "iron");
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").label("B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2"}, 
         {"111408", "2.1"}, 
         {"188487.757", "2.2"}, 
         {"267930.364", "2.3"}, 
         {"347507.836", "2.4"}});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normB", "BH(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normH", "BH_inv(normBin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("Wpm", "BH_prim(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normHin", "Magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normBin", "Magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").label("Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1.000000051691021"}, 
         {"1067.5", "1.4936495124126294"}, 
         {"1705.23", "1.9415328461315795"}, 
         {"2463.11", "2.257765669366018"}, 
         {"3841.67", "2.609980642431287"}, 
         {"5425.74", "2.8664452090837504"}, 
         {"7957.75", "3.1441438097176118"}, 
         {"12298.3", "3.448538051654125"}, 
         {"20462.8", "3.7816711973679054"}, 
         {"32169.6", "4.058345590113038"}, 
         {"61213.4", "4.420646552950275"}, 
         {"111408", "4.721274089545955"}, 
         {"188487.757", "4.972148140718701"}, 
         {"267930.364", "5.145510860855953"}, 
         {"347507.836", "5.245510861426532"}});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("normHeff", "BHeff_inv(normBeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("Wpmeff", "BHeff_prim(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normHeffin", "Effective magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normBeffin", "Effective magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").selection().set(18, 19, 20);
    model.component("comp1").material("mat6").propertyGroup("def").set("relpermeability", new String[]{"100000"});

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.75, 0.4});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.05});
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.75, 0.4});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg7").run();
    model.result("pg8").run();
    model.result("pg6").run();
    model.result("pg4").run();

    model.component("comp1").geom("geom1").feature().duplicate("r24", "r20");
    model.component("comp1").geom("geom1").feature("r24").set("pos", new String[]{"1.1/2", "-3+16*0.375"});
    model.component("comp1").geom("geom1").feature("r24").setIndex("size", 0.55, 1);
    model.component("comp1").geom("geom1").feature().duplicate("r25", "r24");
    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"1.1/2", "-3-1*0.375"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"1.1/2", "-3-1*0.375-0.2"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.4914929866790771, -3.1749305725097656, 3.1575680386407217}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.1178581714630127, -3.3199217319488525, 3.835696819633407}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r24").setIndex("size", 1, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r25").setIndex("size", 1, 1);
    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"1.1/2", "-3-1*0.375-(1-0.35)"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.532331466674805, -3.2370269298553467, 2.193132328372457}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.3389105796813965, -3.2370269298553467, 2.1862927841015933}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.2283844947814941, -3.3475534915924072, 2.5216373597454647}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.2283844947814941, -3.3475534915924072, 2.5216373597454647}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0625951290130615, -3.3751850128173824, 3.3372943849255012}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r22").set("pos", new double[]{1, -3.55});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.15});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").material().remove("mat6");
    model.component("comp1").material().create("mat6", "Common");
    model.component("comp1").material("mat6").propertyGroup().create("BHCurve", "BHCurve", "B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.component("comp1").material("mat6").propertyGroup()
         .create("EffectiveBHCurve", "EffectiveBHCurve", "Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func()
         .create("BHeff", "Interpolation");
    model.component("comp1").material("mat6").label("Soft Iron (Without Losses)");
    model.component("comp1").material("mat6").set("family", "iron");
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").label("B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2"}, 
         {"111408", "2.1"}, 
         {"188487.757", "2.2"}, 
         {"267930.364", "2.3"}, 
         {"347507.836", "2.4"}});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normB", "BH(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normH", "BH_inv(normBin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("Wpm", "BH_prim(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normHin", "Magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normBin", "Magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").label("Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1.000000051691021"}, 
         {"1067.5", "1.4936495124126294"}, 
         {"1705.23", "1.9415328461315795"}, 
         {"2463.11", "2.257765669366018"}, 
         {"3841.67", "2.609980642431287"}, 
         {"5425.74", "2.8664452090837504"}, 
         {"7957.75", "3.1441438097176118"}, 
         {"12298.3", "3.448538051654125"}, 
         {"20462.8", "3.7816711973679054"}, 
         {"32169.6", "4.058345590113038"}, 
         {"61213.4", "4.420646552950275"}, 
         {"111408", "4.721274089545955"}, 
         {"188487.757", "4.972148140718701"}, 
         {"267930.364", "5.145510860855953"}, 
         {"347507.836", "5.245510861426532"}});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("normHeff", "BHeff_inv(normBeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("Wpmeff", "BHeff_prim(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normHeffin", "Effective magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normBeffin", "Effective magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfluxdensity");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.090226650238037, -3.485711574554444, 3.6058373235334544}, new double[]{0, 0, 0});
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.048771619796753, -3.4580798149108887, 3.4378606776676977}, new double[]{0, 0, 0});

    model.component("comp1").material("mat6").selection().set(20, 21, 22);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").material().remove("mat6");
    model.component("comp1").material().create("mat6", "Common");
    model.component("comp1").material("mat6").propertyGroup().create("BHCurve", "BHCurve", "B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.component("comp1").material("mat6").propertyGroup()
         .create("EffectiveBHCurve", "EffectiveBHCurve", "Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func()
         .create("BHeff", "Interpolation");
    model.component("comp1").material("mat6").label("Soft Iron (Without Losses)");
    model.component("comp1").material("mat6").set("family", "iron");
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").label("B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2"}, 
         {"111408", "2.1"}, 
         {"188487.757", "2.2"}, 
         {"267930.364", "2.3"}, 
         {"347507.836", "2.4"}});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("fununit", new String[]{"T"});

    return model;
  }

  public static Model run10(Model model) {
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normB", "BH(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normH", "BH_inv(normBin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("Wpm", "BH_prim(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normHin", "Magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normBin", "Magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").label("Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1.000000051691021"}, 
         {"1067.5", "1.4936495124126294"}, 
         {"1705.23", "1.9415328461315795"}, 
         {"2463.11", "2.257765669366018"}, 
         {"3841.67", "2.609980642431287"}, 
         {"5425.74", "2.8664452090837504"}, 
         {"7957.75", "3.1441438097176118"}, 
         {"12298.3", "3.448538051654125"}, 
         {"20462.8", "3.7816711973679054"}, 
         {"32169.6", "4.058345590113038"}, 
         {"61213.4", "4.420646552950275"}, 
         {"111408", "4.721274089545955"}, 
         {"188487.757", "4.972148140718701"}, 
         {"267930.364", "5.145510860855953"}, 
         {"347507.836", "5.245510861426532"}});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("normHeff", "BHeff_inv(normBeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("Wpmeff", "BHeff_prim(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normHeffin", "Effective magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normBeffin", "Effective magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").selection().set(1, 20, 22);
    model.component("comp1").material().remove("mat6");
    model.component("comp1").material().create("mat6", "Common");
    model.component("comp1").material("mat6").propertyGroup().create("BHCurve", "BHCurve", "B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.component("comp1").material("mat6").propertyGroup()
         .create("EffectiveBHCurve", "EffectiveBHCurve", "Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func()
         .create("BHeff", "Interpolation");
    model.component("comp1").material("mat6").label("Soft Iron (With Losses)");
    model.component("comp1").material("mat6").set("family", "iron");
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("electricconductivity", new String[]{"1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]"});
    model.component("comp1").material("mat6").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").label("B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2"}, 
         {"111408", "2.1"}, 
         {"188487.757", "2.2"}, 
         {"267930.364", "2.3"}, 
         {"347507.836", "2.4"}});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").func("BH").set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normB", "BH(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("normH", "BH_inv(normBin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").set("Wpm", "BH_prim(normHin)");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normHin", "Magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").descr("normBin", "Magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("BHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").label("Effective B-H Curve");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .label("Interpolation 1");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1.000000051691021"}, 
         {"1067.5", "1.4936495124126294"}, 
         {"1705.23", "1.9415328461315795"}, 
         {"2463.11", "2.257765669366018"}, 
         {"3841.67", "2.609980642431287"}, 
         {"5425.74", "2.8664452090837504"}, 
         {"7957.75", "3.1441438097176118"}, 
         {"12298.3", "3.448538051654125"}, 
         {"20462.8", "3.7816711973679054"}, 
         {"32169.6", "4.058345590113038"}, 
         {"61213.4", "4.420646552950275"}, 
         {"111408", "4.721274089545955"}, 
         {"188487.757", "4.972148140718701"}, 
         {"267930.364", "5.145510860855953"}, 
         {"347507.836", "5.245510861426532"}});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("fununit", new String[]{"T"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff").set("defineinv", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("defineprimfun", true);
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("normHeff", "BHeff_inv(normBeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .set("Wpmeff", "BHeff_prim(normHeffin)");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normHeffin", "Effective magnetic field norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve")
         .descr("normBeffin", "Effective magnetic flux density norm");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.component("comp1").material("mat6").propertyGroup("EffectiveBHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat6").selection().set(20, 22);
    model.component("comp1").material().remove("mat5");
    model.component("comp1").material().remove("mat6");
    model.component("comp1").material().create("mat5", "Common");
    model.component("comp1").material("mat5").propertyGroup().create("BHCurve", "BHCurve", "B-H Curve");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.component("comp1").material("mat5").propertyGroup()
         .create("EffectiveBHCurve", "EffectiveBHCurve", "Effective B-H Curve");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func()
         .create("BHeff", "Interpolation");
    model.component("comp1").material("mat5").label("Soft Iron (Without Losses)");
    model.component("comp1").material("mat5").set("family", "iron");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("comp1").material("mat5").propertyGroup("BHCurve").label("B-H Curve");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").label("Interpolation 1");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2"}, 
         {"111408", "2.1"}, 
         {"188487.757", "2.2"}, 
         {"267930.364", "2.3"}, 
         {"347507.836", "2.4"}});
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").set("fununit", new String[]{"T"});
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").set("defineinv", true);
    model.component("comp1").material("mat5").propertyGroup("BHCurve").func("BH").set("defineprimfun", true);
    model.component("comp1").material("mat5").propertyGroup("BHCurve").set("normB", "BH(normHin)");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").set("normH", "BH_inv(normBin)");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").set("Wpm", "BH_prim(normHin)");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").descr("normHin", "Magnetic field norm");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").descr("normBin", "Magnetic flux density norm");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").addInput("magneticfield");
    model.component("comp1").material("mat5").propertyGroup("BHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").label("Effective B-H Curve");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff")
         .label("Interpolation 1");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1.000000051691021"}, 
         {"1067.5", "1.4936495124126294"}, 
         {"1705.23", "1.9415328461315795"}, 
         {"2463.11", "2.257765669366018"}, 
         {"3841.67", "2.609980642431287"}, 
         {"5425.74", "2.8664452090837504"}, 
         {"7957.75", "3.1441438097176118"}, 
         {"12298.3", "3.448538051654125"}, 
         {"20462.8", "3.7816711973679054"}, 
         {"32169.6", "4.058345590113038"}, 
         {"61213.4", "4.420646552950275"}, 
         {"111408", "4.721274089545955"}, 
         {"188487.757", "4.972148140718701"}, 
         {"267930.364", "5.145510860855953"}, 
         {"347507.836", "5.245510861426532"}});
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("fununit", new String[]{"T"});
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("argunit", new String[]{"A/m"});
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff").set("defineinv", true);
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("defineprimfun", true);
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeffin)");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve")
         .set("normHeff", "BHeff_inv(normBeffin)");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve")
         .set("Wpmeff", "BHeff_prim(normHeffin)");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve")
         .descr("normHeffin", "Effective magnetic field norm");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve")
         .descr("normBeffin", "Effective magnetic flux density norm");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.component("comp1").material("mat5").propertyGroup("EffectiveBHCurve").addInput("magneticfluxdensity");
    model.component("comp1").material("mat5").selection().set(20, 21, 22);

    model.component("comp1").physics("mf").feature("al1").set("mur_mat", "from_mat");
    model.component("comp1").physics("mf").feature("al1").set("ConstitutiveRelationBH", "BHCurve");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").material("mat4").selection().set();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").physics("mf").feature("al1").set("ConstitutiveRelationBH", "RelativePermeability");
    model.component("comp1").physics("mf").feature().duplicate("al2", "al1");
    model.component("comp1").physics("mf").feature("al2").selection()
         .set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
    model.component("comp1").physics("mf").feature("al1").set("ConstitutiveRelationBH", "BHCurve");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("al2").selection()
         .set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").physics("mf").feature("al2").selection().set(1);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "100000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "1000000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "797000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "79700 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();

    model.component("comp1").physics("mf").feature("coil1").set("coilGroup", false);
    model.component("comp1").physics("mf").feature("coil1").set("ConductorModel", "Single");
    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").physics("mf").feature("al2").selection().set(1, 2, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");

    return model;
  }

  public static Model run11(Model model) {
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").physics("mf").create("coil2", "Coil", 2);
    model.component("comp1").physics("mf").feature("coil2").selection().set(2, 19);
    model.component("comp1").physics("mf").feature("coil2").set("ICoil", "227500[kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").physics("mf").feature().move("coil2", 5);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").physics("mf").feature("al2").selection().set(1);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.component("comp1").physics("mf").feature("coil2").active(false);
    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.1507582664489746, -3.38684344291687, 2.043339118045305}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0542471408843994, -3.4903032779693604, 2.7244432460956896}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0320625305175781, -3.5198826789855957, 2.9429145438262028}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7863372564315796, -3.386775493621826, 1.969967352496845}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.1412899494171143, -3.224088668823242, 1.7576946527596502}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.355740785598755, -3.275852680206299, 1.5788502750459465}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.3187663555145264, -3.364590644836426, 1.6226172668314507}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0098780393600464, -3.5198826789855957, 3.0290363970999783}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0542471408843994, -3.486604690551758, 2.706568079458421}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0431548357009888, -3.505091905593872, 2.8320888689881007}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.020970344543457, -3.516184091567993, 2.968825518053263}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.0098780393600464, -3.5383687019348145, 3.1210610998502366}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.35});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").set("pos", new double[]{1, 3.1});
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.75, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.75, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.7677874565124512, -3.334307909011841, 1.8811192381335133}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7942309379577637, -3.230769634246826, 1.8702334316938733}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.605769157409668, -3.3115386962890625, 1.9342676883362038}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.471153736114502, -3.392307996749878, 1.987728293522446}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.471153736114502, -3.392307996749878, 1.987728293522446}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.6865386962890625, -3.4461541175842285, 1.9157483226981518}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.9019231796264648, -3.4192309379577637, 1.817943855559181}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{2.3326926231384277, -3.4192309379577637, 1.555903585816034}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r24").set("pos", new String[]{"1.1/2", "-3+16*0.375-x"});
    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"1.1/2", "-3-1*0.375-(1-0.35)-x"});
    model.component("comp1").geom("geom1").feature("r24").set("pos", new String[]{"1.1/2", "-3+16*0.375+x"});

    model.param().set("x", "0 [m]");

    model.study("std1").feature("param").active(true);
    model.study("std1").feature("param").setIndex("pname", "coil1_a", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "coil1_a", 0);
    model.study("std1").feature("param").setIndex("plistarr", "", 0);
    model.study("std1").feature("param").setIndex("punit", "m", 0);
    model.study("std1").feature("param").setIndex("pname", "x", 0);
    model.study("std1").feature("param").setIndex("plistarr", "range(0,0.05,0.25)", 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch().create("p1", "Parametric");
    model.batch("p1").study("std1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "none");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.sol().create("sol16");
    model.sol("sol16").study("std1");
    model.sol("sol16").label("Parametric Solutions 2");

    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").run("compute");

    model.result().create("pg9", "PlotGroup2D");
    model.result("pg9").label("Magnetic Flux Density (mf) 1");
    model.result("pg9").set("data", "dset3");
    model.result("pg9").setIndex("looplevel", 6, 0);
    model.result("pg9").set("dataisaxisym", "off");
    model.result("pg9").set("frametype", "spatial");
    model.result("pg9").set("showlegendsmaxmin", true);
    model.result("pg9").feature().create("surf1", "Surface");
    model.result("pg9").feature("surf1").set("showsolutionparams", "on");
    model.result("pg9").feature("surf1").set("solutionparams", "parent");
    model.result("pg9").feature("surf1").set("colortable", "Prism");
    model.result("pg9").feature("surf1").set("colortabletrans", "nonlinear");
    model.result("pg9").feature("surf1").set("colorcalibration", -0.8);
    model.result("pg9").feature("surf1").set("showsolutionparams", "on");
    model.result("pg9").feature("surf1").set("data", "parent");
    model.result("pg9").feature().create("str1", "Streamline");
    model.result("pg9").feature("str1").set("showsolutionparams", "on");
    model.result("pg9").feature("str1").set("solutionparams", "parent");
    model.result("pg9").feature("str1").set("titletype", "none");
    model.result("pg9").feature("str1").set("posmethod", "uniform");
    model.result("pg9").feature("str1").set("udist", 0.03);
    model.result("pg9").feature("str1").set("maxlen", 0.4);
    model.result("pg9").feature("str1").set("maxsteps", 5000);
    model.result("pg9").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg9").feature("str1").set("inheritcolor", false);
    model.result("pg9").feature("str1").set("showsolutionparams", "on");
    model.result("pg9").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg9").feature("str1").set("showsolutionparams", "on");
    model.result("pg9").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg9").feature("str1").set("showsolutionparams", "on");
    model.result("pg9").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg9").feature("str1").set("showsolutionparams", "on");
    model.result("pg9").feature("str1").set("maxtime", Double.POSITIVE_INFINITY);
    model.result("pg9").feature("str1").set("data", "parent");
    model.result("pg9").feature("str1").selection().geom("geom1", 1);
    model.result("pg9").feature("str1").selection()
         .set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88);
    model.result("pg9").feature("str1").set("inheritplot", "surf1");
    model.result("pg9").feature("str1").feature().create("col1", "Color");
    model.result("pg9").feature("str1").feature("col1").set("colortable", "PrismDark");
    model.result("pg9").feature("str1").feature("col1").set("colorlegend", false);
    model.result("pg9").feature("str1").feature("col1").set("colortabletrans", "nonlinear");
    model.result("pg9").feature("str1").feature("col1").set("colorcalibration", -0.8);
    model.result("pg9").feature("str1").feature().create("filt1", "Filter");
    model.result("pg9").feature("str1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result("pg9").feature().create("con1", "Contour");
    model.result("pg9").feature("con1").set("showsolutionparams", "on");
    model.result("pg9").feature("con1").set("solutionparams", "parent");
    model.result("pg9").feature("con1").set("expr", "mf.Psi");
    model.result("pg9").feature("con1").set("titletype", "none");
    model.result("pg9").feature("con1").set("number", 10);
    model.result("pg9").feature("con1").set("levelrounding", false);
    model.result("pg9").feature("con1").set("coloring", "uniform");
    model.result("pg9").feature("con1").set("colorlegend", false);
    model.result("pg9").feature("con1").set("color", "custom");
    model.result("pg9").feature("con1")
         .set("customcolor", new double[]{0.3764705955982208, 0.3764705955982208, 0.3764705955982208});
    model.result("pg9").feature("con1").set("resolution", "fine");
    model.result("pg9").feature("con1").set("inheritcolor", false);
    model.result("pg9").feature("con1").set("showsolutionparams", "on");
    model.result("pg9").feature("con1").set("data", "parent");
    model.result("pg9").feature("con1").set("inheritplot", "surf1");
    model.result("pg9").feature("con1").feature().create("filt1", "Filter");
    model.result("pg9").feature("con1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result().dataset().create("rev3", "Revolve2D");
    model.result().dataset("rev3").set("data", "none");
    model.result().dataset("rev3").set("startangle", -90);
    model.result().dataset("rev3").set("revangle", 225);
    model.result().dataset("rev3").set("data", "dset3");
    model.result().create("pg10", "PlotGroup3D");
    model.result("pg10").label("Magnetic Flux Density, Revolved Geometry (mf) 1");
    model.result("pg10").set("data", "rev3");
    model.result("pg10").setIndex("looplevel", 6, 0);
    model.result("pg10").set("frametype", "spatial");
    model.result("pg10").set("showlegendsmaxmin", true);
    model.result("pg10").feature().create("vol1", "Volume");
    model.result("pg10").feature("vol1").set("showsolutionparams", "on");
    model.result("pg10").feature("vol1").set("solutionparams", "parent");
    model.result("pg10").feature("vol1").set("colortable", "Prism");
    model.result("pg10").feature("vol1").set("colortabletrans", "nonlinear");
    model.result("pg10").feature("vol1").set("colorcalibration", -0.8);
    model.result("pg10").feature("vol1").set("showsolutionparams", "on");
    model.result("pg10").feature("vol1").set("data", "parent");
    model.result("pg10").feature().create("con1", "Contour");
    model.result("pg10").feature("con1").set("showsolutionparams", "on");
    model.result("pg10").feature("con1").set("solutionparams", "parent");
    model.result("pg10").feature("con1").set("expr", "mf.Psi");
    model.result("pg10").feature("con1").set("titletype", "none");
    model.result("pg10").feature("con1").set("number", 10);
    model.result("pg10").feature("con1").set("levelrounding", false);
    model.result("pg10").feature("con1").set("coloring", "uniform");
    model.result("pg10").feature("con1").set("colorlegend", false);
    model.result("pg10").feature("con1").set("color", "custom");
    model.result("pg10").feature("con1")
         .set("customcolor", new double[]{0.3764705955982208, 0.3764705955982208, 0.3764705955982208});
    model.result("pg10").feature("con1").set("resolution", "fine");
    model.result("pg10").feature("con1").set("inheritcolor", false);
    model.result("pg10").feature("con1").set("showsolutionparams", "on");
    model.result("pg10").feature("con1").set("data", "parent");
    model.result("pg10").feature("con1").set("inheritplot", "vol1");
    model.result("pg10").feature("con1").feature().create("filt1", "Filter");
    model.result("pg10").feature("con1").feature("filt1").set("expr", "!isScalingSystemDomain");
    model.result("pg10").feature("con1").feature("filt1").set("shownodespec", "on");
    model.result("pg9").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").set("data", "dset3");
    model.result("pg6").setIndex("looplevel", 1, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"1.1/2", "-3-1*0.375-(1-0.35)"});
    model.component("comp1").geom("geom1").feature("r24").set("pos", new String[]{"1.1/2", "-3+16*0.375"});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new String[]{"1", "3.1+x"});
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"1", "-3.55-x"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 2, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 6, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 1, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").run();

    model.label("m3pencilMagnetSim_iron.mph");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg5").run();
    model.result().table("evl2")
         .addRow(new double[]{1.6326923370361328, -0.3230772018432617, -0.0424236091861858}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.4173073768615723, -1.4538462162017822, -0.07494707124933996}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{1.7134618759155273, -2.2615387439727783, -0.10228319743759681}, new double[]{0, 0, 0});
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_16T_110cmbore_segmented_iron_6mtall_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_16T_110cmbore_segmented_iron_6mtall_r.txt");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.5, 7.5});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-7.5/2"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    return model;
  }

  public static Model run12(Model model) {

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 6, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.85, 0.45});
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"0.9", "-3.55-x"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("pos", new String[]{"0.9", "3.1+x"});
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.85, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 4, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 1, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.95, 0.45});
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.95, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 5, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 1, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r24").setIndex("size", 1.25, 1);
    model.component("comp1").geom("geom1").feature("r25").setIndex("size", 1.25, 1);
    model.component("comp1").geom("geom1").feature("r25")
         .set("pos", new String[]{"1.1/2", "-3-1*0.375-(1-0.35)-0.25"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{2, 0.45});
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{2, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", ".175");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", ".1");
    model.result("pg6").run();
    model.result("pg6").feature("con1").set("levels", ".17");
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 4, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 5, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.5, 7.75});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-7.75/2"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_16T_110cmbore_segmented_iron_6mtall_v2_r.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_16T_110cmbore_segmented_iron_6mtall_v2_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_16T_110cmbore_segmented_iron_6mtall_v2_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.3801283836364746, -0.4230771064758301, 11.493709161963688}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.21089744567871094, -0.39487171173095703, 11.508765565192014}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.013462066650390625, 0.874359130859375, 11.505296700524168}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.041666507720947266, 1.269230842590332, 11.498343311787998}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.26730775833129883, 2.002563953399658, 11.47483215129708}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.component("comp1").physics("mf").feature("al1").selection()
         .set(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22);
    model.component("comp1").physics("mf").feature("al2").selection().set(1, 2, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil2").active(true);
    model.component("comp1").physics("mf").feature("al2").selection().set(1);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.component("comp1").physics("mf").feature("coil2").active(false);
    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "85700 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.21089744567871094, 0.19743585586547852, 12.378276625518598}, new double[]{0, 0, 0});
    model.result("pg6").setIndex("looplevel", 4, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 6, 0);
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 5, 0);
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r5").set("size", new double[]{0.5, 8});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-8/2"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();

    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");

    return model;
  }

  public static Model run13(Model model) {
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_12T_110cmbore_segmented_iron_8mtall_v3_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_12T_110cmbore_segmented_iron_8mtall_v3_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "90000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.1826925277709961, -0.7897434234619141, 12.984360291450042}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.21089744567871094, -1.0999999046325684, 12.977943089528182}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.21089744567871094, -1.4948716163635254, 12.964967183771176}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.21089744567871094, -2.0025641918182373, 12.940448609093316}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2108974456787109, -2.3410255908966064, 12.909487983802741}, new double[]{0, 0, 0});
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_12T_110cmbore_segmented_iron_8mtall_v3_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.2391018867492676, 0.47948741912841797, 12.990128482642245}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.23910188674926758, 0.08461570739746094, 12.991721169379428}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.24967937171459198, 0.40897440910339355, 12.987789632055598}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "86000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.1826925277709961, 1.6076922416687012, 12.390208084172716}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.1826925277709961, 1.3820514678955078, 12.397811147650124}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "83000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.29551172256469727, 2.4397449493408203, 11.914278928711902}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.18269115686416626, 2.101283073425293, 11.936360939868518}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.18269115686416626, 1.8756420612335207, 11.948043770603835}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.027562916278839108, 1.7839753627777102, 11.953513698907901}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "83500 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.19679373502731323, 2.6653859615325928, 11.945710746100644}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2673066258430481, 2.3833346366882324, 11.98785836908706}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.23910146951675418, 2.1929497718811035, 11.998887540170273}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.26025527715682983, 1.995513916015625, 12.016780110981324}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").run();

    model.component("comp1").mesh("mesh1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20251109/GUT_12T_110cmbore_segmented_iron_8mtall_v3_z.txt");
    model.result().export("plot1").run();
    model.result("pg6").run();
    model.result("pg4").run();

    model.label("GUTpencilMagnetSim_iron_WorkingModel.mph");

    model.result("pg4").run();

    model.component("comp1").geom("geom1").run();

    model.result("pg6").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r7").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r7").active(true);
    model.component("comp1").geom("geom1").feature("r19").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r19").active(true);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r20").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").feature("r19").active(false);
    model.component("comp1").geom("geom1").feature("r20").set("pos", new String[]{"1.1/2", "2.275"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r18").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").feature("r18").set("pos", new String[]{"1.1/2", "2.275-1*0.75"});
    model.component("comp1").geom("geom1").feature("r17").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r16").set("pos", new String[]{"1.1/2", "2.275-1*0.75"});
    model.component("comp1").geom("geom1").feature("r16").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").feature("r16").set("pos", new String[]{"1.1/2", "2.275-2*0.75"});
    model.component("comp1").geom("geom1").feature("r15").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r13").active(false);
    model.component("comp1").geom("geom1").feature("r14").set("pos", new String[]{"1.1/2", "2.275-3*0.75"});
    model.component("comp1").geom("geom1").feature("r14").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r9").active(false);
    model.component("comp1").geom("geom1").feature("r11").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r12").set("pos", new String[]{"1.1/2", "2.275-4*0.75"});
    model.component("comp1").geom("geom1").feature("r12").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").feature("r10").set("pos", new String[]{"1.1/2", "2.275-5*0.75"});
    model.component("comp1").geom("geom1").feature("r10").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r7").active(false);
    model.component("comp1").geom("geom1").feature("r8").set("pos", new String[]{"1.1/2", "2.275-6*0.75"});
    model.component("comp1").geom("geom1").feature("r8").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").set("pos", new String[]{"1.1/2", "2.275-7*0.75"});
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.7, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r1").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{3.5446228981018066, 3.1931138038635254, 0.0026847533297204578}, new double[]{0, 0, 0});
    model.result("pg10").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_r.txt");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r6").setIndex("pos", "0.55-0.1", 0);
    model.component("comp1").geom("geom1").feature("r6").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("pos", 0.55, 0);
    model.component("comp1").geom("geom1").feature("r6").set("size", new String[]{"(coil1_b-coil1_a)*2", "0.7"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg3").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result("pg5").run();

    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0,0.05,0.25)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg6").run();
    model.result("pg6").set("data", "dset2");
    model.result("pg6").run();
    model.result("pg6").set("data", "dset3");
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").setIndex("looplevel", 1, 0);
    model.result("pg6").run();

    model.study("std1").feature("param").setIndex("plistarr", "range(0)", 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"range(0)"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");

    model.result("pg3").run();
    model.result("pg3").set("data", "dset3");
    model.result("pg3").run();
    model.result("pg3").run();

    model.study("std1").feature("param").setIndex("plistarr", 0, 0);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");

    return model;
  }

  public static Model run14(Model model) {
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r6").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r6").setIndex("pos", "0.55-0.1", 0);
    model.component("comp1").geom("geom1").feature("r20").set("pos", new String[]{"1.1/2-0.1", "2.275"});
    model.component("comp1").geom("geom1").feature("r20").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r24")
         .set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "1.25"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r24").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r25").set("base", "corner");
    model.component("comp1").geom("geom1").feature("r25").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").feature("r25")
         .set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "1.25"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r18").set("size", new String[]{"(coil1_b-coil1_a)*2-0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r18").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r18").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r13").set("pos", new String[]{"1.1/2-0.1", "-3+8*0.375"});
    model.component("comp1").geom("geom1").feature("r13")
         .set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.35"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r13").set("size", new String[]{"(coil1_b-coil1_a)*2", "0.35"});
    model.component("comp1").geom("geom1").feature("r13").set("pos", new String[]{"1.1/2", "-3+8*0.375"});
    model.component("comp1").geom("geom1").feature("r14").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r14").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r16").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").feature("r16").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r12").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r12").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r10").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r10").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").feature("r8").set("size", new String[]{"(coil1_b-coil1_a)*2+0.1", "0.7"});
    model.component("comp1").geom("geom1").feature("r8").setIndex("pos", "1.1/2-0.1", 0);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "123500 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.21448850631713867, 0.4772725105285645, 17.911568542921493}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.2966349124908447, 1.0903849601745605, 18.007092791839185}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.256250262260437, 1.1105772256851196, 17.96533196202324}, new double[]{0, 0, 0});
    model.result("pg5").run();
    model.result("pg6").run();
    model.result("pg5").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_r.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1, 0.45});
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"2", "-8/2"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{1.1, 0.45});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"0.9-0.05", "-3.55-x"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1.1, 0.45});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new String[]{"0.85", "3.1+x"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();

    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{1, 0.45});
    model.component("comp1").geom("geom1").feature("r21").set("pos", new String[]{"0.9", "3.1+x"});
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r21").set("size", new double[]{2, 0.45});
    model.component("comp1").geom("geom1").feature("r22").set("size", new double[]{2, 0.45});
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"0.9", "-3.55-x"});
    model.component("comp1").geom("geom1").feature("r5").set("pos", new String[]{"3", "-8/2"});
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{1.1649041175842285, -1.2653847932815552, 0.07764001338065862}, new double[]{0, 0, 0});
    model.result("pg4").run();
    model.result("pg6").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result("pg6").run();
    model.result("pg5").feature("con1").active(false);
    model.result("pg5").feature("con1").active(false);
    model.result().export("plot1").set("plotgroup", "pg6");
    model.result().export("plot1").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_norm.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_12T_110cmbore_fewersegments_iron_norm.txt");
    model.result().export("plot1").set("plotgroup", "pg6");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_norm.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_z.txt");
    model.result().export("plot1").run();
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1").set("plotgroup", "pg6");
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260211/GUT_18T_110cmbore_fewersegments_iron_norm.txt");
    model.result().export("plot1").run();

    model.label("GUTpencilMagnetSim_iron_fewersegments_18T.mph");

    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);

    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.705, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", "0.70", 1);
    model.component("comp1").geom("geom1").run("r6");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.705, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", "0.70", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.75, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.725, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r8").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").feature("r10").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").feature("r12").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r14").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r16").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").feature("r18").setIndex("size", 0.72, 1);
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.4057604670524597, 1.9318482875823975, 18.146428118564565}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4057604968547821, 1.9247967004776, 18.149083422554774}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "123000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.4057600796222687, 0.4158230423927307, 18.142984737237846}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4057600796222687, 0.3805665969848633, 18.143310914624806}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4410165250301361, 0.42287433147430425, 18.172787399867595}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "121000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.3916575014591217, 0.4087717533111572, 17.83460738614477}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4339652359485626, 0.4158230423927307, 17.8756260301517}, new double[]{0, 0, 0});

    model.component("comp1").physics("mf").feature("coil1").set("ICoil", "122000 [kA]");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);

    return model;
  }

  public static Model run15(Model model) {
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result().table("evl2")
         .addRow(new double[]{0.41986265778541565, 0.38761788606643677, 18.01244017012696}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4339652359485627, 0.4087717533111573, 18.02392275570624}, new double[]{0, 0, 0});
    model.result().table("evl2")
         .addRow(new double[]{0.4339652359485627, 0.4087717533111573, 18.02392275570624}, new double[]{0, 0, 0});

    model.component("comp1").geom("geom1").feature("r25").setIndex("size", 1.27, 1);
    model.component("comp1").geom("geom1").runPre("fin");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260303/GUT_18T_fewersegments_iron_r.txt");
    model.result().export("plot1").set("plotgroup", "pg3");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260303/GUT_18T_fewersegments_iron_z.txt");
    model.result().export("plot1").set("plotgroup", "pg5");
    model.result().export("plot1").run();
    model.result().export("plot1")
         .set("filename", "/home/nrapidis/Documents/NicholasCOMSOL/coreSims/20260303/GUT_18T_fewersegments_iron_norm.txt");
    model.result().export("plot1").set("plotgroup", "pg6");
    model.result().export("plot1").run();

    model.component("comp1").geom("geom1").feature("r25").setIndex("size", "1.27+0.7", 1);
    model.component("comp1").geom("geom1").feature("r25").setIndex("size", "1.27+0.74", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r25").setIndex("size", "1.27+0.75", 1);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").feature("r6").active(false);
    model.component("comp1").geom("geom1").runPre("fin");
    model.component("comp1").geom("geom1").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1").run();

    model.label("20260303_MagnetSim_18T_GUT.mph");

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg7").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg9").run();
    model.result("pg5").run();
    model.result("pg6").run();
    model.result("pg4").run();
    model.result("pg9").run();
    model.result("pg8").run();
    model.result("pg7").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1")
         .set("filename", "/home/penelope_quassolo/Downloads/DMRadio-CoreGUT-20260727T171715Z-1-001/DMRadio-CoreGUT/GUT_18T_fewersegments_iron_z.txt");
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1").run();

    model.label("20260303_MagnetSim_18T_GUT.mph");

    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg3").run();

    model.sol("sol1").study("std1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").set("control", "stat");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 100);
    model.sol("sol1").feature("s1").feature("fc1").set("ntolfact", 1);
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.batch("p1").feature().remove("so1");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("store", "on");
    model.batch("p1").feature("so1").set("clear", "on");
    model.batch("p1").feature("so1").set("psol", "sol16");
    model.batch("p1").set("pname", new String[]{"x"});
    model.batch("p1").set("plistarr", new String[]{"0"});
    model.batch("p1").set("sweeptype", "sparse");
    model.batch("p1").set("probesel", "all");
    model.batch("p1").set("probes", new String[]{});
    model.batch("p1").set("plot", "off");
    model.batch("p1").set("err", "on");
    model.batch("p1").attach("std1");
    model.batch("p1").set("control", "param");
    model.batch("p1").run("compute");

    model.result("pg3").run();
    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);
    model.result().export("plot1").run();

    model.label("20260303_MagnetSim_18T_GUT.mph");

    model.result("pg6").feature("con1").active(false);
    model.result("pg6").feature("arws1").active(false);
    model.result("pg6").feature("con1").active(true);
    model.result("pg6").feature("arws1").active(false);

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    model = run2(model);
    model = run3(model);
    model = run4(model);
    model = run5(model);
    model = run6(model);
    model = run7(model);
    model = run8(model);
    model = run9(model);
    model = run10(model);
    model = run11(model);
    model = run12(model);
    model = run13(model);
    model = run14(model);
    run15(model);
  }

}
