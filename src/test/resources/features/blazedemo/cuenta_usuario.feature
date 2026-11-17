# language: es
@blazedemo @cuenta
Característica: Páginas de cuenta de usuario en BlazeDemo
  Como viajero
  Quiero acceder a las opciones de cuenta (ingreso, registro y recuperación)
  Para gestionar mi usuario en la agencia

  Criterios de aceptación:
  - CA11: Los formularios de cuenta marcan como obligatorios sus campos.
  - CA12: Desde el inicio de sesión se puede llegar al registro y a la recuperación de contraseña.
  Nota: por seguridad las pruebas no envían credenciales ni crean cuentas en el sitio real.

  Antecedentes:
    Dado que Carlos abre la página de inicio de sesión de BlazeDemo

  @happy
  Escenario: El inicio de sesión exige correo y contraseña
    Entonces el formulario debe marcar como obligatorios los campos
      | E-Mail Address |
      | Password       |

  @alternate
  Escenario: Ir al registro desde la barra superior
    Cuando navega con el enlace "Register" de la barra superior
    Entonces debe ver el formulario "Register"

  @alternate
  Escenario: El registro exige todos sus campos
    Cuando navega con el enlace "Register" de la barra superior
    Entonces el formulario debe marcar como obligatorios los campos
      | Name             |
      | Company          |
      | E-Mail Address   |
      | Password         |
      | Confirm Password |

  @alternate
  Escenario: Recuperar la contraseña desde el inicio de sesión
    Cuando elige la opción de recuperar la contraseña
    Entonces debe ver el formulario "Reset Password"

  @boundary
  Escenario: La recuperación de contraseña exige el correo
    Cuando elige la opción de recuperar la contraseña
    Entonces el formulario debe marcar como obligatorios los campos
      | E-Mail Address |
