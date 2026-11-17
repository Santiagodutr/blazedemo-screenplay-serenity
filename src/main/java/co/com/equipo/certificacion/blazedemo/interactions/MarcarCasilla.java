package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/** Deja una casilla de verificación marcada (solo hace clic si aún no lo está). */
public class MarcarCasilla implements Interaction {

    private final Target casilla;

    public MarcarCasilla(Target casilla) {
        this.casilla = casilla;
    }

    public static MarcarCasilla llamada(Target casilla) {
        return instrumented(MarcarCasilla.class, casilla);
    }

    @Override
    @Step("{0} marca #casilla")
    public <T extends Actor> void performAs(T actor) {
        if (!casilla.resolveFor(actor).isSelected()) {
            actor.attemptsTo(Click.on(casilla));
        }
        actor.attemptsTo(Pausa.configurada());
    }
}
