package co.com.equipo.certificacion.blazedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Páginas de cuenta (/login, /register, /password/reset): comparten la misma plantilla de formulario. */
public final class CuentaPage {
    public static final Target TITULO_FORMULARIO = Target.the("título del formulario")
            .locatedBy("css:div.panel-heading");
    public static final Target CAMPOS_OBLIGATORIOS = Target.the("campos obligatorios del formulario")
            .locatedBy("css:div.panel-body form input[required]");
    public static final Target ETIQUETA_DEL_CAMPO = Target.the("etiqueta del campo {0}")
            .locatedBy("//label[@for=\"{0}\"]");
    public static final Target LINK_RECUPERAR_CONTRASENA = Target.the("enlace Forgot Your Password?")
            .locatedBy("//a[normalize-space()=\"Forgot Your Password?\"]");

    private CuentaPage() {
    }
}
