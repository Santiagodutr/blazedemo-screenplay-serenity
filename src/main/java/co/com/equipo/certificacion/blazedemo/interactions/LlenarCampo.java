package co.com.equipo.certificacion.blazedemo.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Limpia un campo de texto y escribe un valor. Si el valor es nulo solo lo deja vacío,
 * lo que permite reutilizarla en el caso negativo de formulario vacío.
 */
public class LlenarCampo implements Interaction {

    private final Target campo;
    private final String valor;

    public LlenarCampo(Target campo, String valor) {
        this.campo = campo;
        this.valor = valor;
    }

    public static LlenarCampo conValor(Target campo, String valor) {
        return instrumented(LlenarCampo.class, campo, valor);
    }

    @Override
    @Step("{0} escribe '#valor' en #campo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Clear.field(campo));
        if (valor != null && !valor.isEmpty()) {
            actor.attemptsTo(Enter.theValue(valor).into(campo));
        }
        actor.attemptsTo(Pausa.configurada());
    }
}
