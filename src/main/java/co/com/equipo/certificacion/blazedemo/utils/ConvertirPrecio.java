package co.com.equipo.certificacion.blazedemo.utils;

import java.math.BigDecimal;

/** Normaliza textos de precio ("$472.56", "Price: 400", "555 USD") a BigDecimal. */
public final class ConvertirPrecio {

    private ConvertirPrecio() {
    }

    public static BigDecimal desdeTexto(String texto) {
        String limpio = texto == null ? "" : texto.replaceAll("[^0-9.]", "");
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un precio en el texto: '" + texto + "'");
        }
        return new BigDecimal(limpio);
    }
}
