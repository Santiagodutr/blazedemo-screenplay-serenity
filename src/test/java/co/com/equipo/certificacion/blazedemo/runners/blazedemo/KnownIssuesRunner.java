package co.com.equipo.certificacion.blazedemo.runners.blazedemo;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Runner de defectos conocidos de BlazeDemo (@known-issue). Estos escenarios afirman lo que el sitio
 * DEBERÍA hacer, por eso FALLAN: el reporte sirve como evidencia de los hallazgos.
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/blazedemo",
        glue = "co.com.equipo.certificacion.blazedemo.stepdefinitions",
        tags = "@known-issue",
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class KnownIssuesRunner {}
