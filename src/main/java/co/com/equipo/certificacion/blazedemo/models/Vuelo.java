package co.com.equipo.certificacion.blazedemo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/** Un vuelo tal como aparece en la tabla de resultados de reserve.php. */
@Getter
@AllArgsConstructor
@ToString
public class Vuelo {
    /** Posición de la fila en la tabla (empieza en 1). */
    private final int posicion;
    private final String numero;
    private final String aerolinea;
    private final BigDecimal precio;
}
