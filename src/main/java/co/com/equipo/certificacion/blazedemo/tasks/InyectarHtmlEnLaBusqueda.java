package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.AgregarOpcionALista;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_ORIGEN;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Prueba de seguridad: agrega como ciudad de origen un texto con etiquetas HTML y busca vuelos.
 * Si el sitio no escapa el texto, la etiqueta aparece como elemento real en el título de resultados.
 */
public class InyectarHtmlEnLaBusqueda implements Task {

    /** Etiqueta inofensiva con un id conocido, fácil de detectar en la página. */
    private static final String HTML_INYECTADO = "<i id=\"inyectado\">Paris</i>";
    private static final String DESTINO = "Rome";

    public static InyectarHtmlEnLaBusqueda enLaCiudadDeOrigen() {
        return instrumented(InyectarHtmlEnLaBusqueda.class);
    }

    @Override
    @Step("{0} busca vuelos inyectando código HTML en la ciudad de origen")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AgregarOpcionALista.llamada(HTML_INYECTADO, LISTA_ORIGEN),
                BuscarVuelos.desde(HTML_INYECTADO).hacia(DESTINO)
        );
    }
}
