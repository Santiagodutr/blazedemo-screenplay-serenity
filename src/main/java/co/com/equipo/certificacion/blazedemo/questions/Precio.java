package co.com.equipo.certificacion.blazedemo.questions;

import co.com.equipo.certificacion.blazedemo.models.Vuelo;
import co.com.equipo.certificacion.blazedemo.utils.ConvertirPrecio;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.PRECIO_VUELO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.COSTO_TOTAL;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.IMPUESTOS;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.TOTAL_COMPRA;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.VUELOS_LISTADOS;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.VUELO_ELEGIDO;

/**
 * Preguntas sobre precios a lo largo del flujo: lista de resultados → compra → confirmación.
 * Todos los precios se normalizan a BigDecimal para compararlos sin depender del formato ($, USD).
 */
public final class Precio {

    private Precio() {
    }

    /** Precio del vuelo que el actor eligió en la lista (lo recordó al elegirlo). */
    public static Question<BigDecimal> delVueloElegido() {
        return Question.about("el precio del vuelo elegido")
                .answeredBy(actor -> actor.<Vuelo>recall(VUELO_ELEGIDO).getPrecio());
    }

    /** Menor precio entre los vuelos que el actor vio listados. */
    public static Question<BigDecimal> masBaratoListado() {
        return Question.about("el precio más barato de la lista")
                .answeredBy(actor -> actor.<List<Vuelo>>recall(VUELOS_LISTADOS).stream()
                        .map(Vuelo::getPrecio).min(Comparator.naturalOrder()).orElseThrow());
    }

    /** Mayor precio entre los vuelos que el actor vio listados. */
    public static Question<BigDecimal> masCaroListado() {
        return Question.about("el precio más caro de la lista vista")
                .answeredBy(actor -> actor.<List<Vuelo>>recall(VUELOS_LISTADOS).stream()
                        .map(Vuelo::getPrecio).max(Comparator.naturalOrder()).orElseThrow());
    }

    /** Impuestos y cargos de la página de compra ("Arbitrary Fees and Taxes: ..."). */
    public static Question<BigDecimal> impuestosEnLaCompra() {
        return Question.about("los impuestos de la compra")
                .answeredBy(actor -> ConvertirPrecio.desdeTexto(Text.of(IMPUESTOS).answeredBy(actor)));
    }

    /** Precio más impuestos según la página de compra. */
    public static Question<BigDecimal> precioMasImpuestos() {
        return Question.about("el precio más los impuestos")
                .answeredBy(actor -> enLaPaginaDeCompra().answeredBy(actor)
                        .add(impuestosEnLaCompra().answeredBy(actor)));
    }

    /** Mayor precio entre los vuelos que se ven en la tabla de resultados. */
    public static Question<BigDecimal> masCaroEnPantalla() {
        return Question.about("el precio más caro de la lista")
                .answeredBy(actor -> VuelosListados.enPantalla().answeredBy(actor).stream()
                        .map(Vuelo::getPrecio).max(Comparator.naturalOrder()).orElseThrow());
    }

    /** Menor precio entre los vuelos que se ven en la tabla de resultados. */
    public static Question<BigDecimal> masBaratoEnPantalla() {
        return Question.about("el precio más barato en pantalla")
                .answeredBy(actor -> VuelosListados.enPantalla().answeredBy(actor).stream()
                        .map(Vuelo::getPrecio).min(Comparator.naturalOrder()).orElseThrow());
    }

    /** Precio que muestra la página de compra ("Price: ..."). */
    public static Question<BigDecimal> enLaPaginaDeCompra() {
        return Question.about("el precio mostrado en la compra")
                .answeredBy(actor -> ConvertirPrecio.desdeTexto(Text.of(PRECIO_VUELO).answeredBy(actor)));
    }

    /** Total que mostró la página de compra antes de pagar (el actor lo recordó). */
    public static Question<BigDecimal> totalDeLaCompra() {
        return Question.about("el total de la compra")
                .answeredBy(actor -> actor.<BigDecimal>recall(TOTAL_COMPRA));
    }

    /** Total que se ve en la página de compra ("Total Cost: ..."). */
    public static Question<BigDecimal> totalEnPantalla() {
        return Question.about("el total en pantalla")
                .answeredBy(actor -> ConvertirPrecio.desdeTexto(Text.of(COSTO_TOTAL).answeredBy(actor)));
    }

    /** Monto cobrado según la confirmación ("Amount"). */
    public static Question<BigDecimal> montoDeLaConfirmacion() {
        return Question.about("el monto de la confirmación")
                .answeredBy(actor -> ConvertirPrecio.desdeTexto(
                        DatoDeLaConfirmacion.llamado("Amount").answeredBy(actor)));
    }
}
