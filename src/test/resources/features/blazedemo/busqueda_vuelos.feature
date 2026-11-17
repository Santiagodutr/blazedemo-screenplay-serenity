# language: es
@blazedemo @busqueda
Característica: Búsqueda de vuelos en BlazeDemo
  Como viajero
  Quiero buscar vuelos entre una ciudad de origen y una de destino
  Para conocer los vuelos disponibles y sus precios

  Criterios de aceptación:
  - CA1: El usuario puede buscar vuelos entre un origen y un destino.
  - CA2: El sistema muestra una lista de vuelos, cada uno con un precio mayor a cero.
  - CA3: El título de resultados indica el origen y el destino buscados.
  - CA4: El usuario puede consultar el destino de la semana.

  Antecedentes:
    Dado que Carlos abre la página de BlazeDemo

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

  @alternate
  Escenario: Los vuelos listados muestran el rango de precios de la ruta
    Cuando busca vuelos desde "Boston" hacia "Rome"
    Entonces el vuelo más barato de la lista debe costar menos que el más caro

  @alternate
  Escenario: Consultar el destino de la semana
    Cuando consulta el destino de la semana
    Entonces debe ver que el destino de la semana es "Hawaii"

  @boundary
  Escenario: La interfaz no ofrece la ciudad de origen como destino
    Entonces la lista de destinos no debe ofrecer la ciudad "Paris"

  @boundary @security
  Escenario: Origen y destino iguales forzando el formulario (hallazgo - el servidor no valida la ruta)
    Cuando fuerza una búsqueda desde "Paris" hacia "Paris" modificando el formulario
    Entonces el sistema permite la búsqueda y muestra vuelos aunque el origen y el destino sean iguales
