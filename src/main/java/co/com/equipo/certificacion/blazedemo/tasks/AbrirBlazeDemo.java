package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.Pausa;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.BTN_BUSCAR_VUELOS;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.PAGINA_BLAZEDEMO;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Abre la página principal de BlazeDemo (URL configurada en serenity.conf → pages.blazedemo). */
public class AbrirBlazeDemo implements Task {

    public static AbrirBlazeDemo paginaPrincipal() {
        return instrumented(AbrirBlazeDemo.class);
    }

    @Override
    @Step("{0} abre la página principal de BlazeDemo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn().thePageNamed(PAGINA_BLAZEDEMO),
                WaitUntil.the(BTN_BUSCAR_VUELOS, isVisible()).forNoMoreThan(15).seconds(),
                Pausa.configurada()
        );
    }
}
