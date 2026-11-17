package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Value;

import java.time.YearMonth;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.ANIO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.MES;

/** Valores que el formulario de pago trae precargados antes de que el viajero escriba. */
public final class FormularioDePago {

    private FormularioDePago() {
    }

    public static Question<String> mesPrecargado() {
        return Question.about("el mes precargado").answeredBy(actor -> Value.of(MES).answeredBy(actor).trim());
    }

    public static Question<String> anioPrecargado() {
        return Question.about("el año precargado").answeredBy(actor -> Value.of(ANIO).answeredBy(actor).trim());
    }

    /** true si el mes/año precargados ya pasaron respecto al mes actual. */
    public static Question<Boolean> fechaPrecargadaVencida() {
        return Question.about("si la fecha precargada está vencida")
                .answeredBy(actor -> YearMonth.of(Integer.parseInt(anioPrecargado().answeredBy(actor)),
                        Integer.parseInt(mesPrecargado().answeredBy(actor))).isBefore(YearMonth.now()));
    }
}
