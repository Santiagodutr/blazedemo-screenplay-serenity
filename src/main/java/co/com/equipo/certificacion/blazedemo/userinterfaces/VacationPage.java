package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Página "Destination of the week" (/vacation.html). */
public final class VacationPage {
    public static final Target DESTINO = Target.the("destino de la semana")
            .locatedBy("//div[@class=\"container\"][contains(normalize-space(),\"Destination of the week\")]");

    private VacationPage() {
    }
}
