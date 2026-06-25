# Menu Service - QuickBite 🍔🍕

Este módulo corresponde al microservicio de gestión de catálogos, menús y categorías para la plataforma de pedidos de comida **QuickBite**. Diseñado bajo una arquitectura desacoplada y escalable, el servicio cumple estrictamente con los lineamientos académicos del curso, incluyendo validaciones exhaustivas de entrada y un control unificado de excepciones.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Base de Datos Local:** MySQL (administrado mediante Laragon)
* **Gestión de Dependencias:** Maven
* **Control de Versiones:** Git / GitHub

---

## 🛡️ Control Global de Excepciones (`@RestControllerAdvice`)
Siguiendo las pautas oficiales del material del curso ("*2.3.1 Spring Boot - ResponseEntity y Validaciones.pdf*"), se ha implementado un sistema robusto que centraliza la captura de fallos, evitando el uso de excepciones genéricas que oculten errores del sistema:

1. **Recursos No Encontrados (`404 Not Found`):**
    * Se creó la clase especializada `ResourceNotFoundException` (heredera de `RuntimeException`).
    * Captura fallos cuando un producto o una categoría consultada mediante su UUID no existen en la base de datos, retornando un JSON limpio:
      ```json
      {
        "error": "No existe el producto con el ID: [UUID]"
      }
      ```
2. **Validaciones de Campos (`400 Bad Request`):**
    * Intercepta las excepciones `MethodArgumentNotValidException` gatilladas por la anotación `@Valid` en los controladores.
    * Devuelve un mapeo exacto con los campos que violaron las restricciones (por ejemplo: precio menor a cero o campos vacíos) y sus respectivos mensajes configurados.

---

## 📑 Endpoints Principales

### 📦 Productos
* `GET /api/productos` - Listar todos los productos del menú.
* `GET /api/productos/{id}` - Obtener el detalle de un plato mediante su UUID (`404` si no existe).
* `POST /api/productos` - Crear o actualizar un producto. Valida rigurosamente que la categoría asociada (`categoriaId`) exista previamente en el sistema.

### 🗂️ Categorías
* `GET /api/categorias/{id}` - Obtener el detalle de una categoría específica.
* `DELETE /api/categorias/{id}` - Eliminar una categoría. Comprueba la preexistencia del registro antes de ejecutar la acción.

---

## 🚀 Despliegue Local
1. Asegurar que los servicios de Apache y MySQL estén en ejecución desde el panel de **Laragon**.
2. Clonar o actualizar este repositorio localmente en la máquina de desarrollo.
3. Compilar y ejecutar la aplicación desde IntelliJ IDEA.
4. El servicio quedará escuchando en el puerto local asignado (`http://localhost:8082`), listo para procesar peticiones y ser vinculado al **API Gateway** del proyecto grupal.