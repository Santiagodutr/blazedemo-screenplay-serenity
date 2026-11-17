package co.com.equipo.certificacion.blazedemo.runners.blazedemo;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Runner principal (generado con el scaffold: gradle grun --name=BlazeDemoRunner --folderName=blazedemo).
 * Ejecuta toda la batería excepto los defectos conocidos del sitio.
 * Se puede filtrar con -Dcucumber.filter.tags="@happy" (sobrescribe la etiqueta de abajo).
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/blazedemo",
        glue = "co.com.equipo.certificacion.blazedemo.stepdefinitions",
        tags = "@blazedemo and not @known-issue",
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class BlazeDemoRunner {}
