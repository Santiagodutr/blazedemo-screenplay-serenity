package co.com.equipo.certificacion.blazedemo.questions;

import co.com.equipo.certificacion.blazedemo.userinterfaces.ConfirmationPage;
import co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage;
import co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage;
import co.com.equipo.certificacion.blazedemo.userinterfaces.VacationPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

/** Textos visibles de cada pantalla del flujo, con nombres de negocio para el reporte. */
public final class TextoEnPantalla {

    private TextoEnPantalla() {
    }

    /** Título de la página de resultados ("Flights from X to Y:"). */
    public static Question<String> tituloDeResultados() {
        return texto("el título de resultados", ReservePage.TITULO_RESULTADOS);
    }

    /** Título de la página de compra ("Your flight from ... has been reserved."). */
    public static Question<String> tituloDeLaReserva() {
        return texto("el título de la reserva", PurchasePage.TITULO_RESERVA);
    }

    /** Mensaje principal de la página de confirmación. */
    public static Question<String> mensajeDeConfirmacion() {
        return texto("el mensaje de confirmación", ConfirmationPage.MENSAJE);
    }

    /** Texto de la página "Destination of the week". */
    public static Question<String> destinoDeLaSemana() {
        return texto("el destino de la semana", VacationPage.DESTINO);
    }

    private static Question<String> texto(String descripcion, Target target) {
        return Question.about(descripcion).answeredBy(actor -> Text.of(target).answeredBy(actor).trim());
    }
}
