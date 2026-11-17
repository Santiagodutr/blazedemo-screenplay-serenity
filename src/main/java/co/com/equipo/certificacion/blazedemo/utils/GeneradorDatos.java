package co.com.equipo.certificacion.blazedemo.utils;

import co.com.equipo.certificacion.blazedemo.models.DatosPago;
import net.datafaker.Faker;

import java.time.Year;
import java.util.Locale;

/**
 * Genera pasajeros aleatorios con Datafaker. Los datos cambian en cada ejecución pero
 * siempre son válidos, así que el resultado esperado no cambia (F.I.R.S.T. - Repeatable).
 */
public final class GeneradorDatos {

    private static final Faker FAKER = new Faker(new Locale("es"));
    private static final String TARJETA_DE_PRUEBA = "4111111111111111";

    private GeneradorDatos() {
    }

    /** Pasajero aleatorio con datos de tarjeta válidos (vence dentro de 1 a 5 años). */
    public static DatosPago pasajeroAleatorio(String tipoTarjeta) {
        String nombre = FAKER.name().firstName() + " " + FAKER.name().lastName();
        return DatosPago.builder()
                .nombre(nombre)
                .direccion(FAKER.address().streetAddress())
                .ciudad(FAKER.address().city())
                .estado(FAKER.address().state())
                .codigoPostal(FAKER.address().zipCode())
                .tipoTarjeta(tipoTarjeta)
                .numeroTarjeta(TARJETA_DE_PRUEBA)
                .mes(String.valueOf(FAKER.number().numberBetween(1, 13)))
                .anio(String.valueOf(Year.now().getValue() + FAKER.number().numberBetween(1, 6)))
                .nombreEnTarjeta(nombre)
                .build();
    }
}
