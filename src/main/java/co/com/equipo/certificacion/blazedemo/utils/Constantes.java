package co.com.equipo.certificacion.blazedemo.utils;

/** Constantes compartidas: claves de memoria del actor y nombres de páginas en serenity.conf. */
public final class Constantes {

    /** Nombre de la página principal definida en serenity.conf (pages.blazedemo). */
    public static final String PAGINA_BLAZEDEMO = "pages.blazedemo";

    /** Páginas que se abren de forma directa (definidas en serenity.conf). */
    public static final String PAGINA_RESULTADOS = "pages.resultados";
    public static final String PAGINA_CONFIRMACION = "pages.confirmacion";
    public static final String PAGINA_LOGIN = "pages.login";

    /** Clave con la que el actor recuerda el número (Id) de una compra confirmada. */
    public static final String NUMERO_COMPRA = "numero compra";

    /** Clave con la que el actor recuerda el vuelo que eligió. */
    public static final String VUELO_ELEGIDO = "vuelo elegido";

    /** Clave con la que el actor recuerda los vuelos que vio listados. */
    public static final String VUELOS_LISTADOS = "vuelos listados";

    /** Clave con la que el actor recuerda el total que mostró la página de compra. */
    public static final String TOTAL_COMPRA = "total compra";

    /** Clave con la que el actor recuerda los datos de pago que envió. */
    public static final String DATOS_PAGO = "datos pago";

    private Constantes() {
    }
}
