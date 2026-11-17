package co.com.equipo.certificacion.blazedemo.tasks;

import co.com.equipo.certificacion.blazedemo.interactions.HacerClicEn;
import co.com.equipo.certificacion.blazedemo.interactions.LlenarCampo;
import co.com.equipo.certificacion.blazedemo.interactions.MarcarCasilla;
import co.com.equipo.certificacion.blazedemo.interactions.SeleccionarOpcion;
import co.com.equipo.certificacion.blazedemo.models.DatosPago;
import co.com.equipo.certificacion.blazedemo.questions.Precio;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.ConfirmationPage.MENSAJE;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.ANIO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.BTN_COMPRAR;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.CIUDAD;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.CODIGO_POSTAL;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.DIRECCION;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.ESTADO;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.MES;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.NOMBRE;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.NOMBRE_EN_TARJETA;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.NUMERO_TARJETA;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.RECORDAR_DATOS;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.PurchasePage.TIPO_TARJETA;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.DATOS_PAGO;
import static co.com.equipo.certificacion.blazedemo.utils.Constantes.TOTAL_COMPRA;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Diligencia el formulario de pago y presiona "Purchase Flight".
 * Antes de pagar, el actor recuerda el total que muestra la pantalla para compararlo con la confirmación.
 */
public class CompletarPago implements Task {

    private final DatosPago datos;

    public CompletarPago(DatosPago datos) {
        this.datos = datos;
    }

    public static CompletarPago conLosDatos(DatosPago datos) {
        return instrumented(CompletarPago.class, datos);
    }

    /** Envía el formulario sin diligenciar ningún campo. */
    public static CompletarPago sinDatos() {
        return conLosDatos(DatosPago.vacio());
    }

    @Override
    @Step("{0} completa el pago del vuelo a nombre de '#datos.nombre'")
    public <T extends Actor> void performAs(T actor) {
        actor.remember(TOTAL_COMPRA, Precio.totalEnPantalla().answeredBy(actor));
        actor.remember(DATOS_PAGO, datos);
        Serenity.recordReportData().withTitle("Datos de pago enviados").andContents(datos.toString());

        actor.attemptsTo(
                LlenarCampo.conValor(NOMBRE, datos.getNombre()),
                LlenarCampo.conValor(DIRECCION, datos.getDireccion()),
                LlenarCampo.conValor(CIUDAD, datos.getCiudad()),
                LlenarCampo.conValor(ESTADO, datos.getEstado()),
                LlenarCampo.conValor(CODIGO_POSTAL, datos.getCodigoPostal())
        );
        if (datos.getTipoTarjeta() != null) {
            actor.attemptsTo(SeleccionarOpcion.llamada(datos.getTipoTarjeta(), TIPO_TARJETA));
        }
        actor.attemptsTo(
                LlenarCampo.conValor(NUMERO_TARJETA, datos.getNumeroTarjeta()),
                LlenarCampo.conValor(MES, datos.getMes()),
                LlenarCampo.conValor(ANIO, datos.getAnio()),
                LlenarCampo.conValor(NOMBRE_EN_TARJETA, datos.getNombreEnTarjeta())
        );
        if (datos.isRecordarDatos()) {
            actor.attemptsTo(MarcarCasilla.llamada(RECORDAR_DATOS));
        }
        actor.attemptsTo(
                HacerClicEn.elElemento(BTN_COMPRAR),
                WaitUntil.the(MENSAJE, isVisible()).forNoMoreThan(15).seconds()
        );
    }
}
