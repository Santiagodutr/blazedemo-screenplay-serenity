package co.com.equipo.certificacion.blazedemo.stepdefinitions;

import co.com.equipo.certificacion.blazedemo.questions.CamposObligatorios;
import co.com.equipo.certificacion.blazedemo.questions.PaginaActual;
import co.com.equipo.certificacion.blazedemo.questions.TextoEnPantalla;
import co.com.equipo.certificacion.blazedemo.tasks.AbrirPaginaDirecta;
import co.com.equipo.certificacion.blazedemo.tasks.NavegarPorLaBarra;
import co.com.equipo.certificacion.blazedemo.tasks.RecuperarContrasena;
import co.com.equipo.certificacion.blazedemo.userinterfaces.HomePage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import static net.serenitybdd.screenplay.questions.WebElementQuestion.the;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/** Pasos de navegación por la barra superior y de las páginas de cuenta (login, registro, recuperación). */
public class CuentaUsuarioStepDefinitions {

    @Dado("que {word} abre la página de inicio de sesión de BlazeDemo")
    public void queElViajeroAbreElInicioDeSesion(String viajero) {
        theActorCalled(viajero).wasAbleTo(AbrirPaginaDirecta.deInicioDeSesion());
    }

    @Cuando("navega con el enlace {string} de la barra superior")
    public void navegaConElEnlaceDeLaBarra(String enlace) {
        theActorInTheSpotlight().attemptsTo(NavegarPorLaBarra.alEnlace(enlace));
    }

    @Cuando("elige la opción de recuperar la contraseña")
    public void eligeRecuperarLaContrasena() {
        theActorInTheSpotlight().attemptsTo(RecuperarContrasena.desdeElInicioDeSesion());
    }

    @Entonces("debe ver la página principal de búsqueda")
    public void debeVerLaPaginaPrincipal() {
        theActorInTheSpotlight().should(seeThat(the(HomePage.BTN_BUSCAR_VUELOS), isVisible()));
    }

    @Entonces("debe llegar a la página de inicio de sesión")
    public void debeLlegarAlInicioDeSesion() {
        theActorInTheSpotlight().should(
                seeThat(PaginaActual.direccion(), containsString("/login")),
                seeThat(TextoEnPantalla.tituloDelFormulario(), equalTo("Login"))
        );
    }

    @Entonces("debe ver el formulario {string}")
    public void debeVerElFormulario(String titulo) {
        theActorInTheSpotlight().should(seeThat(TextoEnPantalla.tituloDelFormulario(), equalTo(titulo)));
    }

    @Entonces("el formulario debe marcar como obligatorios los campos")
    public void elFormularioDebeMarcarObligatorios(List<String> campos) {
        theActorInTheSpotlight().should(seeThat(CamposObligatorios.delFormulario(),
                containsInAnyOrder(campos.toArray(new String[0]))));
    }
}
