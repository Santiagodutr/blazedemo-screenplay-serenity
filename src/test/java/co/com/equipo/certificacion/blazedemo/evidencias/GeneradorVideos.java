package co.com.equipo.certificacion.blazedemo.evidencias;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Convierte las capturas paso a paso del reporte de Serenity (FOR_EACH_ACTION) en un GIF animado por
 * escenario, con el nombre del escenario, el paso Gherkin y el resultado sobreimpresos.
 * <p>
 * Uso: {@code gradlew generarVideos} (lee target/site/serenity y escribe en evidencias/videos).
 */
public final class GeneradorVideos {

    private static final int ANCHO = 960;
    private static final int ALTO_BARRA = 64;
    private static final int MS_POR_CUADRO = 900;

    private GeneradorVideos() {
    }

    public static void main(String[] args) throws IOException {
        Path reporte = Paths.get(args.length > 0 ? args[0] : "target/site/serenity");
        Path salida = Paths.get(args.length > 1 ? args[1] : "evidencias/videos");
        Files.createDirectories(salida);
        int generados = 0;
        try (Stream<Path> archivos = Files.list(reporte)) {
            for (Path json : (Iterable<Path>) archivos.filter(p -> p.toString().endsWith(".json"))::iterator) {
                JsonObject resultado = leer(json);
                if (resultado == null || !resultado.has("testSteps")) {
                    continue;
                }
                JsonArray pasos = resultado.getAsJsonArray("testSteps");
                String titulo = resultado.get("title").getAsString();
                String estado = resultado.has("result") ? resultado.get("result").getAsString() : "";
                if (esEsquema(pasos)) {
                    // Scenario Outline: cada paso de primer nivel es una fila de Ejemplos → un GIF por fila
                    for (JsonElement e : pasos) {
                        JsonObject ejemplo = e.getAsJsonObject();
                        List<Cuadro> cuadros = new ArrayList<>();
                        recorrer(ejemplo.getAsJsonArray("children"), null, cuadros);
                        String nombre = ejemplo.get("description").getAsString();
                        String estadoFila = ejemplo.has("result") ? ejemplo.get("result").getAsString() : estado;
                        generados += generar(reporte, salida, cuadros, nombre, estadoFila);
                    }
                } else {
                    List<Cuadro> cuadros = new ArrayList<>();
                    recorrer(pasos, null, cuadros);
                    generados += generar(reporte, salida, cuadros, titulo, estado);
                }
            }
        }
        System.out.println("Total de videos GIF: " + generados);
    }

    private static int generar(Path reporte, Path salida, List<Cuadro> cuadros, String titulo, String estado)
            throws IOException {
        if (cuadros.isEmpty()) {
            return 0;
        }
        Path gif = salida.resolve(estado + "_" + nombreArchivo(titulo) + ".gif");
        escribirGif(reporte, cuadros, titulo, estado, gif.toFile());
        System.out.println("GIF generado: " + gif);
        return 1;
    }

    /** En un Scenario Outline los pasos de primer nivel tienen la forma "1: nombre ({datos})". */
    private static boolean esEsquema(JsonArray pasos) {
        for (JsonElement e : pasos) {
            JsonObject paso = e.getAsJsonObject();
            if (!paso.has("children") || !paso.get("description").getAsString().matches("^\\d+: .*")) {
                return false;
            }
        }
        return pasos.size() > 0;
    }

    private static JsonObject leer(Path json) {
        try {
            JsonElement e = JsonParser.parseString(Files.readString(json, StandardCharsets.UTF_8));
            return e.isJsonObject() ? e.getAsJsonObject() : null;
        } catch (Exception ex) {
            return null;
        }
    }

    /** Recorre los pasos en orden; cada captura queda asociada a su paso Gherkin de primer nivel. */
    private static void recorrer(JsonArray pasos, String pasoGherkin, List<Cuadro> cuadros) {
        for (JsonElement e : pasos) {
            JsonObject paso = e.getAsJsonObject();
            String descripcion = pasoGherkin == null ? paso.get("description").getAsString() : pasoGherkin;
            String resultado = paso.has("result") ? paso.get("result").getAsString() : "";
            if (paso.has("screenshots")) {
                for (JsonElement s : paso.getAsJsonArray("screenshots")) {
                    String archivo = s.getAsJsonObject().get("screenshot").getAsString();
                    if (cuadros.isEmpty() || !cuadros.get(cuadros.size() - 1).archivo.equals(archivo)) {
                        cuadros.add(new Cuadro(archivo, descripcion.split("\n")[0], resultado));
                    }
                }
            }
            if (paso.has("children")) {
                recorrer(paso.getAsJsonArray("children"), descripcion, cuadros);
            }
        }
    }

    private static void escribirGif(Path reporte, List<Cuadro> cuadros, String titulo, String estado, File destino)
            throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
        try (ImageOutputStream out = ImageIO.createImageOutputStream(destino)) {
            writer.setOutput(out);
            writer.prepareWriteSequence(null);
            for (int i = 0; i < cuadros.size(); i++) {
                Cuadro c = cuadros.get(i);
                File png = reporte.resolve(c.archivo).toFile();
                if (!png.exists()) {
                    continue;
                }
                boolean ultimo = i == cuadros.size() - 1;
                BufferedImage imagen = componer(ImageIO.read(png), titulo, c.paso,
                        ultimo ? "RESULTADO: " + estado : c.resultado, ultimo ? estado : c.resultado);
                IIOMetadata meta = metadatos(writer, imagen, ultimo ? MS_POR_CUADRO * 3 : MS_POR_CUADRO, i == 0);
                writer.writeToSequence(new IIOImage(imagen, null, meta), null);
            }
            writer.endWriteSequence();
        } finally {
            writer.dispose();
        }
    }

    private static BufferedImage componer(BufferedImage captura, String titulo, String paso, String etiqueta,
                                          String estado) {
        int alto = (int) Math.round(captura.getHeight() * (ANCHO / (double) captura.getWidth()));
        BufferedImage img = new BufferedImage(ANCHO, alto + ALTO_BARRA, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(24, 32, 48));
        g.fillRect(0, 0, ANCHO, ALTO_BARRA);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(recortar(titulo, 95), 12, 24);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.setColor(new Color(200, 210, 225));
        g.drawString(recortar(paso, 100), 12, 50);
        g.setColor(colorDe(estado));
        g.fillRoundRect(ANCHO - 190, 14, 178, 26, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.drawString(recortar(etiqueta, 24), ANCHO - 182, 32);
        g.drawImage(captura, 0, ALTO_BARRA, ANCHO, alto, null);
        g.dispose();
        return img;
    }

    private static Color colorDe(String estado) {
        switch (estado == null ? "" : estado) {
            case "SUCCESS": return new Color(46, 160, 67);
            case "FAILURE":
            case "ERROR": return new Color(200, 50, 50);
            default: return new Color(110, 110, 120);
        }
    }

    private static IIOMetadata metadatos(ImageWriter writer, BufferedImage img, int ms, boolean primero)
            throws IOException {
        IIOMetadata meta = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(img), null);
        String formato = meta.getNativeMetadataFormatName();
        IIOMetadataNode raiz = (IIOMetadataNode) meta.getAsTree(formato);
        IIOMetadataNode control = nodo(raiz, "GraphicControlExtension");
        control.setAttribute("disposalMethod", "none");
        control.setAttribute("userInputFlag", "FALSE");
        control.setAttribute("transparentColorFlag", "FALSE");
        control.setAttribute("delayTime", String.valueOf(ms / 10));
        control.setAttribute("transparentColorIndex", "0");
        if (primero) {
            IIOMetadataNode apps = nodo(raiz, "ApplicationExtensions");
            IIOMetadataNode app = new IIOMetadataNode("ApplicationExtension");
            app.setAttribute("applicationID", "NETSCAPE");
            app.setAttribute("authenticationCode", "2.0");
            app.setUserObject(new byte[]{0x1, 0, 0});
            apps.appendChild(app);
        }
        meta.setFromTree(formato, raiz);
        return meta;
    }

    private static IIOMetadataNode nodo(IIOMetadataNode raiz, String nombre) {
        for (int i = 0; i < raiz.getLength(); i++) {
            if (raiz.item(i).getNodeName().equalsIgnoreCase(nombre)) {
                return (IIOMetadataNode) raiz.item(i);
            }
        }
        IIOMetadataNode nuevo = new IIOMetadataNode(nombre);
        raiz.appendChild(nuevo);
        return nuevo;
    }

    private static String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + "…";
    }

    private static String nombreArchivo(String titulo) {
        String sinTildes = Normalizer.normalize(titulo, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.replaceAll("[^A-Za-z0-9]+", "_").replaceAll("^_|_$", "");
    }

    private static final class Cuadro {
        private final String archivo;
        private final String paso;
        private final String resultado;

        private Cuadro(String archivo, String paso, String resultado) {
            this.archivo = archivo;
            this.paso = paso;
            this.resultado = resultado;
        }
    }
}
