# language: en
@blazedemo @busqueda
Feature: Búsqueda de vuelos en BlazeDemo
  Como viajero
  Quiero buscar vuelos entre una ciudad de origen y una de destino
  Para conocer los vuelos disponibles y sus precios

  Criterios de aceptación:
  - CA1: El usuario puede buscar vuelos entre un origen y un destino de las listas ofrecidas.
  - CA2: El sistema muestra una lista de vuelos, cada uno con número único, aerolínea y precio en dólares.
  - CA3: El título y los encabezados de resultados indican el origen y el destino buscados.
  - CA4: El usuario puede consultar el destino de la semana y navegar con la barra superior.

  Background:
    Given que Carlos abre la página de BlazeDemo

  # ------------------------------- Ruta feliz -------------------------------
  @happy @smoke
  Scenario Outline: Búsqueda de vuelos para varias rutas
    When busca vuelos desde "<origen>" hacia "<destino>"
    Then debe ver una lista de vuelos disponibles
    And el título de la página de resultados debe contener "<origen>" y "<destino>"
    And todos los vuelos listados deben tener un precio mayor a cero

    Examples:
      | origen       | destino      |
      | Paris        | Buenos Aires |
      | Boston       | London       |
      | Mexico City  | Cairo        |
      | San Diego    | Berlin       |
      | São Paolo    | New York     |
      | Portland     | Dublin       |
      | Philadelphia | Rome         |

  @happy
  Scenario: Búsqueda con las ciudades que vienen seleccionadas por defecto
    When busca vuelos sin cambiar las ciudades seleccionadas
    Then el título de la página de resultados debe contener "Paris" y "Buenos Aires"

  @happy
  Scenario: La lista de origen ofrece las siete ciudades de salida
    Then la lista de origen debe ofrecer las ciudades
      | Paris        |
      | Philadelphia |
      | Boston       |
      | Portland     |
      | San Diego    |
      | Mexico City  |
      | São Paolo    |

  @happy
  Scenario: La lista de destino ofrece las siete ciudades de llegada
    Then la lista de destino debe ofrecer las ciudades
      | Buenos Aires |
      | Rome         |
      | London       |
      | Berlin       |
      | New York     |
      | Dublin       |
      | Cairo        |

  # ------------------------------- Caminos alternos -------------------------------
  @alternate
  Scenario Outline: Cada búsqueda muestra exactamente cinco vuelos
    When busca vuelos desde "<origen>" hacia "<destino>"
    Then debe ver exactamente 5 vuelos

    Examples:
      | origen      | destino  |
      | Paris       | Rome     |
      | São Paolo   | Cairo    |
      | Mexico City | New York |

  @alternate
  Scenario: Los encabezados de la tabla indican la salida y la llegada
    When busca vuelos desde "San Diego" hacia "Dublin"
    Then los encabezados de la tabla deben indicar salida desde "San Diego" y llegada a "Dublin"

  @alternate
  Scenario: Las aerolíneas que operan los vuelos listados
    When busca vuelos desde "Boston" hacia "Berlin"
    Then las aerolíneas listadas deben ser
      | Virgin America  |
      | United Airlines |
      | Aer Lingus      |
      | Lufthansa       |

  @alternate
  Scenario: Cada vuelo listado tiene un número de vuelo distinto
    When busca vuelos desde "Portland" hacia "London"
    Then cada vuelo listado debe tener un número diferente

  @alternate
  Scenario: Los precios se muestran en dólares con dos decimales
    When busca vuelos desde "Paris" hacia "New York"
    Then los precios deben mostrarse en dólares con dos decimales

  @alternate
  Scenario: Los vuelos listados muestran el rango de precios de la ruta
    When busca vuelos desde "Boston" hacia "Rome"
    Then el vuelo más barato de la lista debe costar menos que el más caro

  @alternate
  Scenario: El rango de precios publicado va de 200.98 a 765.32 dólares
    When busca vuelos desde "Philadelphia" hacia "Cairo"
    Then el precio más bajo listado debe ser "200.98" y el más alto "765.32"

  @alternate
  Scenario: Consultar el destino de la semana
    When consulta el destino de la semana
    Then debe ver que el destino de la semana es "Hawaii"

  @alternate
  Scenario: El destino de la semana se acompaña de una imagen
    When consulta el destino de la semana
    Then debe ver la imagen del destino de la semana

  @alternate
  Scenario: El logo "Travel The World" regresa a la página de búsqueda
    When busca vuelos desde "Paris" hacia "Rome"
    And navega con el enlace "Travel The World" de la barra superior
    Then debe ver la página principal de búsqueda

  @alternate
  Scenario: El enlace "home" lleva al inicio de sesión (hallazgo - no regresa al inicio)
    When navega con el enlace "home" de la barra superior
    Then debe llegar a la página de inicio de sesión

  # ------------------------------- Caminos excepcionales -------------------------------
  @negative
  Scenario: Abrir los resultados sin buscar (hallazgo - lista vuelos sin origen ni destino)
    When abre directamente la página de resultados sin buscar
    Then el sistema muestra vuelos aunque no se eligió origen ni destino

  @negative @security
  Scenario: Inyección de HTML en la ciudad de origen (hallazgo - el título no escapa el HTML)
    When busca vuelos inyectando código HTML en la ciudad de origen
    Then el sitio interpreta el código inyectado en el título de resultados

  # ------------------------------- Escenarios límite -------------------------------
  @boundary
  Scenario: La interfaz no ofrece la ciudad de origen como destino
    Then la lista de destinos no debe ofrecer la ciudad "Paris"

  @boundary
  Scenario: Ninguna ciudad aparece a la vez como origen y como destino
    Then ninguna ciudad debe aparecer a la vez como origen y destino

  @boundary @security
  Scenario: Origen y destino iguales forzando el formulario (hallazgo - el servidor no valida la ruta)
    When fuerza una búsqueda desde "Paris" hacia "Paris" modificando el formulario
    Then el sistema permite la búsqueda y muestra vuelos aunque el origen y el destino sean iguales
