package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Página de resultados (/reserve.php): tabla de vuelos. */
public final class ReservePage {
    public static final Target TITULO_RESULTADOS = Target.the("título de resultados")
            .locatedBy("css:div.container h3");
    public static final Target COLUMNA_NUMERO = Target.the("columna Flight #")
            .locatedBy("//table[@class=\"table\"]/tbody/tr/td[2]");
    public static final Target COLUMNA_AEROLINEA = Target.the("columna Airline")
            .locatedBy("//table[@class=\"table\"]/tbody/tr/td[3]");
    public static final Target COLUMNA_PRECIO = Target.the("columna Price")
            .locatedBy("//table[@class=\"table\"]/tbody/tr/td[6]");
    public static final Target BTN_ELEGIR_VUELO_FILA = Target.the("botón Choose This Flight de la fila {0}")
            .locatedBy("(//input[@value=\"Choose This Flight\"])[{0}]");

    private ReservePage() {
    }
}
