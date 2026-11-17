package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.CuentaPage.LINK_RECUPERAR_CONTRASENA;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.CuentaPage.TITULO_FORMULARIO;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Desde el inicio de sesión, abre el formulario para recuperar la contraseña (no lo envía). */
public class RecuperarContrasena implements Task {

    public static RecuperarContrasena desdeElInicioDeSesion() {
        return instrumented(RecuperarContrasena.class);
    }

    @Override
    @Step("{0} elige la opción de recuperar la contraseña")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                HacerClicEn.elElemento(LINK_RECUPERAR_CONTRASENA),
                WaitUntil.the(TITULO_FORMULARIO, isVisible()).forNoMoreThan(15).seconds()
        );
    }
}
