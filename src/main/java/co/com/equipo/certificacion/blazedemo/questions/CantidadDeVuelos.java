package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/** Cantidad de vuelos que muestra la tabla de resultados. */
@Subject("la cantidad de vuelos listados")
public class CantidadDeVuelos implements Question<Integer> {

    public static CantidadDeVuelos listados() {
        return new CantidadDeVuelos();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return VuelosListados.enPantalla().answeredBy(actor).size();
    }
}
