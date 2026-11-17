package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/** Espera explícitamente a que un elemento sea clicable y hace clic en él. */
public class HacerClicEn implements Interaction {

    private final Target elemento;

    public HacerClicEn(Target elemento) {
        this.elemento = elemento;
    }

    public static HacerClicEn elElemento(Target elemento) {
        return instrumented(HacerClicEn.class, elemento);
    }

    @Override
    @Step("{0} hace clic en #elemento")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(elemento, isClickable()).forNoMoreThan(10).seconds(),
                Click.on(elemento),
                Pausa.configurada()
        );
    }
}
