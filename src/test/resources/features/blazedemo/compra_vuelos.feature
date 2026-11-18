# language: es
@blazedemo @compra
Característica: Compra de vuelos en BlazeDemo
  Como viajero
  Quiero elegir un vuelo y pagarlo
  Para recibir la confirmación de mi compra

  Criterios de aceptación:
  - CA5: El usuario puede elegir cualquier vuelo de la lista (por posición, el más barato o el más caro).
  - CA6: El usuario puede completar el formulario de pago y recibir la confirmación.
  - CA7: La confirmación muestra un Id único, el estado "PendingCapture", el monto en USD y la fecha de la compra.
  - CA8: El precio del vuelo elegido debe mantenerse en la compra y en la confirmación.
  - CA9: La página de compra muestra un total igual al precio más los impuestos.
  - CA10: Cada pantalla del flujo se identifica con su propio título de pestaña.

  Antecedentes:
    Dado que Carlos abre la página de BlazeDemo

  # ------------------------------- Ruta feliz -------------------------------
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

  @happy
  Escenario: La confirmación registra la fecha del día de la compra
    Cuando busca vuelos desde "Boston" hacia "Rome"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Entonces la confirmación debe registrar la fecha de hoy

  @happy
  Escenario: La confirmación indica el monto en dólares
    Cuando busca vuelos desde "San Diego" hacia "Cairo"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Entonces el monto de la confirmación debe estar en "USD"

  @happy
  Escenario: Dos compras seguidas generan números de confirmación distintos
    Cuando busca vuelos desde "Paris" hacia "Rome"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Y anota el número de la compra
    Y realiza otra compra completa desde "Boston" hacia "Berlin"
    Entonces el número de la nueva compra debe ser diferente al anotado

  @happy
  Escenario: La página de compra suma el precio y los impuestos en el total
    Cuando busca vuelos desde "Mexico City" hacia "London"
    Y elige el primer vuelo de la lista
    Entonces el total de la compra debe ser la suma del precio y los impuestos

  # ------------------------------- Caminos alternos -------------------------------
  @alternate
  Esquema del escenario: Cualquier vuelo de la lista lleva a la página de compra
    Cuando busca vuelos desde "Portland" hacia "Rome"
    Y elige el vuelo número <posicion> de la lista
    Entonces debe estar en la página de compra del vuelo

    Ejemplos:
      | posicion |
      | 1        |
      | 2        |
      | 3        |
      | 4        |
      | 5        |

  @alternate
  Escenario: Elegir el vuelo más barato de la lista
    Cuando busca vuelos desde "Boston" hacia "Rome"
    Y elige el vuelo de menor precio
    Entonces el vuelo elegido debe ser el de menor precio de la lista
    Y debe estar en la página de compra del vuelo

  @alternate
  Escenario: Elegir el vuelo más caro de la lista
    Cuando busca vuelos desde "Philadelphia" hacia "Dublin"
    Y elige el vuelo de mayor precio
    Entonces el vuelo elegido debe ser el de mayor precio de la lista
    Y debe estar en la página de compra del vuelo

  @alternate
  Escenario: Compra marcando "Remember me"
    Cuando busca vuelos desde "Paris" hacia "Rome"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio marcando recordar mis datos
    Entonces debe ver el mensaje "Thank you for your purchase today!"

  @alternate
  Escenario: Compra con tildes y eñes en los datos del pasajero
    Cuando busca vuelos desde "Mexico City" hacia "Buenos Aires"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio a nombre de "José Ñúñez Pérez"
    Entonces debe ver el mensaje "Thank you for your purchase today!"
    Y la compra debe quedar en estado "PendingCapture"

  @alternate
  Escenario: Compra solo con los datos mínimos del pasajero
    Cuando busca vuelos desde "Boston" hacia "London"
    Y elige el primer vuelo de la lista
    Y completa el pago solo con el nombre "Ana Gomez" y la tarjeta "4111111111111111"
    Entonces debe ver el mensaje "Thank you for your purchase today!"

  @alternate
  Escenario: Cada pantalla del flujo tiene su título de pestaña
    Cuando busca vuelos desde "Paris" hacia "Berlin"
    Entonces la pestaña del navegador debe decir "BlazeDemo - reserve"
    Cuando elige el primer vuelo de la lista
    Entonces la pestaña del navegador debe decir "BlazeDemo Purchase"
    Cuando completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Entonces la pestaña del navegador debe decir "BlazeDemo Confirmation"

  # ------------------------------- Caminos excepcionales -------------------------------
  @negative
  Escenario: Compra con el formulario vacío (hallazgo - no hay validación de campos obligatorios)
    Cuando busca vuelos desde "Paris" hacia "London"
    Y elige el primer vuelo de la lista
    Y intenta comprar sin llenar el formulario
    Entonces el sistema acepta la compra sin validar los datos obligatorios

  @negative
  Escenario: Compra con espacios en blanco en todos los campos (hallazgo - no limpia los datos)
    Cuando busca vuelos desde "San Diego" hacia "Rome"
    Y elige el primer vuelo de la lista
    Y completa el pago con espacios en blanco en todos los campos
    Entonces el sistema acepta la compra sin validar los datos obligatorios

  @negative
  Escenario: Compra con número de tarjeta inválido (hallazgo - no valida la tarjeta)
    Cuando busca vuelos desde "Paris" hacia "London"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con el número de tarjeta "abcd"
    Entonces el sistema acepta la compra sin validar los datos de la tarjeta

  @negative
  Escenario: Compra con un mes de expiración inexistente (hallazgo - acepta el mes 13)
    Cuando busca vuelos desde "Portland" hacia "Berlin"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con mes de expiración "13"
    Entonces el sistema acepta la compra sin validar los datos de la tarjeta

  @negative
  Escenario: Compra con letras en el código postal (hallazgo - no valida el formato)
    Cuando busca vuelos desde "Boston" hacia "Cairo"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con código postal "ABCDE"
    Entonces el sistema acepta la compra sin validar los datos obligatorios

  @negative @security
  Escenario: Abrir la confirmación sin haber comprado (hallazgo - confirma una compra inexistente)
    Cuando abre directamente la página de confirmación sin comprar
    Entonces el sistema muestra una compra confirmada sin haber pagado

  # ------------------------------- Escenarios límite -------------------------------
  @boundary
  Escenario: Tarjeta con año de expiración vencido (hallazgo - no valida la expiración)
    Cuando busca vuelos desde "Boston" hacia "Dublin"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con año de expiración "2019"
    Entonces el sistema acepta la compra sin validar los datos de la tarjeta

  @boundary
  Escenario: Nombre del pasajero de 255 caracteres
    Cuando busca vuelos desde "Paris" hacia "Dublin"
    Y elige el primer vuelo de la lista
    Y completa el pago de un pasajero aleatorio con un nombre de 255 caracteres
    Entonces debe ver el mensaje "Thank you for your purchase today!"

  @boundary
  Escenario: El formulario precarga una fecha de expiración ya vencida (hallazgo)
    Cuando busca vuelos desde "Philadelphia" hacia "London"
    Y elige el primer vuelo de la lista
    Entonces el formulario debe precargar el mes "11" y el año "2017"
    Y la fecha precargada ya está vencida

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
