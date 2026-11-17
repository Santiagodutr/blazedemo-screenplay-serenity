package co.com.equipo.certificacion.blazedemo.questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;

import java.util.List;
import java.util.stream.Collectors;

import static co.com.equipo.certificacion.blazedemo.userinterfaces.CuentaPage.CAMPOS_OBLIGATORIOS;
import static co.com.equipo.certificacion.blazedemo.userinterfaces.CuentaPage.ETIQUETA_DEL_CAMPO;

/** Etiquetas de los campos marcados como obligatorios (atributo required) en el formulario visible. */
@Subject("los campos obligatorios del formulario")
public class CamposObligatorios implements Question<List<String>> {

    public static CamposObligatorios delFormulario() {
        return new CamposObligatorios();
    }

    @Override
    public List<String> answeredBy(Actor actor) {
        List<WebElementFacade> campos = CAMPOS_OBLIGATORIOS.resolveAllFor(actor);
        return campos.stream()
                .map(campo -> ETIQUETA_DEL_CAMPO.of(campo.getAttribute("id")).resolveFor(actor).getText().trim())
                .collect(Collectors.toList());
    }
}
