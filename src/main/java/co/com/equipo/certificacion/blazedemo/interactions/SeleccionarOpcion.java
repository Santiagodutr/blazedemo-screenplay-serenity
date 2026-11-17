package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isEnabled;

/** Selecciona una opción por su texto visible en una lista desplegable (ciudad, tipo de tarjeta). */
public class SeleccionarOpcion implements Interaction {

    private final String opcion;
    private final Target lista;

    public SeleccionarOpcion(String opcion, Target lista) {
        this.opcion = opcion;
        this.lista = lista;
    }

    public static SeleccionarOpcion llamada(String opcion, Target lista) {
        return instrumented(SeleccionarOpcion.class, opcion, lista);
    }

    @Override
    @Step("{0} selecciona '#opcion' en #lista")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(lista, isEnabled()).forNoMoreThan(10).seconds(),
                SelectFromOptions.byVisibleText(opcion).from(lista),
                Pausa.configurada()
        );
    }
}
