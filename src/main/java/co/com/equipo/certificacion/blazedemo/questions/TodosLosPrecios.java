package co.com.equipo.certificacion.blazedemo.questions;

import co.com.equipo.certificacion.blazedemo.models.Vuelo;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.math.BigDecimal;

/** Indica si todos los vuelos listados tienen un precio mayor a cero. */
@Subject("todos los vuelos listados tienen precio mayor a cero")
public class TodosLosPrecios implements Question<Boolean> {

    public static TodosLosPrecios sonPositivos() {
        return new TodosLosPrecios();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return VuelosListados.enPantalla().answeredBy(actor).stream()
                .map(Vuelo::getPrecio)
                .allMatch(precio -> precio.compareTo(BigDecimal.ZERO) > 0);
    }
}
