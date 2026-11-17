package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.exceptions.VueloNoDisponibleException;
import co.com.equipo.certificacion.blazedemo.interactions.ElegirVueloDeLaFila;
import co.com.equipo.certificacion.blazedemo.models.Vuelo;
import co.com.equipo.certificacion.blazedemo.questions.VuelosListados;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.Comparator;
import java.util.List;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.BTN_COMPRAR;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.VUELOS_LISTADOS;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.VUELO_ELEGIDO;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Elige un vuelo de la tabla de resultados: el primero, uno por posición o el más barato.
 * El actor recuerda el vuelo elegido (y la lista completa) para validar precios después.
 */
public class ElegirVuelo implements Task {

    /** Criterio para escoger el vuelo. */
    public enum Criterio { POSICION, MAS_BARATO, MAS_CARO }

    private final Criterio criterio;
    private final int posicion;

    public ElegirVuelo(Criterio criterio, int posicion) {
        this.criterio = criterio;
        this.posicion = posicion;
    }

    public static ElegirVuelo primero() {
        return enPosicion(1);
    }

    public static ElegirVuelo enPosicion(int posicion) {
        return instrumented(ElegirVuelo.class, Criterio.POSICION, posicion);
    }

    public static ElegirVuelo masBarato() {
        return instrumented(ElegirVuelo.class, Criterio.MAS_BARATO, 0);
    }

    public static ElegirVuelo masCaro() {
        return instrumented(ElegirVuelo.class, Criterio.MAS_CARO, 0);
    }

    @Override
    @Step("{0} elige un vuelo de la lista (criterio: #criterio #posicion)")
    public <T extends Actor> void performAs(T actor) {
        List<Vuelo> vuelos = VuelosListados.enPantalla().answeredBy(actor);
        Vuelo elegido = seleccionar(vuelos);

        actor.remember(VUELOS_LISTADOS, vuelos);
        actor.remember(VUELO_ELEGIDO, elegido);
        Serenity.recordReportData().withTitle("Vuelo elegido").andContents(elegido.toString());

        actor.attemptsTo(
                ElegirVueloDeLaFila.numero(elegido.getPosicion()),
                WaitUntil.the(BTN_COMPRAR, isVisible()).forNoMoreThan(15).seconds()
        );
    }

    private Vuelo seleccionar(List<Vuelo> vuelos) {
        if (vuelos.isEmpty()) {
            throw new VueloNoDisponibleException("La búsqueda no devolvió vuelos");
        }
        if (criterio == Criterio.MAS_BARATO) {
            return vuelos.stream().min(Comparator.comparing(Vuelo::getPrecio)).orElseThrow();
        }
        if (criterio == Criterio.MAS_CARO) {
            return vuelos.stream().max(Comparator.comparing(Vuelo::getPrecio)).orElseThrow();
        }
        if (posicion < 1 || posicion > vuelos.size()) {
            throw new VueloNoDisponibleException("No existe el vuelo en la posición " + posicion
                    + "; la lista tiene " + vuelos.size() + " vuelos");
        }
        return vuelos.get(posicion - 1);
    }
}
