package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Simula a un usuario que manipula el HTML (DevTools) agregando una opción que la interfaz no ofrece.
 * Sirve para probar si el servidor valida los datos o si confía solo en la interfaz.
 */
public class AgregarOpcionALista implements Interaction {

    private final String opcion;
    private final Target lista;

    public AgregarOpcionALista(String opcion, Target lista) {
        this.opcion = opcion;
        this.lista = lista;
    }

    public static AgregarOpcionALista llamada(String opcion, Target lista) {
        return instrumented(AgregarOpcionALista.class, opcion, lista);
    }

    @Override
    @Step("{0} agrega por fuera de la interfaz la opción '#opcion' a #lista")
    public <T extends Actor> void performAs(T actor) {
        WebElement select = lista.resolveFor(actor).getElement();
        ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver()).executeScript(
                "var o=document.createElement('option'); o.value=arguments[1]; o.text=arguments[1];"
                        + " arguments[0].appendChild(o);", select, opcion);
        actor.attemptsTo(Pausa.configurada());
    }
}
