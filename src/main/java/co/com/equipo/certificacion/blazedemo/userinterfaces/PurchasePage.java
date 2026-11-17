package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Página de compra (/purchase.php): resumen del vuelo y formulario de pago. */
public final class PurchasePage {
    public static final Target TITULO_RESERVA = Target.the("título de la reserva")
            .locatedBy("css:div.container h2");
    public static final Target PRECIO_VUELO = Target.the("precio del vuelo")
            .locatedBy("//p[starts-with(normalize-space(),\"Price:\")]");
    public static final Target IMPUESTOS = Target.the("impuestos y cargos")
            .locatedBy("//p[starts-with(normalize-space(),\"Arbitrary Fees and Taxes:\")]");
    public static final Target COSTO_TOTAL = Target.the("costo total")
            .locatedBy("//p[starts-with(normalize-space(),\"Total Cost:\")]/em");

    public static final Target NOMBRE = Target.the("campo nombre").locatedBy("css:#inputName");
    public static final Target DIRECCION = Target.the("campo dirección").locatedBy("css:#address");
    public static final Target CIUDAD = Target.the("campo ciudad").locatedBy("css:#city");
    public static final Target ESTADO = Target.the("campo estado").locatedBy("css:#state");
    public static final Target CODIGO_POSTAL = Target.the("campo código postal").locatedBy("css:#zipCode");
    public static final Target TIPO_TARJETA = Target.the("lista tipo de tarjeta").locatedBy("css:#cardType");
    public static final Target NUMERO_TARJETA = Target.the("campo número de tarjeta").locatedBy("css:#creditCardNumber");
    public static final Target MES = Target.the("campo mes").locatedBy("css:#creditCardMonth");
    public static final Target ANIO = Target.the("campo año").locatedBy("css:#creditCardYear");
    public static final Target NOMBRE_EN_TARJETA = Target.the("campo nombre en la tarjeta").locatedBy("css:#nameOnCard");
    public static final Target RECORDAR_DATOS = Target.the("casilla Remember me").locatedBy("css:#rememberMe");
    public static final Target BTN_COMPRAR = Target.the("botón Purchase Flight")
            .locatedBy("css:input[type=\"submit\"][value=\"Purchase Flight\"]");

    private PurchasePage() {
    }
}
