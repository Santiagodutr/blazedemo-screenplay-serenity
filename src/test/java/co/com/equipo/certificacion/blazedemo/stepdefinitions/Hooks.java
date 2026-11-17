package co.com.equipo.certificacion.blazedemo.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

/**
 * Prepara el escenario antes de cada prueba: un elenco nuevo de actores con la habilidad
 * BrowseTheWeb y un navegador limpio (F.I.R.S.T. - Independent). Al final se cierra todo.
 */
public class Hooks {

    @Before
    public void prepararElEscenario() {
        OnStage.setTheStage(new OnlineCast());
    }

    @After
    public void bajarElTelon() {
        OnStage.drawTheCurtain();
    }
}
