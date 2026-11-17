package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.utils.GeneradorDatos;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Compra completa de punta a punta en una sola Task de negocio: reutiliza las Tasks
 * AbrirBlazeDemo, BuscarVuelos, ElegirVuelo y CompletarPago.
 */
public class ComprarVuelo implements Task {

    private static final String TARJETA = "Visa";

    private final String origen;
    private final String destino;

    public ComprarVuelo(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public static ComprarVuelo completo(String origen, String destino) {
        return instrumented(ComprarVuelo.class, origen, destino);
    }

    @Override
    @Step("{0} realiza una compra completa desde #origen hacia #destino")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AbrirBlazeDemo.paginaPrincipal(),
                BuscarVuelos.desde(origen).hacia(destino),
                ElegirVuelo.primero(),
                CompletarPago.conLosDatos(GeneradorDatos.pasajeroAleatorio(TARJETA))
        );
    }
}
