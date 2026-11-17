package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ConfirmationPage.DATO_CONFIRMACION;

/** Valor de una fila de la tabla de confirmación (Id, Status, Amount, Card Number, Auth Code...). */
public class DatoDeLaConfirmacion implements Question<String> {

    private final String etiqueta;

    public DatoDeLaConfirmacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public static DatoDeLaConfirmacion llamado(String etiqueta) {
        return new DatoDeLaConfirmacion(etiqueta);
    }

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(DATO_CONFIRMACION.of(etiqueta)).answeredBy(actor).replaceAll("\\s+", " ").trim();
    }

    @Override
    public String toString() {
        return "el dato '" + etiqueta + "' de la confirmación";
    }
}
