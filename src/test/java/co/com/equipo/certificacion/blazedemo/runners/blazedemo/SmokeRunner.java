package co.com.equipo.certificacion.blazedemo.runners.blazedemo;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/** Runner de humo: solo la ruta feliz crítica (@smoke). Ideal para la demo en vivo. */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/blazedemo",
        glue = "co.com.equipo.certificacion.blazedemo.stepdefinitions",
        tags = "@smoke",
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class SmokeRunner {}
