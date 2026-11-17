package co.com.equipo.certificacion.blazedemo.stepdefinitions;

import co.com.equipo.certificacion.blazedemo.questions.CantidadDeVuelos;
import co.com.equipo.certificacion.blazedemo.questions.CiudadesDisponibles;
import co.com.equipo.certificacion.blazedemo.questions.Precio;
import co.com.equipo.certificacion.blazedemo.questions.TablaDeVuelos;
import co.com.equipo.certificacion.blazedemo.questions.TextoEnPantalla;
import co.com.equipo.certificacion.blazedemo.questions.TodosLosPrecios;
import co.com.equipo.certificacion.blazedemo.tasks.AbrirBlazeDemo;
import co.com.equipo.certificacion.blazedemo.tasks.AbrirPaginaDirecta;
import co.com.equipo.certificacion.blazedemo.tasks.BuscarVuelos;
import co.com.equipo.certificacion.blazedemo.tasks.BuscarVuelosPorDefecto;
import co.com.equipo.certificacion.blazedemo.tasks.ConsultarDestinoDeLaSemana;
import co.com.equipo.certificacion.blazedemo.tasks.ForzarBusqueda;
import co.com.equipo.certificacion.blazedemo.tasks.InyectarHtmlEnLaBusqueda;
import co.com.equipo.certificacion.blazedemo.userinterfaces.ReservePage;
import co.com.equipo.certificacion.blazedemo.userinterfaces.VacationPage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.math.BigDecimal;
import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.serenitybdd.screenplay.questions.WebElementQuestion.the;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;

/** Pasos de búsqueda de vuelos. Solo orquestan Tasks y Questions: sin localizadores ni lógica. */
public class BusquedaVuelosStepDefinitions {

    /** Define el actor principal de la prueba E2E (el viajero) y su punto de partida. */
    @Dado("que {word} abre la página de BlazeDemo")
    public void queElViajeroAbreLaPaginaDeBlazeDemo(String viajero) {
        theActorCalled(viajero).wasAbleTo(AbrirBlazeDemo.paginaPrincipal());
    }

    @Cuando("busca vuelos desde {string} hacia {string}")
    public void buscaVuelosDesdeHacia(String origen, String destino) {
        theActorInTheSpotlight().attemptsTo(BuscarVuelos.desde(origen).hacia(destino));
    }

    @Cuando("busca vuelos sin cambiar las ciudades seleccionadas")
    public void buscaVuelosSinCambiarLasCiudades() {
        theActorInTheSpotlight().attemptsTo(BuscarVuelosPorDefecto.conLasCiudadesSeleccionadas());
    }

    @Cuando("consulta el destino de la semana")
    public void consultaElDestinoDeLaSemana() {
        theActorInTheSpotlight().attemptsTo(ConsultarDestinoDeLaSemana.enLaPaginaPrincipal());
    }

    @Cuando("fuerza una búsqueda desde {string} hacia {string} modificando el formulario")
    public void fuerzaUnaBusqueda(String origen, String destino) {
        theActorInTheSpotlight().attemptsTo(ForzarBusqueda.desdeHacia(origen, destino));
    }

    @Cuando("abre directamente la página de resultados sin buscar")
    public void abreLosResultadosSinBuscar() {
        theActorInTheSpotlight().attemptsTo(AbrirPaginaDirecta.deResultados());
    }

    @Cuando("busca vuelos inyectando código HTML en la ciudad de origen")
    public void buscaVuelosInyectandoHtml() {
        theActorInTheSpotlight().attemptsTo(InyectarHtmlEnLaBusqueda.enLaCiudadDeOrigen());
    }

    @Entonces("debe ver una lista de vuelos disponibles")
    public void debeVerUnaListaDeVuelosDisponibles() {
        theActorInTheSpotlight().should(seeThat(CantidadDeVuelos.listados(), greaterThan(0)));
    }

    @Entonces("debe ver exactamente {int} vuelos")
    public void debeVerExactamenteVuelos(int cantidad) {
        theActorInTheSpotlight().should(seeThat(CantidadDeVuelos.listados(), equalTo(cantidad)));
    }

    @Entonces("el título de la página de resultados debe contener {string} y {string}")
    public void elTituloDebeContener(String origen, String destino) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.tituloDeResultados(),
                allOf(containsString(origen), containsString(destino))));
    }

    @Entonces("los encabezados de la tabla deben indicar salida desde {string} y llegada a {string}")
    public void losEncabezadosDebenIndicar(String origen, String destino) {
        theActorInTheSpotlight().should(
                seeThat(TextoEnPantalla.encabezadoDeColumna(4), equalTo("Departs: " + origen)),
                seeThat(TextoEnPantalla.encabezadoDeColumna(5), equalTo("Arrives: " + destino))
        );
    }

    @Entonces("todos los vuelos listados deben tener un precio mayor a cero")
    public void todosLosVuelosDebenTenerPrecio() {
        theActorInTheSpotlight().should(seeThat(TodosLosPrecios.sonPositivos(), is(true)));
    }

    @Entonces("las aerolíneas listadas deben ser")
    public void lasAerolineasListadasDebenSer(List<String> aerolineas) {
        theActorInTheSpotlight().should(seeThat(TablaDeVuelos.aerolineas(),
                containsInAnyOrder(aerolineas.toArray(new String[0]))));
    }

    @Entonces("cada vuelo listado debe tener un número diferente")
    public void cadaVueloDebeTenerNumeroDiferente() {
        theActorInTheSpotlight().should(seeThat(TablaDeVuelos.numerosRepetidos(), is(empty())));
    }

    @Entonces("los precios deben mostrarse en dólares con dos decimales")
    public void losPreciosDebenTenerFormatoDeDolares() {
        theActorInTheSpotlight().should(seeThat(TablaDeVuelos.preciosConFormatoInvalido(), is(empty())));
    }

    @Entonces("el vuelo más barato de la lista debe costar menos que el más caro")
    public void elMasBaratoDebeCostarMenosQueElMasCaro() {
        theActorInTheSpotlight().should(seeThat(Precio.masBaratoEnPantalla(),
                lessThan(Precio.masCaroEnPantalla().answeredBy(theActorInTheSpotlight()))));
    }

    @Entonces("el precio más bajo listado debe ser {string} y el más alto {string}")
    public void elRangoDePreciosDebeSer(String minimo, String maximo) {
        theActorInTheSpotlight().should(
                seeThat(Precio.masBaratoEnPantalla(), comparesEqualTo(new BigDecimal(minimo))),
                seeThat(Precio.masCaroEnPantalla(), comparesEqualTo(new BigDecimal(maximo)))
        );
    }

    @Entonces("debe ver que el destino de la semana es {string}")
    public void debeVerElDestinoDeLaSemana(String destino) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.destinoDeLaSemana(), containsString(destino)));
    }

    @Entonces("debe ver la imagen del destino de la semana")
    public void debeVerLaImagenDelDestino() {
        theActorInTheSpotlight().should(seeThat(the(VacationPage.IMAGEN_DESTINO), isVisible()));
    }

    @Entonces("la lista de origen debe ofrecer las ciudades")
    public void laListaDeOrigenDebeOfrecer(List<String> ciudades) {
        theActorInTheSpotlight().should(seeThat(CiudadesDisponibles.comoOrigen(), equalTo(ciudades)));
    }

    @Entonces("la lista de destino debe ofrecer las ciudades")
    public void laListaDeDestinoDebeOfrecer(List<String> ciudades) {
        theActorInTheSpotlight().should(seeThat(CiudadesDisponibles.comoDestino(), equalTo(ciudades)));
    }

    @Entonces("la lista de destinos no debe ofrecer la ciudad {string}")
    public void laListaDeDestinosNoDebeOfrecer(String ciudad) {
        theActorInTheSpotlight().should(
                seeThat(CiudadesDisponibles.comoOrigen(), hasItem(ciudad)),
                seeThat(CiudadesDisponibles.comoDestino(), not(hasItem(ciudad)))
        );
    }

    @Entonces("ninguna ciudad debe aparecer a la vez como origen y destino")
    public void ningunaCiudadEnAmbasListas() {
        theActorInTheSpotlight().should(seeThat(CiudadesDisponibles.enAmbasListas(), is(empty())));
    }

    @Entonces("el sistema permite la búsqueda y muestra vuelos aunque el origen y el destino sean iguales")
    public void elSistemaPermiteRutaConMismoOrigenYDestino() {
        theActorInTheSpotlight().should(
                seeThat(CantidadDeVuelos.listados(), greaterThan(0)),
                seeThat(TextoEnPantalla.tituloDeResultados(), containsString("Paris to Paris"))
        );
    }

    @Entonces("el sistema muestra vuelos aunque no se eligió origen ni destino")
    public void elSistemaMuestraVuelosSinRuta() {
        theActorInTheSpotlight().should(
                seeThat(CantidadDeVuelos.listados(), greaterThan(0)),
                seeThat(TextoEnPantalla.tituloDeResultados(), matchesPattern("Flights from\\s+to\\s*:?"))
        );
    }

    @Entonces("el sitio interpreta el código inyectado en el título de resultados")
    public void elSitioInterpretaElCodigoInyectado() {
        theActorInTheSpotlight().should(seeThat(the(ReservePage.ELEMENTO_INYECTADO), isVisible()));
    }
}
