package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.BTN_ELEGIR_VUELO_FILA;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/** Hace clic en el botón "Choose This Flight" de una fila concreta de la tabla de resultados. */
public class ElegirVueloDeLaFila implements Interaction {

    private final int fila;

    public ElegirVueloDeLaFila(int fila) {
        this.fila = fila;
    }

    public static ElegirVueloDeLaFila numero(int fila) {
        return instrumented(ElegirVueloDeLaFila.class, fila);
    }

    @Override
    @Step("{0} hace clic en 'Choose This Flight' de la fila #fila")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(HacerClicEn.elElemento(BTN_ELEGIR_VUELO_FILA.of(String.valueOf(fila))));
    }
}
