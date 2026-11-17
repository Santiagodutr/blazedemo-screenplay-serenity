package co.com.equipo.certificacion.blazedemo.questions;

import co.com.equipo.certificacion.blazedemo.models.Vuelo;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.COLUMNA_PRECIO;

/** Preguntas sobre el contenido de la tabla de resultados (aerolíneas, números de vuelo, formato de precios). */
public final class TablaDeVuelos {

    /** Formato de moneda esperado: signo de dólar y exactamente dos decimales, p. ej. "$472.56". */
    private static final String FORMATO_DOLARES = "^\\$\\d+\\.\\d{2}$";

    private TablaDeVuelos() {
    }

    /** Aerolíneas distintas que operan los vuelos listados. */
    public static Question<List<String>> aerolineas() {
        return Question.about("las aerolíneas listadas")
                .answeredBy(actor -> VuelosListados.enPantalla().answeredBy(actor).stream()
                        .map(Vuelo::getAerolinea).distinct().collect(Collectors.toList()));
    }

    /** Números de vuelo que aparecen más de una vez (lista vacía si todos son únicos). */
    public static Question<List<String>> numerosRepetidos() {
        return Question.about("los números de vuelo repetidos")
                .answeredBy(actor -> {
                    Set<String> vistos = new HashSet<>();
                    return VuelosListados.enPantalla().answeredBy(actor).stream()
                            .map(Vuelo::getNumero).filter(numero -> !vistos.add(numero))
                            .collect(Collectors.toList());
                });
    }

    /** Precios cuyo texto no cumple el formato "$0.00" (lista vacía si todos lo cumplen). */
    public static Question<List<String>> preciosConFormatoInvalido() {
        return Question.about("los precios con formato inválido")
                .answeredBy(actor -> Text.ofEach(COLUMNA_PRECIO).answeredBy(actor).stream()
                        .map(String::trim).filter(precio -> !precio.matches(FORMATO_DOLARES))
                        .collect(Collectors.toList()));
    }
}
