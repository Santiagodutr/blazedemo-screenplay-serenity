# Guion técnico – Recorrido en el IDE y demo en vivo

Guía para mostrar en IntelliJ IDEA dónde está cada pieza del scaffold y del patrón Screenplay,
y cómo correr todo en vivo. Las rutas son relativas a la raíz del proyecto (`TALLER_BLAZEDEMO/`).
Abreviatura usada abajo: `…/blazedemo/` = `co/com/equipo/certificacion/blazedemo/`.

> Los números de línea corresponden al commit `65196f7` (merge de la rama `entrega/gherkin-reportes-serenity`).
> Desde ese commit las palabras clave de Gherkin están en inglés (`Feature`, `Background`, `Scenario`, `Given/When/Then`)
> y el texto de los pasos sigue en español; los StepDefinitions usan `@Given/@When/@Then`. Si se edita un archivo, verificarlos con **Ctrl+G** (ir a línea).

---

## 1. Preparación (antes de la clase, ~10 min)

1. **Requisitos:** JDK 17, Google Chrome actualizado e IntelliJ IDEA con los plugins *Cucumber for Java* y *Gherkin*
   (`File > Settings > Plugins`). Gradle no hace falta: el proyecto trae el wrapper `gradlew.bat`.
2. **Abrir el proyecto:** `File > Open…` → carpeta `TALLER_BLAZEDEMO` → *Trust project*. Esperar a que Gradle sincronice
   (barra inferior). Verificar en `File > Project Structure > Project` que el SDK sea 17.
3. **Compilar sin correr pruebas** (en la terminal de IntelliJ, `Alt+F12`, PowerShell):
   ```powershell
   .\gradlew.bat clean compileTestJava
   ```
4. **Ensayo de la demo** (debe terminar en `BUILD SUCCESSFUL`):
   ```powershell
   .\gradlew.bat clean test -Prunner=SmokeRunner
   ```
5. **Dejar abiertas** estas pestañas para no buscar en vivo: `build.gradle`, `serenity.conf`, `compra_vuelos.feature`,
   `BuscarVuelos.java`, `HomePage.java`, `Pausa.java`, `Precio.java`, `CompraVuelosStepDefinitions.java`, `BlazeDemoRunner.java`.
6. **Plan B** abierto en el navegador: `evidencias/reportes/suite-principal/index.html` y la carpeta `evidencias/videos/suite-principal/`.
7. Zoom del editor al 125 % o más: `Ctrl+Shift+A` → *Increase Font Size*, o *Presentation Mode* (`View > Appearance > Enter Presentation Mode`).

> ⚠️ En PowerShell las propiedades `-D…` van **entre comillas** (`"-Ddemo.delay=2000"`); sin comillas PowerShell las corta en el punto.

---

## 2. Recorrido por el scaffold (3 min)

| # | Mostrar | Dónde | Qué decir |
|---|---|---|---|
| 1 | Plugin del scaffold | `build.gradle` **línea 14**: `id 'io.github.jumosqu12.screenplayarchitecture' version '2.0.1'` | "Este es el plugin del taller; con él generamos el arquetipo." |
| 2 | Tareas del scaffold | Terminal: `.\gradlew.bat tasks --group "ScreenPlay Architecture"` | "Aquí están `spa`, `gft`, `grun`, `gtk`, `gpl`… las usamos todas menos las de base de datos." **No correr `spa` en vivo: sobrescribe `build.gradle`.** |
| 3 | Comandos usados | `README.md` **sección 1** (líneas 18–28) | "Así generamos la estructura, los features, el runner, las tasks y el pipeline." |
| 4 | Estructura generada | Panel *Project* (`Alt+1`) → `src/main/java/…/blazedemo/` | Expandir: `exceptions`, `interactions`, `models`, `questions`, `tasks`, `userinterfaces`, `utils`. "Un paquete por responsabilidad, tal como lo crea el scaffold." |
| 5 | Lado de pruebas | `src/test/java/…/blazedemo/` → `runners/blazedemo`, `stepdefinitions` | "El runner lo generó `grun`; los StepDefinitions conectan Gherkin con Screenplay." |
| 6 | Features | `src/test/resources/features/blazedemo/` (3 archivos) | "Las dos primeras salieron de `gft`; `cuenta_usuario` la agregamos después." |
| 7 | Pipeline del scaffold | `pipelineCi_Build.yaml` (raíz) y `.github/workflows/pruebas-e2e.yml` **línea 23** | "El scaffold generó el pipeline de Azure; además corremos GitHub Actions en modo headless." |
| 8 | Ajustes al scaffold | `build.gradle` **líneas 19, 36 y 102** | "Agregamos el plugin de reportes de Serenity, alineamos Serenity a 4.1.3 y activamos `generateOutcomes` para el detalle por escenario." |

---

## 3. Recorrido por Screenplay, de Gherkin al navegador (6 min)

Seguir **un solo escenario de punta a punta**: la compra E2E. Usar **Ctrl+clic** (o `Ctrl+B`) sobre cada paso para saltar al código.

### 3.1 Feature (Gherkin) y criterios de aceptación
- `src/test/resources/features/blazedemo/compra_vuelos.feature`
  - **Líneas 8–14:** criterios de aceptación CA5–CA10 ("escritos antes del código").
  - **Líneas 16–17:** `Background` → cada escenario abre la página desde cero (Independent).
  - **Línea 21:** `Scenario: Compra exitosa de un vuelo (ruta feliz E2E)` con la tabla de datos de las líneas 25–26.
  - **Líneas 31–44:** `Scenario Outline` con tag `@tarjetas-prueba` y `Examples` de 4 tarjetas de prueba: 1 Visa, 2 American Express y 1 Diner's Club, cada una con su número.
  - **Líneas 203–221:** escenarios `@known-issue`.
- `busqueda_vuelos.feature` **línea 136:** inyección de HTML (`@negative @security`).
- `cuenta_usuario.feature` **línea 28:** el registro exige todos sus campos (tabla de una columna).

### 3.2 Actor y escenario (StepDefinitions)
- `src/test/java/…/blazedemo/stepdefinitions/Hooks.java`
  - **Línea 16:** `OnStage.setTheStage(new OnlineCast())` → elenco nuevo en cada escenario.
  - **Línea 21:** `OnStage.drawTheCurtain()`.
- `BusquedaVuelosStepDefinitions.java`
  - **Línea 47:** `@Given("que {word} abre la página de BlazeDemo")` → **actor principal** (`theActorCalled(viajero)`, línea 49).
  - **Línea 52:** `@When("busca vuelos desde {string} hacia {string}")` → una sola línea que llama a la Task.
- `CompraVuelosStepDefinitions.java`
  - **Línea 36:** `elige el primer vuelo de la lista`.
  - **Línea 157:** `completa el pago con los siguientes datos` → `DataTable` convertido a `DatosPago`.
  - **Línea 169:** `completa el pago … con tarjeta {string} y número de prueba {string}` (paso del Outline de tarjetas).
  - **Línea 198:** `debe ver el mensaje {string}` → `seeThat(...)` (Self-validating).
  - **Línea 239:** paso del defecto conocido (precio de compra vs. precio elegido).
  - "Ningún paso tiene un localizador ni un `if`: solo orquestan Tasks y Questions."

### 3.3 Tasks (negocio)
- `src/main/java/…/blazedemo/tasks/BuscarVuelos.java`
  - **Línea 28:** `desde(...)` y **línea 51:** `hacia(...)` → se lee como una frase.
  - **Línea 33:** `@Step("{0} busca vuelos desde #origen hacia #destino")` → texto que aparece en el reporte.
  - **Líneas 35–40:** `attemptsTo(SeleccionarOpcion…, SeleccionarOpcion…, HacerClicEn…, WaitUntil…)`.
- `tasks/ElegirVuelo.java`
  - **Línea 29:** `enum Criterio { POSICION, MAS_BARATO, MAS_CARO }`.
  - **Líneas 61–63:** `actor.remember(...)` (memoria del actor) y `Serenity.recordReportData()` (datos en el reporte).
  - **Línea 66:** usa la Interaction `ElegirVueloDeLaFila`.
- `tasks/CompletarPago.java` **línea 55** (`@Step`), **línea 57** (recuerda el total antes de pagar), **línea 81** (clic en *Purchase Flight*).
- `tasks/ComprarVuelo.java` **líneas 31–33:** una Task compuesta por otras Tasks (reutilización).

### 3.4 Interactions ("la menuda")
- `interactions/HacerClicEn.java` **líneas 30–31:** `WaitUntil … isClickable()` y luego `Click.on(...)` → espera explícita, sin sleep.
- `interactions/Pausa.java`
  - **Línea 14:** `extends SilentInteraction` (no ensucia el reporte).
  - **Línea 16:** propiedad `demo.delay`; **línea 34:** `delayConfigurado()`; **línea 50:** el único `Thread.sleep` y solo si el valor es mayor a 0.
- `interactions/AgregarOpcionALista.java` **línea 35:** `executeScript(...)` → simula manipular el formulario con DevTools.

### 3.5 Targets (userinterfaces)
- `userinterfaces/HomePage.java` **línea 7** (`LISTA_ORIGEN`) y **línea 11** (`BTN_BUSCAR_VUELOS`).
- `userinterfaces/ReservePage.java`
  - **Línea 13:** `COLUMNA_PRECIO` por `td[6]` (la tabla tiene `<form>` dentro de `<tr>`).
  - **Línea 15:** `BTN_ELEGIR_VUELO_FILA` parametrizado `{0}` → los 5 botones son idénticos.
  - **Línea 20:** `ELEMENTO_INYECTADO` (prueba de inyección HTML).
- `userinterfaces/ConfirmationPage.java` **línea 10:** `DATO_CONFIRMACION` por etiqueta (`Id`, `Status`, `Amount`…).
- "Prefijo `css:` en los selectores: en Serenity 4.1 un CSS sin prefijo no se resuelve."

### 3.6 Questions (verificación)
- `questions/Precio.java`: **línea 29** `delVueloElegido()` (usa la memoria del actor), **línea 55** `precioMasImpuestos()`, **línea 76** `enLaPaginaDeCompra()`.
- `utils/ConvertirPrecio.java` **línea 11:** normaliza `"$472.56"`, `"Price: 400"`, `"555 USD"` a `BigDecimal`.
- `questions/VuelosListados.java` **línea 26:** lee la tabla y arma objetos `Vuelo`.

### 3.7 Modelos, datos y configuración
- `models/DatosPago.java` (Lombok `@Builder`), `models/Vuelo.java`.
- `utils/GeneradorDatos.java` **línea 27:** `pasajeroAleatorio(tipoTarjeta)` elige el número de prueba según el tipo (líneas 28–32); **línea 36:** sobrecarga que recibe el número desde el `Examples`.
- `src/test/resources/serenity.conf`
  - **Línea 4:** `take.screenshots = FOR_EACH_ACTION` (captura por acción).
  - **Línea 6:** `restart.browser.for.each = scenario` (Independent).
  - **Línea 20:** `demo.delay = 0` (Fast).
  - **Línea 27:** `headless.mode = false` (visible en la demo; CI usa `true`).
  - **Líneas 45–48:** URLs de las páginas (sin datos quemados en el código).

### 3.8 Runners y ejecución
- `runners/blazedemo/BlazeDemoRunner.java` **línea 12** `@RunWith(CucumberWithSerenity.class)`, **línea 15** `glue`, **línea 16** `tags = "@blazedemo and not @known-issue"`.
- `SmokeRunner` (`@smoke`), `NegativeRunner` (`@negative or @boundary`), `KnownIssuesRunner` (`@known-issue`).
- `build.gradle` **línea 72:** `-Prunner=...` elige el runner; **línea 86:** filtro; **línea 95:** `finalizedBy 'aggregate'` (el reporte se genera aunque algo falle).

---

## 4. Demo en vivo (4 min)

Abrir la terminal de IntelliJ (`Alt+F12`) en la raíz del proyecto. Ventana del navegador y del IDE lado a lado si es posible.

| Paso | Comando / acción | Qué se ve | Quién narra |
|---|---|---|---|
| 1 | `.\gradlew.bat clean test -Prunner=SmokeRunner "-Ddemo.delay=2000"` | Chrome se abre y recorre búsqueda → compra → confirmación con 2 s entre acciones (~1 min). En consola: `PASSED`. | **A** describe cada acción ("Carlos selecciona Paris… elige el primer vuelo… paga"). |
| 2 | `Invoke-Item .\target\site\serenity\index.html` | Reporte: 100 %, escenarios, tags. | **B** |
| 3 | En el reporte: *Features* → *Compra de vuelos* → *Compra exitosa de un vuelo* | Pasos Gherkin con captura por acción; abrir una captura. | **B** |
| 4 | Clic en el tag **Negative** del resumen | Filtro de escenarios negativos con su nombre "(hallazgo …)". | **A** explica por qué pasan. |
| 5 | (Opcional, ~1,5 min) `.\gradlew.bat clean test "-Dcucumber.filter.tags=@tarjetas-prueba"` | Las 4 compras con Visa, 2 Amex y Diners en verde. | **B** |
| 6 | (Opcional, 40 s) `.\gradlew.bat clean test -Prunner=KnownIssuesRunner` | 3 `FAILED` esperados; en el reporte: "Expected 200.98 but 400". | **A** |
| 7 | (Opcional) GitHub → pestaña **Actions** del repositorio | Historial de corridas de CI del repositorio (headless). | **B** |

### Comandos útiles (PowerShell, raíz del proyecto)

```powershell
# Suite principal completa (62 ejecuciones, ~5 min) + reporte
.\gradlew.bat clean test

# Solo la ruta feliz crítica (ideal para la demo)
.\gradlew.bat clean test -Prunner=SmokeRunner

# Modo sustentación: pausa de 2 s entre acciones
.\gradlew.bat clean test -Prunner=SmokeRunner "-Ddemo.delay=2000"

# Solo las 4 tarjetas de prueba (Visa, 2 Amex, Diners)
.\gradlew.bat clean test "-Dcucumber.filter.tags=@tarjetas-prueba"

# Un solo escenario por tags (ejemplo: el E2E de compra)
.\gradlew.bat clean test "-Dcucumber.filter.tags=@smoke and @compra"

# Por tipo
.\gradlew.bat clean test -Prunner=NegativeRunner
.\gradlew.bat clean test "-Dcucumber.filter.tags=@security"

# Defectos conocidos (fallan a propósito)
.\gradlew.bat clean test -Prunner=KnownIssuesRunner

# Sin ventana del navegador (como en CI)
.\gradlew.bat clean test "-Dheadless.mode=true"

# Abrir el reporte
Invoke-Item .\target\site\serenity\index.html

# Regenerar TODAS las evidencias (ambas corridas, reportes, grabaciones, capturas y zips, ~7 min)
.\generar-evidencias.ps1
```

### Correr desde IntelliJ (alternativa gráfica)
- Panel **Gradle** (derecha) → `Tasks > verification > test` → clic derecho → *Modify Run Configuration* → en *Run* agregar
  `-Prunner=SmokeRunner "-Ddemo.delay=2000"` → *Run*.
- Clic derecho sobre un escenario en el `.feature` → *Run 'Scenario: …'* también funciona, pero no genera el reporte agregado:
  para el reporte usar la tarea Gradle o la terminal.

---

## 5. Plan B y problemas comunes

| Problema en vivo | Solución inmediata |
|---|---|
| Sin internet o BlazeDemo lento | Abrir `evidencias/reportes/suite-principal/index.html` y una grabación de `evidencias/videos/suite-principal/` (por ejemplo `SUCCESS_Compra_exitosa_de_un_vuelo_ruta_feliz_E2E.gif`). |
| `SessionNotCreatedException` (versión de Chrome) | Actualizar Chrome y repetir: Selenium Manager descarga el driver correcto (`serenity.conf` línea 32, `autodownload = false`). |
| `Task '.delay=2000' not found` | Faltan las comillas: `"-Ddemo.delay=2000"`. |
| `clearReports FAILED` | Cerrar la carpeta `target` en el explorador o el reporte abierto y repetir. |
| El reporte no muestra el detalle | Correr con `clean test` completo (el `aggregate` va al final), no un escenario suelto desde el `.feature`. |

## 6. Evidencias para mostrar si preguntan

- Reporte principal: `evidencias/reportes/suite-principal/index.html` (62/62) · zip: `evidencias/reporte-serenity-suite-principal.zip`.
- Defectos: `evidencias/reportes/defectos-conocidos/index.html` (3 fallos esperados).
- Capturas: `evidencias/capturas/` (01–16 del reporte, `E2E_*` del flujo real).
- Grabaciones por ejecución: `evidencias/videos/suite-principal/` (62) y `evidencias/videos/defectos-conocidos/` (3).
- Logs: `evidencias/logs/` (incluye la comparación con y sin delay).
- Generador de grabaciones: `src/test/java/…/blazedemo/evidencias/GeneradorVideos.java` (línea 46, `main`) · tarea `build.gradle` línea 107 (`generarVideos`).
