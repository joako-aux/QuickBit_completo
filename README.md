QuickBite - Proyecto Semestral
Asignatura: Desarrollo FullStack 1 (DSY1103) Evaluación: Examen Final Transversal
Integrantes (Equipo N° 7)
Joaquin Perez: Encargado de API Gateway, Auth Service y Notification Service
Maximiliano Provoste: Encargado de User Service y Order Service
Kevin Valdes: Encargado de Menu Service y Payment Service
Descripción del Contexto
Las aplicaciones de delivery son algo de todos los días, pero cuando las desarrollan como sistemas monolíticos, suelen chocar contra una barrera técnica al intentar crecer. En esos sistemas tradicionales, si de repente hay un pico de usuarios pidiendo comida, todo sufre porque los módulos están muy acoplados. Esto significa que si algo secundario se satura, puede bloquear procesos vitales como el inicio de sesión o los pagos, terminando por botar la aplicación completa.

Para nuestro proyecto semestral desarrollamos QuickBite, buscando aplicar una solución a estos típicos problemas de escalabilidad. Construimos una plataforma que abarca lo esencial del negocio: autenticación de usuarios, visualización de menús, gestión de pedidos, simulación de pagos y el envío de notificaciones. El objetivo es conectar a los clientes, los locales y los repartidores de una forma mucho más estable.

En lugar de hacer un monolito, decidimos aplicar una arquitectura de microservicios. Implementamos un API Gateway y el patrón de base de datos por servicio para poder aislar los fallos; por ejemplo, si el servicio de notificaciones se cae, la gente todavía puede seguir pagando sus pedidos sin que la app se congele. Además, esto nos permitió demostrar cómo escalaríamos solo los módulos que más se usan (como el de Menú o Pedidos) si estuviéramos en un escenario de tráfico real, optimizando los recursos del sistema en lugar de agrandar toda la plataforma.
Arquitectura del Ecosistema y Microservicios
El sistema está compuesto por un ecosistema distribuido de 6 microservicios independientes, estructurados bajo el patrón CSR y centralizados mediante un API Gateway.

A continuación se detallan los microservicios implementados, sus puertos y los enlaces a sus respectivos repositorios:

Proyecto Completo: Repositorio principal que centraliza el proyecto. Repositorio GitHub
API Gateway (api-gateway): Punto de entrada único que gestiona el enrutamiento dinámico, el manejo de CORS, el control de flujo y los filtros de las solicitudes (puerto 8080). Repositorio GitHub
Servicio de Autenticación (auth-service): Responsable de la seguridad del sistema, registro de credenciales, inicio de sesión y la generación/validación de tokens JWT (puerto 8081). Repositorio GitHub
Servicio de Usuarios (user-service): Gestión y almacenamiento de perfiles de clientes, repartidores y administradores de restaurantes (puerto 8082). Repositorio GitHub
Servicio de Menú (menu-service): Gestión de la oferta gastronómica, altamente optimizado para lecturas constantes y stock en tiempo real (puerto 8083). Repositorio GitHub
Servicio de Pedidos (order-service): Motor central que administra el ciclo de vida de una orden (puerto 8084). Repositorio GitHub
Servicio de Pagos (payment-service): Encargado de la integración con pasarelas externas, procesamiento de cobros bancarios y gestión de reembolsos (puerto 8085). Repositorio GitHub
Servicio de Notificaciones (notification-service): Gestión y envío automatizado de correos electrónicos, SMS y alertas push (puerto 8086). Repositorio GitHub
Rutas Principales del API Gateway
Todas las solicitudes externas deben dirigirse al Gateway en http://localhost:8080.

Microservicio
Ruta en el Gateway
Método HTTP
Descripción
auth-service
/api/v1/auth/**
POST
Registro e inicio de sesión. Generación de tokens.
user-service
/api/v1/users/**
GET, POST, PUT, DELETE
CRUD completo de usuarios, direcciones y roles.
menu-service
/menu-docs o /api/v1/menu/**
GET, POST, PUT, DELETE
Consulta de platos y gestión de la oferta gastronómica.
order-service
/api/v1/orders/**
GET, POST, PUT
Creación y seguimiento del ciclo de vida de los pedidos.
payment-service
/api/v1/payments/**
POST
Procesamiento de transacciones financieras y reembolsos.
notification-service
/api/v1/notifications/**
POST
Disparador manual o por eventos de alertas internas y externas.

Documentación Swagger / OpenAPI
Cada microservicio expone su propia documentación interactiva para la exploración de endpoints.

Swagger UI unificado (vía Gateway): http://localhost:8080/swagger-ui.html

URLs Individuales (Entorno Local):

Auth Service: http://localhost:8081/swagger-ui.html o http://localhost:8081/api-docs
User Service: http://localhost:8082/swagger-ui.html o http://localhost:8082/api-docs
Menu Service: http://localhost:8083/menu-docs o http://localhost:8083/api-docs
Order Service: http://localhost:8084/swagger-ui.html o http://localhost:8084/api-docs
Payment Service: http://localhost:8085/swagger-ui.html o http://localhost:8085/api-docs
Notification Service: http://localhost:8086/swagger-ui.html o http://localhost:8086/api-docs






 Pruebas Unitarias:
Nombre de la clase: JwtServiceTest
Métodos de Test
cuandoElTokenEsValido_debeExtraerEmailCorrectamente
cuandoElTokenTieneRol_debeExtraerRolCorrectamente
cuandoElTokenEsCorrecto_isTokenValid_debeRetornarTrue
cuandoElTokenEsInvalidoOMalformado_isTokenValid_debeRetornarFalse
cuandoElTokenEsFirmadoConOtroSecreto_isTokenValid_debeRetornarFalse


Nombre de la clase: AuthServiceTest
Métodos de Test
register_CuandoElEmailNoExiste_DebeRegistrarUsuarioExitosamente
register_CuandoElEmailYaExiste_DebeLanzarRuntimeException
Nombre de la clase: MenuServiceImplTest
Métodos de Test
listarMenus_DebeRetornarListaDeMenuDTO
obtenerMenu_CuandoExiste_DebeRetornarMenuDTO
obtenerMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException
guardarMenu_DebeGuardarYRetornarMenuDTO
actualizarMenu_CuandoExiste_DebeActualizarYRetornarMenuDTO
actualizarMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException
eliminarMenu_CuandoExiste_DebeEliminarCorrectamente
eliminarMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException

Nombre de la clase: NotificacionServiceTest
Métodos de Test
cuandoElEnvioEsExitoso_debeGuardarNotificacionComoENVIADO
cuandoElEnvioFalla_debeGuardarNotificacionComoFALLIDO

Nombre de la clase: PagoServiceTest
Métodos de Test
obtenerTodos_DebeRetornarListaDePagoDto
obtenerPorId_CuandoExiste_DebeRetornarPagoDto
obtenerPorId_CuandoNoExiste_DebeLanzarResourceNotFoundException
procesarPago_ConMetodoTarjeta_DebeAprobarYGuardar
procesarPago_ConMetodoEfectivo_DebeAprobarYGuardar
procesarPago_ConMetodoNoValido_DebeRechazarYGuardar

Nombre de la clase: UsuarioServiceTest
Métodos de Test
actualizarUsuario_CuandoExiste_DeberiaModificarYGuardar
 
🚀 Instrucciones de Ejecución
Prerrequisitos
Antes de levantar el proyecto, asegúrate de contar con lo siguiente instalado en tu entorno local:

Java Development Kit (JDK): Versión 17 o superior.
Gestor de dependencias: Maven.
Base de datos: Motor de base de datos SQL (MySQL, PostgreSQL o Laragon) corriendo de forma local.
IDE recomendado: IntelliJ IDEA o Visual Studio Code (con las extensiones de Spring Boot).
Ejecución Local
Clonar los repositorios: Clona el repositorio principal y los repositorios individuales de cada microservicio en una misma carpeta de trabajo.

Configuración de Base de Datos: Asegúrate de crear las bases de datos necesarias para cada microservicio antes de ejecutarlos. Revisa los archivos application.yml o application.properties de cada servicio y ajusta las credenciales (spring.datasource.username y password) según tu entorno local.

Orden de Arranque: Para evitar errores de conexión o dependencias, te sugerimos levantar los servicios en el siguiente orden desde tu IDE o utilizando el comando mvn spring-boot:run en la terminal:

1° - Microservicios Base: auth-service, user-service, menu-service.
2° - Microservicios de Negocio: order-service, payment-service, notification-service.
3° - API Gateway: Levantar el api-gateway al final (Puerto 8080). Este será el encargado de enrutar el tráfico hacia los servicios que ya están activos.

Verificación: Una vez que la consola indique que todos los servicios están operativos, ingresa a http://localhost:8080/swagger-ui.html para confirmar que el Gateway está logrando acceder a las documentaciones de los microservicios.

