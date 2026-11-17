package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.Pausa;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ConfirmationPage.MENSAJE;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.CuentaPage.TITULO_FORMULARIO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.TITULO_RESULTADOS;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.PAGINA_CONFIRMACION;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.PAGINA_LOGIN;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.PAGINA_RESULTADOS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Abre una página escribiendo su dirección, sin pasar por el flujo normal (como haría un usuario
 * con un enlace guardado). Las URL están en serenity.conf.
 */
public class AbrirPaginaDirecta implements Task {

    private final String descripcion;
    private final String pagina;
    private final Target elementoEsperado;

    public AbrirPaginaDirecta(String descripcion, String pagina, Target elementoEsperado) {
        this.descripcion = descripcion;
        this.pagina = pagina;
        this.elementoEsperado = elementoEsperado;
    }

    public static AbrirPaginaDirecta deResultados() {
        return instrumented(AbrirPaginaDirecta.class, "resultados", PAGINA_RESULTADOS, TITULO_RESULTADOS);
    }

    public static AbrirPaginaDirecta deConfirmacion() {
        return instrumented(AbrirPaginaDirecta.class, "confirmación", PAGINA_CONFIRMACION, MENSAJE);
    }

    public static AbrirPaginaDirecta deInicioDeSesion() {
        return instrumented(AbrirPaginaDirecta.class, "inicio de sesión", PAGINA_LOGIN, TITULO_FORMULARIO);
    }

    @Override
    @Step("{0} abre directamente la página de #descripcion")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn().thePageNamed(pagina),
                WaitUntil.the(elementoEsperado, isVisible()).forNoMoreThan(15).seconds(),
                Pausa.configurada()
        );
    }
}
