package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import co.com.equipo.certificacion.blazedemo.interactions.Pausa;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.BarraNavegacion.ENLACE;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/** Navega usando un enlace de la barra superior, identificado por su texto visible. */
public class NavegarPorLaBarra implements Task {

    private final String enlace;

    public NavegarPorLaBarra(String enlace) {
        this.enlace = enlace;
    }

    public static NavegarPorLaBarra alEnlace(String enlace) {
        return instrumented(NavegarPorLaBarra.class, enlace);
    }

    @Override
    @Step("{0} navega con el enlace '#enlace' de la barra superior")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HacerClicEn.elElemento(ENLACE.of(enlace)),
                Pausa.configurada()
        );
    }
}
