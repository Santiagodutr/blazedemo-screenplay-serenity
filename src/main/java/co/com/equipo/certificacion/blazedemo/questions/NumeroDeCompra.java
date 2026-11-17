package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.Question;

import static co.com.equipo.certificacion.blazedemo.utils.Constantes.NUMERO_COMPRA;

/** Números (Id) de compra: el que el actor anotó y el que muestra la confirmación actual. */
public final class NumeroDeCompra {

    private NumeroDeCompra() {
    }

    public static Question<String> anotado() {
        return Question.about("el número de compra anotado").answeredBy(actor -> actor.recall(NUMERO_COMPRA));
    }

    public static Question<String> actual() {
        return Question.about("el número de la compra actual")
                .answeredBy(actor -> DatoDeLaConfirmacion.llamado("Id").answeredBy(actor));
    }
}
