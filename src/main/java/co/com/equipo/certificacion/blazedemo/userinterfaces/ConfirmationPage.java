package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Página de confirmación (/confirmation.php). */
public final class ConfirmationPage {
    public static final Target MENSAJE = Target.the("mensaje de confirmación")
            .locatedBy("css:div.container h1");
    /** Valor de la tabla de confirmación según su etiqueta (Id, Status, Amount, Card Number, ...). */
    public static final Target DATO_CONFIRMACION = Target.the("dato «{0}» de la confirmación")
            .locatedBy("//table/tbody/tr[td[1][normalize-space()=\"{0}\"]]/td[2]");

    private ConfirmationPage() {
    }
}
