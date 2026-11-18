# BlazeDemo – Automatización E2E con Screenplay + Serenity BDD

Taller de automatización · Calidad de Software 2026-2 · Sitio **#4 BlazeDemo** (<https://blazedemo.com/>)

Prueba E2E que **busca vuelos, elige uno, llena el formulario de pago y valida precios y pagos**, con el patrón
**Screenplay**, **Cucumber (Gherkin en español)** y reportes **Serenity BDD**.

| Resultado | Valor |
|---|---|
| Suite principal (`BlazeDemoRunner`) | **61 / 61 ejecuciones en verde**: 47 escenarios en 3 features, 4 Scenario Outline (18 filas de Examples) |
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
├─ userinterfaces/  Targets (Page Objects): HomePage, ReservePage, PurchasePage, ConfirmationPage, VacationPage,
│                   CuentaPage, BarraNavegacion
├─ interactions/    "La menuda": SeleccionarOpcion, LlenarCampo, HacerClicEn, MarcarCasilla,
│                   ElegirVueloDeLaFila, AgregarOpcionALista, Pausa (delay configurable)
├─ tasks/           Negocio: AbrirBlazeDemo, BuscarVuelos, BuscarVuelosPorDefecto, ElegirVuelo, CompletarPago,
│                   ComprarVuelo, AnotarNumeroDeCompra, ConsultarDestinoDeLaSemana, ForzarBusqueda,
│                   InyectarHtmlEnLaBusqueda, AbrirPaginaDirecta, NavegarPorLaBarra, RecuperarContrasena
├─ questions/       Verificaciones: VuelosListados, CantidadDeVuelos, Precio, DatoDeLaConfirmacion, TextoEnPantalla,
│                   TodosLosPrecios, CiudadesDisponibles, TablaDeVuelos, PaginaActual, CamposObligatorios,
│                   FormularioDePago, NumeroDeCompra
├─ models/          DatosPago (Lombok @Builder), Vuelo
├─ exceptions/      VueloNoDisponibleException
└─ utils/           Constantes, ConvertirPrecio, Fechas, GeneradorDatos (Datafaker)
src/test/java/co/com/equipo/certificacion/blazedemo/
├─ stepdefinitions/ Hooks (OnStage/OnlineCast), BusquedaVuelos-, CompraVuelos- y CuentaUsuarioStepDefinitions
├─ runners/blazedemo/ BlazeDemoRunner, SmokeRunner, NegativeRunner, KnownIssuesRunner
└─ evidencias/      GeneradorVideos (GIF por escenario)
src/test/resources/
├─ features/blazedemo/ busqueda_vuelos.feature, compra_vuelos.feature, cuenta_usuario.feature
└─ serenity.conf
```

Flujo: **Actor** (Carlos, con la habilidad `BrowseTheWeb`) → **Task** (`BuscarVuelos.desde("Paris").hacia("Rome")`)
→ **Interactions** (`SeleccionarOpcion`, `HacerClicEn`…) → **Question** (`Precio.delVueloElegido()`…) con `seeThat(...)`.

Reglas cumplidas: localizadores solo en `userinterfaces`; StepDefinitions sin lógica (solo llaman Tasks/Questions);
métodos fábrica que se leen como frases; datos en Gherkin, `Examples`, modelos o Datafaker; URL en `serenity.conf`;
Javadoc en cada clase.

## 3. Escenarios (Gherkin)

**47 escenarios** (61 ejecuciones) en la suite principal + 3 `@known-issue`. Por tipo de ejecución: 19 ruta feliz,
27 alternos, 8 negativos y 7 límite (3 de ellos también `@security`). Por feature: búsqueda 28, compra 28, cuenta 5.

| Feature | Escenarios destacados |
|---|---|
| `busqueda_vuelos.feature` (20) | Outline de 7 rutas · ciudades por defecto · las 7 ciudades de origen y de destino · Outline "siempre 5 vuelos" · encabezados Departs/Arrives · aerolíneas · números de vuelo únicos · formato `$0.00` · rango 200.98–765.32 · destino de la semana (texto e imagen) · logo y enlace "home" · resultados sin buscar · inyección HTML · listas disjuntas · origen = destino forzado |
| `compra_vuelos.feature` (22) | E2E con tabla de datos · Outline de tarjetas · fecha de hoy en la confirmación · monto en USD · dos compras con Ids distintos · total = precio + impuestos · Outline "cualquiera de los 5 vuelos" · más barato · más caro · Remember me · tildes y eñes · datos mínimos · títulos de pestaña · formulario vacío · espacios en blanco · tarjeta "abcd" · mes 13 · código postal con letras · confirmación sin comprar · año 2019 · nombre de 255 caracteres · fecha precargada vencida |
| `cuenta_usuario.feature` (5) | Login exige correo y contraseña · ir al registro · el registro exige sus 5 campos · recuperar contraseña · la recuperación exige el correo (sin enviar credenciales ni crear cuentas) |
| `@known-issue` (3) | Precio compra = precio elegido · Monto confirmación = total · Últimos 4 dígitos de la tarjeta |

### Hallazgos sobre BlazeDemo (documentados en los escenarios)

1. **La página de compra está fija**: sin importar el vuelo elegido, siempre muestra TLV→SFO, *United UA954*,
   `Price: 400` y `Total Cost: 914.76`. → `@known-issue` (espera 200.98 y recibe 400).
2. **El monto confirmado no coincide**: la confirmación siempre dice `Amount: 555 USD` (el total era 914.76). → `@known-issue`.
3. **La confirmación ignora la tarjeta ingresada**: siempre muestra `xxxxxxxxxxxx1111` y vence `11/2018`. → `@known-issue`.
4. **No hay validación del formulario de pago**: acepta campos vacíos, solo espacios, tarjeta `abcd`, mes 13, año 2019 y
   código postal con letras; además precarga una fecha ya vencida (11/2017).
5. **Inyección de HTML**: el título de resultados muestra el origen sin escapar; una etiqueta inyectada se interpreta como HTML.
6. **Confirmación sin compra**: `confirmation.php` abierta directamente muestra "Thank you for your purchase" y un Id.
7. **Resultados sin búsqueda**: `reserve.php` abierta directamente lista vuelos "from  to".
8. **Validación solo en la interfaz**: la UI no permite origen = destino, pero el servidor acepta Paris→Paris.
9. **Navegación**: el enlace "home" de la barra lleva al inicio de sesión, no a la búsqueda.

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
| `reportes/suite-principal/index.html` | Reporte histórico de la suite principal (61/61), con 494 capturas paso a paso |
| `reportes/defectos-conocidos/index.html` | Reporte de los 3 defectos del sitio |
| `reporte-serenity-tarjetas-prueba.zip` | Corrida reciente de tarjetas con 4/4 casos aprobados en navegador. Extraer y abrir `index.html`. |
| `reporte-serenity-suite-principal.zip` y `reporte-serenity-defectos-conocidos.zip` | Copias comprimidas de los reportes históricos |
| `videos/suite-principal/*.gif` | **61 videos GIF**, uno por ejecución, con paso Gherkin y resultado sobreimpresos |
| `videos/defectos-conocidos/*.gif` | 3 GIFs de los defectos |
| `capturas/01…16_*.png` | Capturas del reporte: resumen, ruta feliz, Outlines, hallazgos, defectos, cuenta |
| `capturas/E2E_1…4_*.png` | Pantallas reales del flujo: home → resultados → pago → confirmación |
| `logs/corrida-*.log` | Salida de consola de cada corrida (incluye con y sin delay) |

Prueba del delay: el mismo escenario E2E tardó **25 s sin pausa** y **40 s con `-Ddemo.delay=1000`**
(`logs/corrida-demo-sin-delay.log` y `logs/corrida-demo-con-delay-1000ms.log`).

## 6. F.I.R.S.T. aplicado

| Principio | Cómo se cumple en este proyecto (ejemplo concreto) |
|---|---|
| **F**ast | `demo.delay = 0` por defecto en `serenity.conf`: la `Pausa` no duerme salvo en la sustentación. Sincronización con esperas explícitas (`WaitUntil.the(BTN_COMPRAR, isVisible()).forNoMoreThan(15).seconds()` en `ElegirVuelo`), nunca `sleep` fijos. Soporta `headless.mode=true`. Suite completa (61 ejecuciones): 5 min 34 s, mediana 1,7 s por escenario. |
| **I**ndependent | Cada escenario arranca desde `Background` (abre la página), `Hooks` crea un `OnlineCast` nuevo y `restart.browser.for.each = scenario` da un navegador limpio. No hay variables estáticas compartidas: el estado vive en la memoria del actor (`actor.remember/recall`). Los runners por tag ejecutan cualquier subconjunto en cualquier orden. |
| **R**epeatable | Sin usuarios ni datos persistentes. Los datos aleatorios de Datafaker siempre son válidos (tarjeta de prueba, vencimiento futuro), así que el resultado esperado no cambia. URL en `serenity.conf`, versiones fijas en `build.gradle` y wrapper de Gradle: mismo resultado en cualquier máquina o en CI (GitHub Actions). Se corrió la suite varias veces con el mismo resultado. |
| **S**elf-validating | Cada `Then` usa `seeThat(...)` con matchers de Hamcrest (`equalTo`, `comparesEqualTo`, `greaterThan`, `hasItem`…). El resultado es PASSED/FAILED sin revisión manual; los precios se normalizan a `BigDecimal` (`ConvertirPrecio`). |
| **T**imely | Los criterios de aceptación (CA1–CA12) y los features se escribieron antes que el código; las Tasks salen de esos criterios. Los hallazgos del sitio se convirtieron en escenarios `@known-issue` en cuanto se detectaron. |

## 7. Creatividad e innovación

- Validación de precios en **tres pantallas** (lista → compra → confirmación) que destapa 3 defectos reales.
- **Prueba de manipulación del formulario** (`AgregarOpcionALista` con JavaScript): demuestra que la validación es solo de interfaz.
- **Datos aleatorios** con Datafaker y elección del **vuelo más barato / más caro** calculado desde la tabla.
- **Pruebas de seguridad básicas**: inyección de HTML, páginas abiertas fuera del flujo y formulario manipulado.
- **Videos GIF automáticos por escenario** (`gradlew generarVideos`), hechos solo con el JDK a partir de las capturas de Serenity.
- `Serenity.recordReportData()`: el reporte adjunta el vuelo elegido y los datos de pago enviados.
- Runners por tag, script de evidencias de un clic, **GitHub Actions** headless (`.github/workflows/pruebas-e2e.yml`)
  y el pipeline Azure generado por el scaffold.

## 8. Tips para la sustentación

- Demo en vivo: `gradlew.bat clean test -Prunner=SmokeRunner "-Ddemo.delay=2000"` (navegador visible, ~1 min).
- Plan B: abrir `evidencias/reportes/suite-principal/index.html` y los GIF de `evidencias/videos/`.
- Para grabar la pantalla durante la demo: **Win + Alt + R** (Xbox Game Bar de Windows).
