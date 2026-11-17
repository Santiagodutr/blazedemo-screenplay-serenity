package co.com.equipo.certificacion.blazedemo.runners.blazedemo;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/** Runner de caminos excepcionales y escenarios límite (@negative, @boundary). */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/blazedemo",
        glue = "co.com.equipo.certificacion.blazedemo.stepdefinitions",
        tags = "@negative or @boundary",
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class NegativeRunner {}
