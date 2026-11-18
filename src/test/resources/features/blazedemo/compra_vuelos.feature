# language: en
@blazedemo @compra
Feature: Compra de vuelos en BlazeDemo
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

  Background:
    Given que Carlos abre la página de BlazeDemo

  # ------------------------------- Ruta feliz -------------------------------
  @happy @smoke
  Scenario: Compra exitosa de un vuelo (ruta feliz E2E)
    When busca vuelos desde "Paris" hacia "Buenos Aires"
    And elige el primer vuelo de la lista
    And completa el pago con los siguientes datos
      | nombre       | direccion     | ciudad | estado | codigo postal | tipo de tarjeta | numero de tarjeta | mes | anio | nombre en la tarjeta |
      | Carlos Perez | Calle 123 # 4 | Bogota | DC     | 110111        | Visa            | 4111111111111111  | 12  | 2028 | Carlos Perez         |
    Then debe ver el mensaje "Thank you for your purchase today!"
    And la compra debe quedar en estado "PendingCapture"
    And la confirmación debe tener un id y un código de autorización

  @happy @tarjetas-prueba
  Scenario Outline: Compra con distintos tipos de tarjeta y pasajero aleatorio
    When busca vuelos desde "<origen>" hacia "<destino>"
    And elige el vuelo número <posicion> de la lista
    And completa el pago de un pasajero aleatorio con tarjeta "<tarjeta>" y número de prueba "<numero>"
    Then debe ver el mensaje "Thank you for your purchase today!"
    And la compra debe quedar en estado "PendingCapture"

    Examples:
      | origen       | destino  | posicion | tarjeta          | numero           |
      | Boston       | London   | 1        | Visa             | 4242424242424242 |
      | Philadelphia | Berlin   | 3        | American Express | 378282246310005  |
      | Philadelphia | Berlin   | 3        | American Express | 371449635398431  |
      | Portland     | Dublin   | 5        | Diner's Club      | 3056930009020004 |

  @happy
  Scenario: La confirmación registra la fecha del día de la compra
    When busca vuelos desde "Boston" hacia "Rome"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Then la confirmación debe registrar la fecha de hoy

  @happy
  Scenario: La confirmación indica el monto en dólares
    When busca vuelos desde "San Diego" hacia "Cairo"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Then el monto de la confirmación debe estar en "USD"

  @happy
  Scenario: Dos compras seguidas generan números de confirmación distintos
    When busca vuelos desde "Paris" hacia "Rome"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con tarjeta "Visa"
    And anota el número de la compra
    And realiza otra compra completa desde "Boston" hacia "Berlin"
    Then el número de la nueva compra debe ser diferente al anotado

  @happy
  Scenario: La página de compra suma el precio y los impuestos en el total
    When busca vuelos desde "Mexico City" hacia "London"
    And elige el primer vuelo de la lista
    Then el total de la compra debe ser la suma del precio y los impuestos

  # ------------------------------- Caminos alternos -------------------------------
  @alternate
  Scenario Outline: Cualquier vuelo de la lista lleva a la página de compra
    When busca vuelos desde "Portland" hacia "Rome"
    And elige el vuelo número <posicion> de la lista
    Then debe estar en la página de compra del vuelo

    Examples:
      | posicion |
      | 1        |
      | 2        |
      | 3        |
      | 4        |
      | 5        |

  @alternate
  Scenario: Elegir el vuelo más barato de la lista
    When busca vuelos desde "Boston" hacia "Rome"
    And elige el vuelo de menor precio
    Then el vuelo elegido debe ser el de menor precio de la lista
    And debe estar en la página de compra del vuelo

  @alternate
  Scenario: Elegir el vuelo más caro de la lista
    When busca vuelos desde "Philadelphia" hacia "Dublin"
    And elige el vuelo de mayor precio
    Then el vuelo elegido debe ser el de mayor precio de la lista
    And debe estar en la página de compra del vuelo

  @alternate
  Scenario: Compra marcando "Remember me"
    When busca vuelos desde "Paris" hacia "Rome"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio marcando recordar mis datos
    Then debe ver el mensaje "Thank you for your purchase today!"

  @alternate
  Scenario: Compra con tildes y eñes en los datos del pasajero
    When busca vuelos desde "Mexico City" hacia "Buenos Aires"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio a nombre de "José Ñúñez Pérez"
    Then debe ver el mensaje "Thank you for your purchase today!"
    And la compra debe quedar en estado "PendingCapture"

  @alternate
  Scenario: Compra solo con los datos mínimos del pasajero
    When busca vuelos desde "Boston" hacia "London"
    And elige el primer vuelo de la lista
    And completa el pago solo con el nombre "Ana Gomez" y la tarjeta "4111111111111111"
    Then debe ver el mensaje "Thank you for your purchase today!"

  @alternate
  Scenario: Cada pantalla del flujo tiene su título de pestaña
    When busca vuelos desde "Paris" hacia "Berlin"
    Then la pestaña del navegador debe decir "BlazeDemo - reserve"
    When elige el primer vuelo de la lista
    Then la pestaña del navegador debe decir "BlazeDemo Purchase"
    When completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Then la pestaña del navegador debe decir "BlazeDemo Confirmation"

  # ------------------------------- Caminos excepcionales -------------------------------
  @negative
  Scenario: Compra con el formulario vacío (hallazgo - no hay validación de campos obligatorios)
    When busca vuelos desde "Paris" hacia "London"
    And elige el primer vuelo de la lista
    And intenta comprar sin llenar el formulario
    Then el sistema acepta la compra sin validar los datos obligatorios

  @negative
  Scenario: Compra con espacios en blanco en todos los campos (hallazgo - no limpia los datos)
    When busca vuelos desde "San Diego" hacia "Rome"
    And elige el primer vuelo de la lista
    And completa el pago con espacios en blanco en todos los campos
    Then el sistema acepta la compra sin validar los datos obligatorios

  @negative
  Scenario: Compra con número de tarjeta inválido (hallazgo - no valida la tarjeta)
    When busca vuelos desde "Paris" hacia "London"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con el número de tarjeta "abcd"
    Then el sistema acepta la compra sin validar los datos de la tarjeta

  @negative
  Scenario: Compra con un mes de expiración inexistente (hallazgo - acepta el mes 13)
    When busca vuelos desde "Portland" hacia "Berlin"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con mes de expiración "13"
    Then el sistema acepta la compra sin validar los datos de la tarjeta

  @negative
  Scenario: Compra con letras en el código postal (hallazgo - no valida el formato)
    When busca vuelos desde "Boston" hacia "Cairo"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con código postal "ABCDE"
    Then el sistema acepta la compra sin validar los datos obligatorios

  @negative @security
  Scenario: Abrir la confirmación sin haber comprado (hallazgo - confirma una compra inexistente)
    When abre directamente la página de confirmación sin comprar
    Then el sistema muestra una compra confirmada sin haber pagado

  # ------------------------------- Escenarios límite -------------------------------
  @boundary
  Scenario: Tarjeta con año de expiración vencido (hallazgo - no valida la expiración)
    When busca vuelos desde "Boston" hacia "Dublin"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con año de expiración "2019"
    Then el sistema acepta la compra sin validar los datos de la tarjeta

  @boundary
  Scenario: Nombre del pasajero de 255 caracteres
    When busca vuelos desde "Paris" hacia "Dublin"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con un nombre de 255 caracteres
    Then debe ver el mensaje "Thank you for your purchase today!"

  @boundary
  Scenario: El formulario precarga una fecha de expiración ya vencida (hallazgo)
    When busca vuelos desde "Philadelphia" hacia "London"
    And elige el primer vuelo de la lista
    Then el formulario debe precargar el mes "11" y el año "2017"
    And la fecha precargada ya está vencida

  # ---------------------------------------------------------------------------------
  # Defectos conocidos: estos escenarios afirman lo que el sitio DEBERÍA hacer.
  # Fallan por defectos de BlazeDemo, no de la automatización. Se excluyen de la
  # corrida principal y se ejecutan aparte con: gradlew test -Prunner=KnownIssuesRunner
  # ---------------------------------------------------------------------------------
  @known-issue
  Scenario: El precio de la compra coincide con el precio del vuelo elegido
    When busca vuelos desde "Philadelphia" hacia "Berlin"
    And elige el vuelo número 3 de la lista
    Then el precio mostrado en la compra debe coincidir con el precio elegido

  @known-issue
  Scenario: El monto de la confirmación coincide con el total a pagar
    When busca vuelos desde "Paris" hacia "Buenos Aires"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con tarjeta "Visa"
    Then el monto de la confirmación debe ser igual al total de la compra

  @known-issue
  Scenario: La confirmación muestra los últimos 4 dígitos de la tarjeta ingresada
    When busca vuelos desde "Paris" hacia "Cairo"
    And elige el primer vuelo de la lista
    And completa el pago de un pasajero aleatorio con el número de tarjeta "5500000000000004"
    Then la tarjeta de la confirmación debe terminar en "0004"
