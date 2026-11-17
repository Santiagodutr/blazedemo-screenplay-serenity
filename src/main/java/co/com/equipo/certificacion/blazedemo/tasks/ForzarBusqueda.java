package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.AgregarOpcionALista;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_DESTINO;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Busca vuelos hacia un destino que la interfaz no ofrece, agregándolo antes al formulario.
 * Reutiliza la Task BuscarVuelos: solo cambia la preparación del formulario.
 */
public class ForzarBusqueda implements Task {

    private final String origen;
    private final String destino;

    public ForzarBusqueda(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public static ForzarBusqueda desdeHacia(String origen, String destino) {
        return instrumented(ForzarBusqueda.class, origen, destino);
    }

    @Override
    @Step("{0} fuerza una búsqueda desde #origen hacia #destino modificando el formulario")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AgregarOpcionALista.llamada(destino, LISTA_DESTINO),
                BuscarVuelos.desde(origen).hacia(destino)
        );
    }
}
