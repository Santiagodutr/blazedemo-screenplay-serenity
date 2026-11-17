package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.page.TheWebPage;

/** Datos de la página que el actor tiene abierta en el navegador. */
public final class PaginaActual {

    private PaginaActual() {
    }

    /** Título que muestra la pestaña del navegador. */
    public static Question<String> tituloDeLaPestana() {
        return Question.about("el título de la pestaña").answeredBy(actor -> TheWebPage.title().answeredBy(actor).trim());
    }

    /** Dirección (URL) de la página actual. */
    public static Question<String> direccion() {
        return Question.about("la dirección de la página").answeredBy(actor -> TheWebPage.currentUrl().answeredBy(actor));
    }
}
