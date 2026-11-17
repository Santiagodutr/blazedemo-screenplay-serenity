package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.BTN_BUSCAR_VUELOS;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.TITULO_RESULTADOS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Busca vuelos sin tocar las listas: usa las ciudades que la página trae seleccionadas. */
public class BuscarVuelosPorDefecto implements Task {

    public static BuscarVuelosPorDefecto conLasCiudadesSeleccionadas() {
        return instrumented(BuscarVuelosPorDefecto.class);
    }

    @Override
    @Step("{0} busca vuelos con las ciudades seleccionadas por defecto")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HacerClicEn.elElemento(BTN_BUSCAR_VUELOS),
                WaitUntil.the(TITULO_RESULTADOS, isVisible()).forNoMoreThan(15).seconds()
        );
    }
}
