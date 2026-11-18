# language: en
@blazedemo @cuenta
Feature: Páginas de cuenta de usuario en BlazeDemo
  Como viajero
  Quiero acceder a las opciones de cuenta (ingreso, registro y recuperación)
  Para gestionar mi usuario en la agencia

  Criterios de aceptación:
  - CA11: Los formularios de cuenta marcan como obligatorios sus campos.
  - CA12: Desde el inicio de sesión se puede llegar al registro y a la recuperación de contraseña.
  Nota: por seguridad las pruebas no envían credenciales ni crean cuentas en el sitio real.

  Background:
    Given que Carlos abre la página de inicio de sesión de BlazeDemo

  @happy
  Scenario: El inicio de sesión exige correo y contraseña
    Then el formulario debe marcar como obligatorios los campos
      | E-Mail Address |
      | Password       |

  @alternate
  Scenario: Ir al registro desde la barra superior
    When navega con el enlace "Register" de la barra superior
    Then debe ver el formulario "Register"

  @alternate
  Scenario: El registro exige todos sus campos
    When navega con el enlace "Register" de la barra superior
    Then el formulario debe marcar como obligatorios los campos
      | Name             |
      | Company          |
      | E-Mail Address   |
      | Password         |
      | Confirm Password |

  @alternate
  Scenario: Recuperar la contraseña desde el inicio de sesión
    When elige la opción de recuperar la contraseña
    Then debe ver el formulario "Reset Password"

  @boundary
  Scenario: La recuperación de contraseña exige el correo
    When elige la opción de recuperar la contraseña
    Then el formulario debe marcar como obligatorios los campos
      | E-Mail Address |
