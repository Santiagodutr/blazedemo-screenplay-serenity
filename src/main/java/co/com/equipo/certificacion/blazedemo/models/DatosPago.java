package co.com.equipo.certificacion.blazedemo.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Datos del formulario de pago de BlazeDemo (pasajero + tarjeta).
 * Se construye desde la tabla de Gherkin o desde el generador de datos aleatorios.
 */
@Getter
@Builder(toBuilder = true)
@ToString
public class DatosPago {
    private final String nombre;
    private final String direccion;
    private final String ciudad;
    private final String estado;
    private final String codigoPostal;
    private final String tipoTarjeta;
    private final String numeroTarjeta;
    private final String mes;
    private final String anio;
    private final String nombreEnTarjeta;
    private final boolean recordarDatos;

    /** Convierte una fila de la tabla de Gherkin (encabezados en español) en DatosPago. */
    public static DatosPago desdeFila(Map<String, String> fila) {
        return DatosPago.builder()
                .nombre(fila.get("nombre"))
                .direccion(fila.get("direccion"))
                .ciudad(fila.get("ciudad"))
                .estado(fila.get("estado"))
                .codigoPostal(fila.get("codigo postal"))
                .tipoTarjeta(fila.get("tipo de tarjeta"))
                .numeroTarjeta(fila.get("numero de tarjeta"))
                .mes(fila.get("mes"))
                .anio(fila.get("anio"))
                .nombreEnTarjeta(fila.get("nombre en la tarjeta"))
                .build();
    }

    /** Formulario sin ningún dato (para el caso negativo de campos vacíos). */
    public static DatosPago vacio() {
        return DatosPago.builder().build();
    }
}
