package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.questions.DatoDeLaConfirmacion;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static co.com.equipo.certificacion.blazedemo.utils.Constantes.NUMERO_COMPRA;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/** El viajero anota el número (Id) de la compra que acaba de confirmar. */
public class AnotarNumeroDeCompra implements Task {

    public static AnotarNumeroDeCompra deLaConfirmacion() {
        return instrumented(AnotarNumeroDeCompra.class);
    }

    @Override
    @Step("{0} anota el número de la compra")
    public <T extends Actor> void performAs(T actor) {
        actor.remember(NUMERO_COMPRA, DatoDeLaConfirmacion.llamado("Id").answeredBy(actor));
    }
}
