package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.SilentInteraction;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/**
 * Pausa entre pasos para que la prueba se pueda observar en la sustentación.
 * <p>
 * Es configurable con {@code -Ddemo.delay=2000} (milisegundos) o con {@code demo.delay} en serenity.conf.
 * Por defecto vale 0, así la batería sigue siendo rápida (F.I.R.S.T. - Fast).
 * No se usa para sincronizar: para eso están las esperas explícitas de Serenity.
 */
public class Pausa extends SilentInteraction {

    private static final String PROPIEDAD_DELAY = "demo.delay";

    private final long milisegundos;

    public Pausa(long milisegundos) {
        this.milisegundos = milisegundos;
    }

    /** Pausa con la duración configurada en demo.delay (0 si no está configurada). */
    public static Pausa configurada() {
        return new Pausa(delayConfigurado());
    }

    /** Pausa explícita de N segundos. */
    public static Pausa deSegundos(int segundos) {
        return new Pausa(segundos * 1000L);
    }

    static long delayConfigurado() {
        String valor = System.getProperty(PROPIEDAD_DELAY,
                SystemEnvironmentVariables.currentEnvironmentVariables().getProperty(PROPIEDAD_DELAY, "0"));
        try {
            return Math.max(0, Long.parseLong(valor.trim()));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (milisegundos <= 0) {
            return;
        }
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
