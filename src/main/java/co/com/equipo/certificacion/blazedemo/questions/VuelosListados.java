package co.com.equipo.certificacion.blazedemo.questions;

import co.com.equipo.certificacion.blazedemo.models.Vuelo;
import co.com.equipo.certificacion.blazedemo.utils.ConvertirPrecio;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.util.ArrayList;
import java.util.List;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.COLUMNA_AEROLINEA;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.COLUMNA_NUMERO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage.COLUMNA_PRECIO;

/** Lee la tabla de resultados y devuelve los vuelos (número, aerolínea y precio). */
@Subject("los vuelos listados")
public class VuelosListados implements Question<List<Vuelo>> {

    public static VuelosListados enPantalla() {
        return new VuelosListados();
    }

    @Override
    public List<Vuelo> answeredBy(Actor actor) {
        List<String> numeros = new ArrayList<>(Text.ofEach(COLUMNA_NUMERO).answeredBy(actor));
        List<String> aerolineas = new ArrayList<>(Text.ofEach(COLUMNA_AEROLINEA).answeredBy(actor));
        List<String> precios = new ArrayList<>(Text.ofEach(COLUMNA_PRECIO).answeredBy(actor));
        List<Vuelo> vuelos = new ArrayList<>();
        for (int i = 0; i < precios.size(); i++) {
            vuelos.add(new Vuelo(i + 1, numeros.get(i).trim(), aerolineas.get(i).trim(),
                    ConvertirPrecio.desdeTexto(precios.get(i))));
        }
        return vuelos;
    }
}
