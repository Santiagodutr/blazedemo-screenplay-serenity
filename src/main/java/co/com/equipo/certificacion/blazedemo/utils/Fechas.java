package co.com.equipo.certificacion.blazedemo.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Fechas en el formato que usa la confirmación de BlazeDemo ("Mon, 16 Nov 2026 ..." en UTC). */
public final class Fechas {

    private static final DateTimeFormatter FORMATO_CONFIRMACION =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private Fechas() {
    }

    /** Fecha de hoy en UTC con el formato de la confirmación, por ejemplo "16 Nov 2026". */
    public static String hoyEnFormatoDeConfirmacion() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(FORMATO_CONFIRMACION);
    }
}
