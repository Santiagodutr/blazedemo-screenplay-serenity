# language: es
@blazedemo @compra
Característica: Compra de vuelos en BlazeDemo
  Como viajero
  Quiero elegir un vuelo y pagarlo
  Para recibir la confirmación de mi compra

  Criterios de aceptación:
  - CA5: El usuario puede elegir un vuelo de la lista (primero, por posición o el más barato).
  - CA6: El usuario puede completar el formulario de pago y recibir la confirmación.
  - CA7: La confirmación muestra un Id, el estado "PendingCapture" y un código de autorización.
  - CA8: El precio del vuelo elegido debe mantenerse en la compra y en la confirmación.

  Antecedentes:
    Dado que Carlos abre la página de BlazeDemo

  @happy @smoke
  Escenario: Compra exitosa de un vuelo (ruta feliz E2E)
    Cuando busca vuelos desde "Paris" hacia "Buenos Aires"
    Y elige el primer vuelo de la lista
    Y completa el pago con los siguientes datos
      | nombre       | direccion     | ciudad | estado | codigo postal | tipo de tarjeta | numero de tarjeta | mes | anio | nombre en la tarjeta |
      | Carlos Perez | Calle 123 # 4 | Bogota | DC     | 110111        | Visa            | 4111111111111111  | 12  | 2028 | Carlos Perez         |
    Entonces debe ver el mensaje "Thank you for your purchase today!"
    Y la compra debe quedar en estado "PendingCapture"
    Y la confirmación debe tener un id y un código de autorización

  @happy
  Esquema del escenario: Compra con distintos tipos de tarjeta y pasajero aleatorio
    Cuando busca vuelos desde "<origen>" hacia "<destino>"
    Y elige el vuelo número <posicion> de la lista
    Y completa el pago de un pasajero aleatorio con tarjeta "<tarjeta>"
    Entonces debe ver el mensaje "Thank you for your purchase today!"
    Y la compra debe quedar en estado "PendingCapture"

    Ejemplos:
      | origen       | destino  | posicion | tarjeta          |
      | Boston       | London   | 1        | Visa             |
      | Philadelphia | Berlin   | 3        | American Express |
      | Portland     | Dublin   | 5        | Diner's Club     |

  @alternate
  Escenario: Elegir el vuelo más barato de la lista
    Cuando busca vuelos desde "Boston" hacia "Rome"
    Y elige el vuelo de menor precio
    Entonces el vuelo elegido debe ser el de menor precio de la lista
    Y debe estar en la página de compra del vuelo

  @alternate
  Escenario: Compra marcando "Remember me"
    Cuando busca vuelos desde "Paris" hacia "Rome"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio marcando recordar mis datos
    Entonces debe ver el mensaje "Thank you for your purchase today!"

  @negative
  Escenario: Compra con el formulario vacío (hallazgo - no hay validación de campos obligatorios)
    Cuando busca vuelos desde "Paris" hacia "London"
    Y elige el primer vuelo de la lista
    Y intenta comprar sin llenar el formulario
    Entonces el sistema acepta la compra sin validar los datos obligatorios

  @negative
  Escenario: Compra con número de tarjeta inválido (hallazgo - no valida la tarjeta)
    Cuando busca vuelos desde "Paris" hacia "London"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con el número de tarjeta "abcd"
    Entonces el sistema acepta la compra sin validar los datos de la tarjeta

  @boundary
  Escenario: Tarjeta con año de expiración vencido (hallazgo - no valida la expiración)
    Cuando busca vuelos desde "Boston" hacia "Dublin"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con año de expiración "2019"
    Entonces el sistema acepta la compra sin validar los datos de la tarjeta

  # ---------------------------------------------------------------------------------
  # Defectos conocidos: estos escenarios afirman lo que el sitio DEBERÍA hacer.
  # Fallan por defectos de BlazeDemo, no de la automatización. Se excluyen de la
  # corrida principal y se ejecutan aparte con: gradlew test -Prunner=KnownIssuesRunner
  # ---------------------------------------------------------------------------------
  @known-issue
  Escenario: El precio de la compra coincide con el precio del vuelo elegido
    Cuando busca vuelos desde "Philadelphia" hacia "Berlin"
    Y elige el vuelo número 3 de la lista
    Entonces el precio mostrado en la compra debe coincidir con el precio elegido

  @known-issue
  Escenario: El monto de la confirmación coincide con el total a pagar
    Cuando busca vuelos desde "Paris" hacia "Buenos Aires"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Entonces el monto de la confirmación debe ser igual al total de la compra

  @known-issue
  Escenario: La confirmación muestra los últimos 4 dígitos de la tarjeta ingresada
    Cuando busca vuelos desde "Paris" hacia "Cairo"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con el número de tarjeta "5500000000000004"
    Entonces la tarjeta de la confirmación debe terminar en "0004"
