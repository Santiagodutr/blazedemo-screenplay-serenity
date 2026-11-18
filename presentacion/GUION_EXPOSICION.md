# Guion de exposición – BlazeDemo con Screenplay (2 expositores)

Presentación: `presentacion/BlazeDemo_Screenplay_Sustentacion.pptx` (26 diapositivas).
Las notas del orador de cada diapositiva empiezan con `[Expositor A]` o `[Expositor B]`.

| | Expositor A | Expositor B |
|---|---|---|
| Bloques | Introducción, patrón Screenplay, resultados y hallazgos, conclusiones | Construcción y código, calidad (FIRST y retos), demo en vivo |
| Diapositivas | 1–7, 15–21, 25 | 8–14, 22–24, 26 + demo |
| Tiempo aprox. | 8 min | 7 min + 4 min de demo |

**Duración total:** ~15 min de exposición + 4 min de demo + preguntas.
Regla de oro: quien no habla controla el clic de las diapositivas y, durante la demo, narra lo que se ve en pantalla.

---

## Bloque 1 · Introducción y patrón (Expositor A · ~4 min)

### Diapositiva 1 – Portada (30 s)
**A:** "Buenos días. Somos [nombres] y les vamos a presentar la automatización E2E del sitio BlazeDemo, la opción 4 del taller.
Usamos el patrón Screenplay sobre Serenity BDD, Cucumber y Selenium, y generamos el proyecto con el scaffold que pidió el profesor."

### Diapositiva 2 – El proyecto en números (50 s)
**A:** "Antes de entrar en detalle, el resultado en cifras: escribimos 47 escenarios en Gherkin que, con sus tablas de ejemplos,
se convierten en 62 ejecuciones, y las 62 pasan. En el camino encontramos 9 hallazgos del sitio, 3 de ellos defectos reales de precio y pago.
Cada ejecución deja evidencia: 594 capturas y una grabación paso a paso por ejecución.
La suite completa tarda unos 8 minutos y no tiene ni un solo sleep fijo."

### Diapositiva 3 – El reto (40 s)
**A:** "El reto del taller era una E2E que busque vuelos, llene el formulario de pago y valide precios y pagos.
Estas son capturas reales que tomó la propia automatización: buscar, elegir, pagar y confirmar.
Nuestra decisión clave fue no quedarnos en 'llegar al final': validamos el precio en las tres pantallas."

### Diapositiva 4 – Sección 01 (5 s)
**A:** "Primero, el patrón."

### Diapositiva 5 – Cinco piezas (60 s)
**A:** "Screenplay modela la prueba como un guion de teatro. Hay un **actor**, Carlos el viajero, que tiene una **habilidad**: navegar con Chrome.
El actor realiza **tareas** de negocio, como 'buscar vuelos' o 'completar el pago'. Cada tarea se compone de **interacciones** pequeñas,
lo que el taller llama 'la menuda': seleccionar una opción, hacer clic. Y para verificar, el actor hace **preguntas** al sistema, como '¿cuál es el precio?'.
La regla mental: Task es un verbo de negocio, Interaction es un clic, Question es verificar y Target es dónde está el elemento."

### Diapositiva 6 – Screenplay vs Page Object (40 s)
**A:** "¿Por qué no Page Object clásico? Porque las clases por página crecen sin control y los pasos terminan mezclando lógica con localizadores.
En Screenplay cada clase tiene una sola responsabilidad, el código se lee como el negocio y el reporte de Serenity cuenta la intención, no los clics."

### Diapositiva 7 – Así se ve en nuestro proyecto (40 s)
**A:** "Un ejemplo de punta a punta: el actor Carlos ejecuta la Task BuscarVuelos, que usa las Interactions SeleccionarOpcion y HacerClicEn
sobre los Targets de la página de inicio, y al final la Question CantidadDeVuelos verifica el resultado.
Fíjense en el StepDefinition de abajo: solo orquesta, no tiene ni lógica ni localizadores. Le paso la palabra a [B] para que cuente cómo lo construimos."

---

## Bloque 2 · Construcción (Expositor B · ~5 min)

### Diapositiva 8 – Sección 02 (5 s)
**B:** "Gracias. Vamos a cómo lo construimos."

### Diapositiva 9 – Scaffold (50 s)
**B:** "El arquetipo lo generamos con el plugin del taller, ScreenplayArchitecture versión 2.0.1. Con la tarea `spa` creamos la estructura UX;
con `gft` los features, con `grun` el runner, con `gtk` las tasks y con `gpl` el pipeline.
El stack: Java 17, Gradle 8.10 con wrapper, Serenity 4.1.3, Cucumber en español, Selenium con Chrome, Datafaker para datos y GitHub Actions como integración continua."

### Diapositiva 10 – Un paquete por responsabilidad (40 s)
**B:** "Respetamos la estructura del scaffold: un paquete por responsabilidad. Tenemos 13 Tasks, 12 Questions, 7 Interactions propias y 7 clases de interfaz.
Los 36 localizadores viven solo en `userinterfaces`; ninguna Task ni StepDefinition tiene un selector escrito a mano."

### Diapositiva 11 – Primero el Gherkin (50 s)
**B:** "El taller pide que las Tasks salgan de criterios de aceptación escritos antes. Así lo hicimos: 12 criterios, CA1 a CA12, y después el código.
Este es el escenario E2E principal con su tabla de datos. Usamos las palabras clave de Gherkin en inglés —Feature, Scenario, Given, When, Then— y el texto de los pasos en español.
La dona muestra cómo se reparten las 62 ejecuciones: 20 de ruta feliz, 27 alternas, 8 negativas y 7 de límite, más 3 escenarios de defectos conocidos que corremos aparte."

### Diapositiva 12 – Mapa de la batería (50 s)
**B:** "Organizamos la batería en tres features. Búsqueda, con 20 escenarios: rutas, listas de ciudades, aerolíneas, formato de precios, destino de la semana e inyección de HTML.
Compra, con 22: el E2E, cuatro tarjetas de prueba —una Visa, dos American Express y una Diners, cada una con su número—, la suma de impuestos, compras repetidas y todos los casos de datos inválidos.
Y Cuenta, con 5, donde validamos el login, el registro y la recuperación sin enviar credenciales al sitio real."

### Diapositiva 13 – Una Task (40 s)
**B:** "Así se ve una Task por dentro. La anotación @Step hace que el reporte muestre 'Carlos busca vuelos desde Paris hacia Rome'.
Se compone de Interactions y sincroniza con una espera explícita, WaitUntil, en lugar de dormir el hilo. Y se usa como una frase: `BuscarVuelos.desde("Paris").hacia("Rome")`."

### Diapositiva 14 – Interaction y Question (40 s)
**B:** "Dos piezas propias. La Interaction Pausa es el delay que pide la socialización: es configurable y por defecto vale cero, así la batería sigue siendo rápida.
La Question Precio normaliza textos como '$472.56', 'Price: 400' o '555 USD' a BigDecimal para comparar sin errores de formato. [A] les muestra los resultados."

---

## Bloque 3 · Resultados y hallazgos (Expositor A · ~4 min)

### Diapositiva 15 – Sección 03 (5 s)
**A:** "Resultados y evidencia."

### Diapositiva 16 – Métricas de ejecución (45 s)
**A:** "Las 62 ejecuciones pasan. La última corrida completa tardó 8 minutos y 3 segundos, con un promedio de 4 segundos por ejecución,
y las cuatro compras con tarjetas de prueba —Visa, dos Amex y Diners— pasan las cuatro.
El gráfico muestra cómo se reparte cada feature entre feliz, alternos, negativos y límite. Además la suite corre en GitHub Actions sin ventana del navegador."

### Diapositiva 17 – Reporte Serenity (30 s)
**A:** "Este es el reporte de Serenity: el resumen con el 100 % y, a la derecha, el detalle de un escenario con cada paso Gherkin y su captura. En la demo lo vamos a abrir en vivo."

### Diapositiva 18 – Evidencias audiovisuales (30 s)
**A:** "Además del reporte, cada ejecución queda grabada paso a paso. Aquí ven la compra completa en movimiento.
Son 594 capturas y 65 grabaciones, y nos sirven de plan B si en la demo falla internet."

### Diapositiva 19 – Delay entre pasos (30 s)
**A:** "El taller pide pausas entre pasos para ver la prueba. Con `-Ddemo.delay=1000` el mismo escenario pasa de 25 a 40 segundos:
son 15 pausas, una por acción. Por defecto vale cero, así no afecta la velocidad."

### Diapositiva 20 – 3 defectos reales (50 s)
**A:** "Lo más interesante: tres defectos reales. Elegimos un vuelo de 200,98 y la página de compra cobra 400.
El total es 914,76, pero la confirmación dice 555. Y escribimos una tarjeta que termina en 0004, pero la confirmación muestra 1111.
Estos escenarios afirman lo que el sitio debería hacer, por eso fallan, y los separamos con la etiqueta @known-issue."

### Diapositiva 21 – Seis hallazgos más (50 s)
**A:** "Y seis hallazgos más que tienen algo en común: el sitio solo valida en el navegador. Si inyectamos HTML en la ciudad de origen, el título lo interpreta.
Si abrimos la confirmación por la URL, nos confirma una compra que nunca hicimos. El formulario acepta mes 13, año vencido y letras en el código postal.
Estos escenarios sí pasan, porque afirman el comportamiento actual y lo documentan como hallazgo. [B] cierra con la calidad de la batería."

---

## Bloque 4 · Calidad (Expositor B · ~2 min)

### Diapositiva 22 – Sección 04 (5 s)
**B:** "Calidad de la batería."

### Diapositiva 23 – F.I.R.S.T. (60 s)
**B:** "Aplicamos F.I.R.S.T. con ejemplos concretos.
**Fast:** la pausa vale cero por defecto y usamos esperas explícitas.
**Independent:** cada escenario abre la página desde el Background, con un navegador limpio y un actor nuevo.
**Repeatable:** no dependemos de usuarios ni de datos guardados; da 62 de 62 en corridas repetidas y también corre en GitHub Actions.
**Self-validating:** cada 'Then' es una aserción con seeThat, nadie revisa a ojo.
**Timely:** el Gherkin se escribió antes que el código."

### Diapositiva 24 – Retos que nos puso BlazeDemo (50 s)
**B:** "El sitio nos puso retos propios. Los cinco botones de la tabla son idénticos, así que el Target recibe el número de fila.
La tabla tiene HTML inválido, con formularios dentro de las filas, por eso localizamos las columnas por td.
Hay tres formatos de precio, que normalizamos. Y como la validación es solo de interfaz, creamos una Interaction que inyecta opciones con JavaScript para probar el servidor."

---

## Bloque 5 · Cierre (A y B · ~1 min + demo)

### Diapositiva 25 – Conclusiones (45 s)
**A:** "Para cerrar: 62 de 62 en verde, 47 escenarios, 9 hallazgos y 594 capturas.
Aprendimos tres cosas. Screenplay escala: pasamos de 12 a 47 escenarios reutilizando las mismas Tasks. Escribir el Gherkin primero nos obligó a pensar en todos los caminos.
Y validar el negocio, no solo navegar, es lo que encuentra defectos."

### Diapositiva 26 – Demo en vivo y preguntas
**B:** "Ahora lo vemos funcionando." → seguir `GUION_DEMO_IDE.md` (sección 4, demo de 4 minutos).
**A** narra mientras B ejecuta: "Fíjense cómo Carlos selecciona la ciudad, espera… y el reporte registra cada paso."

---

## Preguntas probables y quién responde

| Pregunta | Responde | Respuesta corta |
|---|---|---|
| ¿Diferencia entre Task e Interaction? | A | La Task es una acción de negocio (BuscarVuelos); la Interaction es atómica sobre la UI (HacerClicEn). Una Task se compone de Interactions. |
| ¿Dónde viven los localizadores y por qué? | B | Solo en `userinterfaces` (Targets). Si cambia la página se cambia un solo archivo. |
| ¿Cómo evitaron los sleep fijos? | B | Esperas explícitas `WaitUntil`. La única pausa es `Pausa`, que vale 0 por defecto. |
| ¿Cómo garantizan independencia? | B | `Background` en cada feature, `OnlineCast` nuevo en `Hooks` y navegador nuevo por escenario. Estado en la memoria del actor, sin variables estáticas. |
| ¿Qué pasa con los negativos si el sitio no valida? | A | No es fallo de la prueba, es un hallazgo: el escenario afirma lo que hace hoy el sitio y su nombre lo marca como hallazgo. Lo que debería pasar va en `@known-issue`. |
| ¿Por qué fallan 3 escenarios? | A | Son defectos del sitio (precio 400, monto 555, tarjeta 1111). Están aislados en `KnownIssuesRunner` para que la suite principal siga en verde. |
| ¿Usaron el scaffold de verdad? | B | Sí: `spa`, `gft`, `grun`, `gtk` y `gpl`. Se muestra en `build.gradle` línea 14 y en la estructura de paquetes. |
| ¿Por qué Gherkin con palabras clave en inglés? | B | Lo unificamos en la rama `entrega/gherkin-reportes-serenity`: `Given/When/Then` es el estándar y el texto de negocio sigue en español. Cucumber admite ambos. |
| ¿Por qué no prueban login real? | B | Para no enviar credenciales ni crear cuentas en un sitio real. Se valida la estructura y los campos obligatorios. |

## Checklist antes de exponer
- [ ] Nombres del equipo en la portada (diapositiva 1).
- [ ] Ensayo con cronómetro: 15 min + 4 de demo.
- [ ] Demo probada 2 veces ese mismo día (ver `GUION_DEMO_IDE.md`, sección 1).
- [ ] Plan B abierto en una pestaña: `evidencias/reportes/suite-principal/index.html`.
- [ ] Presentación en modo pantalla completa; zoom del IDE y del navegador al 125 % o más.
