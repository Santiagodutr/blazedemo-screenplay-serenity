# BlazeDemo – Automatización E2E con Screenplay + Serenity BDD

Taller de automatización · Calidad de Software 2026-2 · Sitio **#4 BlazeDemo** (<https://blazedemo.com/>)

Prueba E2E que **busca vuelos, elige uno, llena el formulario de pago y valida precios y pagos**, con el patrón
**Screenplay**, **Cucumber (Gherkin en español)** y reportes **Serenity BDD**.

| Resultado | Valor |
|---|---|
| Suite principal (`BlazeDemoRunner`) | **20 / 20 escenarios en verde** (12 escenarios, 2 Scenario Outline) |
| Defectos conocidos (`KnownIssuesRunner`) | 3 escenarios que **fallan a propósito** y documentan defectos del sitio |
| Reporte | `target/site/serenity/index.html` (copia en `evidencias/reportes/`) |

---

## 1. Generación con el scaffold (ScreenplayArchitecture)

El proyecto se generó con el plugin del taller `io.github.jumosqu12.screenplayarchitecture` v2.0.1
(<https://github.com/jumosqu12/ScreenplayArchitecture>), usando sus tareas:

```bash
gradle spa --projectName=BlazeDemoScreenplay --groupId=co.com.equipo.certificacion --principalPackage=blazedemo --type=UX
gradle gft --name=compra_vuelos   --nameSubFolder=blazedemo --examples=true   # features
gradle gft --name=busqueda_vuelos --nameSubFolder=blazedemo --examples=true
gradle grun --name=BlazeDemoRunner --folderName=blazedemo                     # runner
gradle gtk --name=BuscarVuelos --typeTask=Ux                                  # tasks (también ElegirVuelo, CompletarPago, AbrirBlazeDemo)
gradle gpl --name=pipelineCi --type=azure                                     # pipeline CI (pipelineCi_Build.yaml)
```

Ajustes sobre lo generado (documentados en `build.gradle`):
- Plugin `net.serenity-bdd.serenity-gradle-plugin` para la tarea `aggregate` (reporte HTML) con `generateOutcomes = true`.
- Serenity alineado a **4.1.3** en todas las dependencias (el scaffold trae 4.1.0, pero el plugin de reportes 4.1.0 no
  está publicado; mezclar 4.1.0 con 4.1.3 hacía que no se generaran las páginas de detalle).
- Se quitó `hamcrest-all 1.3` (choca con el Hamcrest 2 de Serenity) y se agregó Datafaker.
- Localizadores CSS con prefijo `css:`: en Serenity 4.1 un selector CSS sin prefijo no se resuelve.

## 2. Arquitectura Screenplay

```
src/main/java/co/com/equipo/certificacion/blazedemo/
├─ userinterfaces/  Targets (Page Objects): HomePage, ReservePage, PurchasePage, ConfirmationPage, VacationPage
├─ interactions/    "La menuda": SeleccionarOpcion, LlenarCampo, HacerClicEn, MarcarCasilla,
│                   ElegirVueloDeLaFila, AgregarOpcionALista, Pausa (delay configurable)
├─ tasks/           Negocio: AbrirBlazeDemo, BuscarVuelos, ElegirVuelo, CompletarPago,
│                   ConsultarDestinoDeLaSemana, ForzarBusqueda
├─ questions/       Verificaciones: VuelosListados, CantidadDeVuelos, Precio, DatoDeLaConfirmacion,
│                   TextoEnPantalla, TodosLosPrecios, CiudadesDisponibles
├─ models/          DatosPago (Lombok @Builder), Vuelo
├─ exceptions/      VueloNoDisponibleException
└─ utils/           Constantes, ConvertirPrecio, GeneradorDatos (Datafaker)
src/test/java/co/com/equipo/certificacion/blazedemo/
├─ stepdefinitions/ Hooks (OnStage/OnlineCast), BusquedaVuelosStepDefinitions, CompraVuelosStepDefinitions
├─ runners/blazedemo/ BlazeDemoRunner, SmokeRunner, NegativeRunner, KnownIssuesRunner
└─ evidencias/      GeneradorVideos (GIF por escenario)
src/test/resources/
├─ features/blazedemo/ busqueda_vuelos.feature, compra_vuelos.feature
└─ serenity.conf
```

Flujo: **Actor** (Carlos, con la habilidad `BrowseTheWeb`) → **Task** (`BuscarVuelos.desde("Paris").hacia("Rome")`)
→ **Interactions** (`SeleccionarOpcion`, `HacerClicEn`…) → **Question** (`Precio.delVueloElegido()`…) con `seeThat(...)`.

Reglas cumplidas: localizadores solo en `userinterfaces`; StepDefinitions sin lógica (solo llaman Tasks/Questions);
métodos fábrica que se leen como frases; datos en Gherkin, `Examples`, modelos o Datafaker; URL en `serenity.conf`;
Javadoc en cada clase.

## 3. Escenarios (Gherkin)

| Tag | Escenario | Tipo |
|---|---|---|
| `@happy @smoke` | Búsqueda de vuelos para varias rutas (**Outline**, 7 rutas) | Ruta feliz |
| `@happy @smoke` | Compra exitosa de un vuelo (E2E con tabla de datos) | Ruta feliz |
| `@happy` | Compra con distintos tipos de tarjeta y pasajero aleatorio (**Outline**: Visa, Amex, Diner's; vuelos 1, 3 y 5) | Ruta feliz |
| `@alternate` | Elegir el vuelo más barato · Rango de precios · Remember me · Destino de la semana | Alternos |
| `@negative` | Formulario vacío · Número de tarjeta inválido ("abcd") | Excepcionales |
| `@boundary` | Tarjeta vencida (2019) · La UI no ofrece origen = destino | Límite |
| `@boundary @security` | Origen = destino forzando el formulario (se inyecta la opción con JS) | Límite / seguridad |
| `@known-issue` | Precio compra = precio elegido · Monto confirmación = total · Últimos 4 dígitos de la tarjeta | Defectos |

### Hallazgos sobre BlazeDemo (documentados en los escenarios)

1. **La página de compra está fija**: sin importar el vuelo elegido, siempre muestra TLV→SFO, *United UA954*,
   `Price: 400` y `Total Cost: 914.76`. → `@known-issue` (espera 200.98 y recibe 400).
2. **El monto confirmado no coincide**: la confirmación siempre dice `Amount: 555 USD` (el total era 914.76). → `@known-issue`.
3. **La confirmación ignora la tarjeta ingresada**: siempre muestra `xxxxxxxxxxxx1111` y vence `11/2018`. → `@known-issue`.
4. **No hay validación del formulario**: acepta campos vacíos, tarjeta `abcd` y año vencido 2019 → los escenarios
   `@negative`/`@boundary` afirman el comportamiento actual y se nombran "(hallazgo - …)".
5. **Validación solo en la interfaz**: la UI no permite origen = destino (las listas no se cruzan), pero si se manipula
   el formulario el servidor acepta Paris→Paris y devuelve vuelos.
6. Todas las rutas devuelven los mismos 5 vuelos y precios.

## 4. Cómo ejecutar

Requisitos: JDK 17, Google Chrome. Gradle no hace falta (usa el wrapper `gradlew`).

```bash
gradlew.bat clean test
```
- Suite principal (sin defectos conocidos). El reporte queda en `target/site/serenity/index.html`.

```bash
gradlew.bat clean test -Prunner=SmokeRunner
```
- Otros runners: `NegativeRunner`, `KnownIssuesRunner`.

```bash
gradlew.bat clean test "-Dcucumber.filter.tags=@alternate"
```
- Filtrar por tag. **En PowerShell las propiedades `-D…` van entre comillas**, porque si no PowerShell las corta en el punto.

```bash
gradlew.bat clean test -Prunner=SmokeRunner "-Ddemo.delay=2000"
```
- **Modo sustentación**: pausa de 2 s después de cada acción para ver la prueba con detenimiento.

```bash
gradlew.bat clean test "-Dheadless.mode=true"
```
- Sin ventana del navegador (CI).

```powershell
.\generar-evidencias.ps1
```
- Regenera todas las evidencias (ambas corridas, reportes, GIFs, capturas y zips).

## 5. Evidencias (`evidencias/`)

| Carpeta / archivo | Contenido |
|---|---|
| `reportes/suite-principal/index.html` | Reporte Serenity de la corrida exitosa (20/20), con capturas de cada paso |
| `reportes/defectos-conocidos/index.html` | Reporte de los 3 defectos del sitio |
| `reporte-serenity-*.zip` | Los dos reportes comprimidos para entregar |
| `videos/suite-principal/*.gif` | **20 videos GIF**, uno por escenario/ejemplo, con paso Gherkin y resultado sobreimpresos |
| `videos/defectos-conocidos/*.gif` | 3 GIFs de los defectos |
| `capturas/01…10_*.png` | Capturas del reporte: resumen, ruta feliz, Outlines, hallazgos, defectos |
| `capturas/E2E_1…4_*.png` | Pantallas reales del flujo: home → resultados → pago → confirmación |
| `logs/corrida-*.log` | Salida de consola de cada corrida (incluye con y sin delay) |

Prueba del delay: el mismo escenario E2E tardó **25 s sin pausa** y **40 s con `-Ddemo.delay=1000`**
(`logs/corrida-demo-sin-delay.log` y `logs/corrida-demo-con-delay-1000ms.log`).

## 6. F.I.R.S.T. aplicado

| Principio | Cómo se cumple en este proyecto (ejemplo concreto) |
|---|---|
| **F**ast | `demo.delay = 0` por defecto en `serenity.conf`: la `Pausa` no duerme salvo en la sustentación. Sincronización con esperas explícitas (`WaitUntil.the(BTN_COMPRAR, isVisible()).forNoMoreThan(15).seconds()` en `ElegirVuelo`), nunca `sleep` fijos. Soporta `headless.mode=true`. Suite completa: ~2 min, promedio 2 s por escenario. |
| **I**ndependent | Cada escenario arranca desde `Antecedentes` (abre la página), `Hooks` crea un `OnlineCast` nuevo y `restart.browser.for.each = scenario` da un navegador limpio. No hay variables estáticas compartidas: el estado vive en la memoria del actor (`actor.remember/recall`). Los runners por tag ejecutan cualquier subconjunto en cualquier orden. |
| **R**epeatable | Sin usuarios ni datos persistentes. Los datos aleatorios de Datafaker siempre son válidos (tarjeta de prueba, vencimiento futuro), así que el resultado esperado no cambia. URL en `serenity.conf`, versiones fijas en `build.gradle` y wrapper de Gradle: mismo resultado en cualquier máquina o en CI (GitHub Actions). Se corrió la suite varias veces con el mismo resultado. |
| **S**elf-validating | Cada `Entonces` usa `seeThat(...)` con matchers de Hamcrest (`equalTo`, `comparesEqualTo`, `greaterThan`, `hasItem`…). El resultado es PASSED/FAILED sin revisión manual; los precios se normalizan a `BigDecimal` (`ConvertirPrecio`). |
| **T**imely | Los criterios de aceptación (CA1–CA8) y los features se escribieron antes que el código; las Tasks salen de esos criterios. Los hallazgos del sitio se convirtieron en escenarios `@known-issue` en cuanto se detectaron. |

## 7. Creatividad e innovación

- Validación de precios en **tres pantallas** (lista → compra → confirmación) que destapa 3 defectos reales.
- **Prueba de manipulación del formulario** (`AgregarOpcionALista` con JavaScript): demuestra que la validación es solo de interfaz.
- **Datos aleatorios** con Datafaker y elección del **vuelo más barato** calculado desde la tabla.
- **Videos GIF automáticos por escenario** (`gradlew generarVideos`), hechos solo con el JDK a partir de las capturas de Serenity.
- `Serenity.recordReportData()`: el reporte adjunta el vuelo elegido y los datos de pago enviados.
- Runners por tag, script de evidencias de un clic, **GitHub Actions** headless (`.github/workflows/pruebas-e2e.yml`)
  y el pipeline Azure generado por el scaffold.

## 8. Tips para la sustentación

- Demo en vivo: `gradlew.bat clean test -Prunner=SmokeRunner "-Ddemo.delay=2000"` (navegador visible, ~1 min).
- Plan B: abrir `evidencias/reportes/suite-principal/index.html` y los GIF de `evidencias/videos/`.
- Para grabar la pantalla durante la demo: **Win + Alt + R** (Xbox Game Bar de Windows).
