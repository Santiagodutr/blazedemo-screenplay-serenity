package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import co.com.equipo.certificacion.blazedemo.interactions.SeleccionarOpcion;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.BTN_BUSCAR_VUELOS;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_DESTINO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_ORIGEN;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.TITULO_RESULTADOS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Busca vuelos entre una ciudad de origen y una de destino. Uso: BuscarVuelos.desde("Paris").hacia("Rome"). */
public class BuscarVuelos implements Task {

    private final String origen;
    private final String destino;

    public BuscarVuelos(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public static Desde desde(String origen) {
        return new Desde(origen);
    }

    @Override
    @Step("{0} busca vuelos desde #origen hacia #destino")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                SeleccionarOpcion.llamada(origen, LISTA_ORIGEN),
                SeleccionarOpcion.llamada(destino, LISTA_DESTINO),
                HacerClicEn.elElemento(BTN_BUSCAR_VUELOS),
                WaitUntil.the(TITULO_RESULTADOS, isVisible()).forNoMoreThan(15).seconds()
        );
    }

    /** Builder para que la Task se lea como una frase. */
    public static class Desde {
        private final String origen;

        Desde(String origen) {
            this.origen = origen;
        }

        public BuscarVuelos hacia(String destino) {
            return instrumented(BuscarVuelos.class, origen, destino);
        }
    }
}
