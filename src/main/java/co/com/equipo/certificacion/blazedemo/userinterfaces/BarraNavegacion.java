package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Barra superior de navegación (presente en todas las páginas del sitio). */
public final class BarraNavegacion {
    /** Enlace de la barra por su texto visible: "Travel The World", "home", "Login", "Register". */
    public static final Target ENLACE = Target.the("enlace \"{0}\" de la barra superior")
            .locatedBy("(//a[normalize-space()=\"{0}\"])[1]");

    private BarraNavegacion() {
    }
}
