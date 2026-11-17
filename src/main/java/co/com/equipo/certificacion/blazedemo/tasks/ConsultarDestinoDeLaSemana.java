package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LINK_DESTINO_SEMANA;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.VacationPage.DESTINO;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Abre la página "Destination of the week" desde la página principal. */
public class ConsultarDestinoDeLaSemana implements Task {

    public static ConsultarDestinoDeLaSemana enLaPaginaPrincipal() {
        return instrumented(ConsultarDestinoDeLaSemana.class);
    }

    @Override
    @Step("{0} consulta el destino de la semana")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HacerClicEn.elElemento(LINK_DESTINO_SEMANA),
                WaitUntil.the(DESTINO, isVisible()).forNoMoreThan(15).seconds()
        );
    }
}
