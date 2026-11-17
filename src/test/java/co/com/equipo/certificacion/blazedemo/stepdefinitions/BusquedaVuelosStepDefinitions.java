package co.com.equipo.certificacion.blazedemo.stepdefinitions;

import co.com.equipo.certificacion.blazedemo.questions.CantidadDeVuelos;
import co.com.equipo.certificacion.blazedemo.questions.CiudadesDisponibles;
import co.com.equipo.certificacion.blazedemo.tasks.ForzarBusqueda;
import co.com.equipo.certificacion.blazedemo.questions.Precio;
import co.com.equipo.certificacion.blazedemo.questions.TextoEnPantalla;
import co.com.equipo.certificacion.blazedemo.questions.TodosLosPrecios;
import co.com.equipo.certificacion.blazedemo.tasks.AbrirBlazeDemo;
import co.com.equipo.certificacion.blazedemo.tasks.BuscarVuelos;
import co.com.equipo.certificacion.blazedemo.tasks.ConsultarDestinoDeLaSemana;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

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

    @Cuando("consulta el destino de la semana")
    public void consultaElDestinoDeLaSemana() {
        theActorInTheSpotlight().attemptsTo(ConsultarDestinoDeLaSemana.enLaPaginaPrincipal());
    }

    @Entonces("debe ver una lista de vuelos disponibles")
    public void debeVerUnaListaDeVuelosDisponibles() {
        theActorInTheSpotlight().should(seeThat(CantidadDeVuelos.listados(), greaterThan(0)));
    }

    @Entonces("el título de la página de resultados debe contener {string} y {string}")
    public void elTituloDebeContener(String origen, String destino) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.tituloDeResultados(),
                allOf(containsString(origen), containsString(destino))));
    }

    @Entonces("todos los vuelos listados deben tener un precio mayor a cero")
    public void todosLosVuelosDebenTenerPrecio() {
        theActorInTheSpotlight().should(seeThat(TodosLosPrecios.sonPositivos(), is(true)));
    }

    @Entonces("el vuelo más barato de la lista debe costar menos que el más caro")
    public void elMasBaratoDebeCostarMenosQueElMasCaro() {
        theActorInTheSpotlight().should(seeThat(Precio.masBaratoEnPantalla(),
                lessThan(Precio.masCaroEnPantalla().answeredBy(theActorInTheSpotlight()))));
    }

    @Entonces("debe ver que el destino de la semana es {string}")
    public void debeVerElDestinoDeLaSemana(String destino) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.destinoDeLaSemana(), containsString(destino)));
    }

    @Cuando("fuerza una búsqueda desde {string} hacia {string} modificando el formulario")
    public void fuerzaUnaBusqueda(String origen, String destino) {
        theActorInTheSpotlight().attemptsTo(ForzarBusqueda.desdeHacia(origen, destino));
    }

    @Entonces("la lista de destinos no debe ofrecer la ciudad {string}")
    public void laListaDeDestinosNoDebeOfrecer(String ciudad) {
        theActorInTheSpotlight().should(
                seeThat(CiudadesDisponibles.comoOrigen(), hasItem(ciudad)),
                seeThat(CiudadesDisponibles.comoDestino(), not(hasItem(ciudad)))
        );
    }

    @Entonces("el sistema permite la búsqueda y muestra vuelos aunque el origen y el destino sean iguales")
    public void elSistemaPermiteRutaConMismoOrigenYDestino() {
        theActorInTheSpotlight().should(
                seeThat(CantidadDeVuelos.listados(), greaterThan(0)),
                seeThat(TextoEnPantalla.tituloDeResultados(), containsString("Paris to Paris"))
        );
    }
}
