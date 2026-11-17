package co.com.equipo.certificacion.blazedemo.stepdefinitions;

import co.com.equipo.certificacion.blazedemo.models.DatosPago;
import co.com.equipo.certificacion.blazedemo.questions.DatoDeLaConfirmacion;
import co.com.equipo.certificacion.blazedemo.questions.Precio;
import co.com.equipo.certificacion.blazedemo.questions.TextoEnPantalla;
import co.com.equipo.certificacion.blazedemo.tasks.CompletarPago;
import co.com.equipo.certificacion.blazedemo.tasks.ElegirVuelo;
import co.com.equipo.certificacion.blazedemo.utils.GeneradorDatos;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

/** Pasos de elección de vuelo, pago y confirmación. Solo orquestan Tasks y Questions. */
public class CompraVuelosStepDefinitions {

    private static final String TARJETA_POR_DEFECTO = "Visa";

    @Cuando("elige el primer vuelo de la lista")
    public void eligeElPrimerVuelo() {
        theActorInTheSpotlight().attemptsTo(ElegirVuelo.primero());
    }

    @Cuando("elige el vuelo número {int} de la lista")
    public void eligeElVueloNumero(int posicion) {
        theActorInTheSpotlight().attemptsTo(ElegirVuelo.enPosicion(posicion));
    }

    @Cuando("elige el vuelo de menor precio")
    public void eligeElVueloDeMenorPrecio() {
        theActorInTheSpotlight().attemptsTo(ElegirVuelo.masBarato());
    }

    @Cuando("completa el pago con los siguientes datos")
    public void completaElPagoConLosDatos(DataTable tabla) {
        theActorInTheSpotlight().attemptsTo(
                CompletarPago.conLosDatos(DatosPago.desdeFila(tabla.asMaps().get(0))));
    }

    @Cuando("completa el pago de un pasajero aleatorio con tarjeta {string}")
    public void completaElPagoConTarjeta(String tipoTarjeta) {
        theActorInTheSpotlight().attemptsTo(
                CompletarPago.conLosDatos(GeneradorDatos.pasajeroAleatorio(tipoTarjeta)));
    }

    @Cuando("completa el pago de un pasajero aleatorio marcando recordar mis datos")
    public void completaElPagoMarcandoRecordar() {
        theActorInTheSpotlight().attemptsTo(CompletarPago.conLosDatos(
                GeneradorDatos.pasajeroAleatorio(TARJETA_POR_DEFECTO).toBuilder().recordarDatos(true).build()));
    }

    @Cuando("completa el pago de un pasajero aleatorio con el número de tarjeta {string}")
    public void completaElPagoConNumeroDeTarjeta(String numeroTarjeta) {
        theActorInTheSpotlight().attemptsTo(CompletarPago.conLosDatos(
                GeneradorDatos.pasajeroAleatorio(TARJETA_POR_DEFECTO).toBuilder().numeroTarjeta(numeroTarjeta).build()));
    }

    @Cuando("completa el pago de un pasajero aleatorio con año de expiración {string}")
    public void completaElPagoConAnioDeExpiracion(String anio) {
        theActorInTheSpotlight().attemptsTo(CompletarPago.conLosDatos(
                GeneradorDatos.pasajeroAleatorio(TARJETA_POR_DEFECTO).toBuilder().anio(anio).build()));
    }

    @Cuando("intenta comprar sin llenar el formulario")
    public void intentaComprarSinLlenarElFormulario() {
        theActorInTheSpotlight().attemptsTo(CompletarPago.sinDatos());
    }

    @Entonces("debe ver el mensaje {string}")
    public void debeVerElMensaje(String mensaje) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.mensajeDeConfirmacion(), equalTo(mensaje)));
    }

    @Entonces("la compra debe quedar en estado {string}")
    public void laCompraDebeQuedarEnEstado(String estado) {
        theActorInTheSpotlight().should(seeThat(DatoDeLaConfirmacion.llamado("Status"), equalTo(estado)));
    }

    @Entonces("la confirmación debe tener un id y un código de autorización")
    public void laConfirmacionDebeTenerIdYCodigo() {
        theActorInTheSpotlight().should(
                seeThat(DatoDeLaConfirmacion.llamado("Id"), not(emptyOrNullString())),
                seeThat(DatoDeLaConfirmacion.llamado("Auth Code"), not(emptyOrNullString()))
        );
    }

    @Entonces("el vuelo elegido debe ser el de menor precio de la lista")
    public void elVueloElegidoDebeSerElMasBarato() {
        theActorInTheSpotlight().should(seeThat(Precio.delVueloElegido(),
                comparesEqualTo(Precio.masBaratoListado().answeredBy(theActorInTheSpotlight()))));
    }

    @Entonces("debe estar en la página de compra del vuelo")
    public void debeEstarEnLaPaginaDeCompra() {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.tituloDeLaReserva(), containsString("has been reserved")));
    }

    @Entonces("el sistema acepta la compra sin validar los datos obligatorios")
    public void elSistemaAceptaLaCompraSinDatos() {
        aceptaLaCompra();
    }

    @Entonces("el sistema acepta la compra sin validar los datos de la tarjeta")
    public void elSistemaAceptaLaCompraConTarjetaInvalida() {
        aceptaLaCompra();
    }

    // ----- Defectos conocidos (@known-issue): afirman el comportamiento esperado -----

    @Entonces("el precio mostrado en la compra debe coincidir con el precio elegido")
    public void elPrecioDeLaCompraDebeCoincidir() {
        theActorInTheSpotlight().should(seeThat(Precio.enLaPaginaDeCompra(),
                comparesEqualTo(Precio.delVueloElegido().answeredBy(theActorInTheSpotlight()))));
    }

    @Entonces("el monto de la confirmación debe ser igual al total de la compra")
    public void elMontoDebeSerIgualAlTotal() {
        theActorInTheSpotlight().should(seeThat(Precio.montoDeLaConfirmacion(),
                comparesEqualTo(Precio.totalDeLaCompra().answeredBy(theActorInTheSpotlight()))));
    }

    @Entonces("la tarjeta de la confirmación debe terminar en {string}")
    public void laTarjetaDebeTerminarEn(String ultimosDigitos) {
        theActorInTheSpotlight().should(seeThat(DatoDeLaConfirmacion.llamado("Card Number"), endsWith(ultimosDigitos)));
    }

    private void aceptaLaCompra() {
        theActorInTheSpotlight().should(
                seeThat(TextoEnPantalla.mensajeDeConfirmacion(), equalTo("Thank you for your purchase today!")),
                seeThat(DatoDeLaConfirmacion.llamado("Status"), equalTo("PendingCapture"))
        );
    }
}
