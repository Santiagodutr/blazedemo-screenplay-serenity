package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.SelectOptions;

import java.util.List;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_DESTINO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage.LISTA_ORIGEN;

/** Ciudades que ofrece cada lista desplegable de la página principal. */
public final class CiudadesDisponibles {

    private CiudadesDisponibles() {
    }

    public static Question<List<String>> comoDestino() {
        return Question.about("las ciudades de destino disponibles")
                .answeredBy(actor -> SelectOptions.of(LISTA_DESTINO).answeredBy(actor));
    }

    public static Question<List<String>> comoOrigen() {
        return Question.about("las ciudades de origen disponibles")
                .answeredBy(actor -> SelectOptions.of(LISTA_ORIGEN).answeredBy(actor));
    }
}
