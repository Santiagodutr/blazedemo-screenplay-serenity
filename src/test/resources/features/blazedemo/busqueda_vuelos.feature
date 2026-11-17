# language: es
@blazedemo @busqueda
Característica: Búsqueda de vuelos en BlazeDemo
  Como viajero
  Quiero buscar vuelos entre una ciudad de origen y una de destino
  Para conocer los vuelos disponibles y sus precios

  Criterios de aceptación:
  - CA1: El usuario puede buscar vuelos entre un origen y un destino de las listas ofrecidas.
  - CA2: El sistema muestra una lista de vuelos, cada uno con número único, aerolínea y precio en dólares.
  - CA3: El título y los encabezados de resultados indican el origen y el destino buscados.
  - CA4: El usuario puede consultar el destino de la semana y navegar con la barra superior.

  Antecedentes:
    Dado que Carlos abre la página de BlazeDemo

  # ------------------------------- Ruta feliz -------------------------------
  @happy @smoke
  Esquema del escenario: Búsqueda de vuelos para varias rutas
    Cuando busca vuelos desde "<origen>" hacia "<destino>"
    Entonces debe ver una lista de vuelos disponibles
    Y el título de la página de resultados debe contener "<origen>" y "<destino>"
    Y todos los vuelos listados deben tener un precio mayor a cero

    Ejemplos:
      | origen       | destino      |
      | Paris        | Buenos Aires |
      | Boston       | London       |
      | Mexico City  | Cairo        |
      | San Diego    | Berlin       |
      | São Paolo    | New York     |
      | Portland     | Dublin       |
      | Philadelphia | Rome         |

  @happy
  Escenario: Búsqueda con las ciudades que vienen seleccionadas por defecto
    Cuando busca vuelos sin cambiar las ciudades seleccionadas
    Entonces el título de la página de resultados debe contener "Paris" y "Buenos Aires"

  @happy
  Escenario: La lista de origen ofrece las siete ciudades de salida
    Entonces la lista de origen debe ofrecer las ciudades
      | Paris        |
      | Philadelphia |
      | Boston       |
      | Portland     |
      | San Diego    |
      | Mexico City  |
      | São Paolo    |

  @happy
  Escenario: La lista de destino ofrece las siete ciudades de llegada
    Entonces la lista de destino debe ofrecer las ciudades
      | Buenos Aires |
      | Rome         |
      | London       |
      | Berlin       |
      | New York     |
      | Dublin       |
      | Cairo        |

  # ------------------------------- Caminos alternos -------------------------------
  @alternate
  Esquema del escenario: Cada búsqueda muestra exactamente cinco vuelos
    Cuando busca vuelos desde "<origen>" hacia "<destino>"
    Entonces debe ver exactamente 5 vuelos

    Ejemplos:
      | origen      | destino  |
      | Paris       | Rome     |
      | São Paolo   | Cairo    |
      | Mexico City | New York |

  @alternate
  Escenario: Los encabezados de la tabla indican la salida y la llegada
    Cuando busca vuelos desde "San Diego" hacia "Dublin"
    Entonces los encabezados de la tabla deben indicar salida desde "San Diego" y llegada a "Dublin"

  @alternate
  Escenario: Las aerolíneas que operan los vuelos listados
    Cuando busca vuelos desde "Boston" hacia "Berlin"
    Entonces las aerolíneas listadas deben ser
      | Virgin America  |
      | United Airlines |
      | Aer Lingus      |
      | Lufthansa       |

  @alternate
  Escenario: Cada vuelo listado tiene un número de vuelo distinto
    Cuando busca vuelos desde "Portland" hacia "London"
    Entonces cada vuelo listado debe tener un número diferente

  @alternate
  Escenario: Los precios se muestran en dólares con dos decimales
    Cuando busca vuelos desde "Paris" hacia "New York"
    Entonces los precios deben mostrarse en dólares con dos decimales

  @alternate
  Escenario: Los vuelos listados muestran el rango de precios de la ruta
    Cuando busca vuelos desde "Boston" hacia "Rome"
    Entonces el vuelo más barato de la lista debe costar menos que el más caro

  @alternate
  Escenario: El rango de precios publicado va de 200.98 a 765.32 dólares
    Cuando busca vuelos desde "Philadelphia" hacia "Cairo"
    Entonces el precio más bajo listado debe ser "200.98" y el más alto "765.32"

  @alternate
  Escenario: Consultar el destino de la semana
    Cuando consulta el destino de la semana
    Entonces debe ver que el destino de la semana es "Hawaii"

  @alternate
  Escenario: El destino de la semana se acompaña de una imagen
    Cuando consulta el destino de la semana
    Entonces debe ver la imagen del destino de la semana

  @alternate
  Escenario: El logo "Travel The World" regresa a la página de búsqueda
    Cuando busca vuelos desde "Paris" hacia "Rome"
    Y navega con el enlace "Travel The World" de la barra superior
    Entonces debe ver la página principal de búsqueda

  @alternate
  Escenario: El enlace "home" lleva al inicio de sesión (hallazgo - no regresa al inicio)
    Cuando navega con el enlace "home" de la barra superior
    Entonces debe llegar a la página de inicio de sesión

  # ------------------------------- Caminos excepcionales -------------------------------
  @negative
  Escenario: Abrir los resultados sin buscar (hallazgo - lista vuelos sin origen ni destino)
    Cuando abre directamente la página de resultados sin buscar
    Entonces el sistema muestra vuelos aunque no se eligió origen ni destino

  @negative @security
  Escenario: Inyección de HTML en la ciudad de origen (hallazgo - el título no escapa el HTML)
    Cuando busca vuelos inyectando código HTML en la ciudad de origen
    Entonces el sitio interpreta el código inyectado en el título de resultados

  # ------------------------------- Escenarios límite -------------------------------
  @boundary
  Escenario: La interfaz no ofrece la ciudad de origen como destino
    Entonces la lista de destinos no debe ofrecer la ciudad "Paris"

  @boundary
  Escenario: Ninguna ciudad aparece a la vez como origen y como destino
    Entonces ninguna ciudad debe aparecer a la vez como origen y destino

  @boundary @security
  Escenario: Origen y destino iguales forzando el formulario (hallazgo - el servidor no valida la ruta)
    Cuando fuerza una búsqueda desde "Paris" hacia "Paris" modificando el formulario
    Entonces el sistema permite la búsqueda y muestra vuelos aunque el origen y el destino sean iguales
