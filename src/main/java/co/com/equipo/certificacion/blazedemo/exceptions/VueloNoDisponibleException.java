package co.com.equipo.certificacion.blazedemo.exceptions;

/** Se lanza cuando se pide un vuelo que no existe en la lista de resultados. */
public class VueloNoDisponibleException extends RuntimeException {
    public VueloNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
