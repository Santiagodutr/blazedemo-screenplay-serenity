package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Página principal (/): búsqueda de vuelos. */
public final class HomePage {
    public static final Target LISTA_ORIGEN = Target.the("lista de ciudad de origen")
            .locatedBy("css:select[name=\"fromPort\"]");
    public static final Target LISTA_DESTINO = Target.the("lista de ciudad de destino")
            .locatedBy("css:select[name=\"toPort\"]");
    public static final Target BTN_BUSCAR_VUELOS = Target.the("botón Find Flights")
            .locatedBy("css:input[type=\"submit\"][value=\"Find Flights\"]");
    public static final Target LINK_DESTINO_SEMANA = Target.the("enlace destino de la semana")
            .locatedBy("css:a[href=\"vacation.html\"]");

    private HomePage() {
    }
}
