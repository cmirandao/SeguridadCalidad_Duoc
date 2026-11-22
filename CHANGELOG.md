# Registro de Cambios (Changelog)

Todas las modificaciones notables de este proyecto serán documentadas en este archivo. El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/), y este proyecto adhiere al versionado semántico.

---

## [1.3.1] - 2026-11-22

### Seguridad
- **CSRF (`SecurityConfig.java`):** Se eliminó la deshabilitación global de CSRF (`java:S4502`), manteniendo la protección activa para vistas Thymeleaf y restringiendo su exclusión únicamente a los endpoints REST *stateless* (`/api/**`) autenticados con JWT.
- **Repositorio CSRF (`SecurityConfig.java`):** Se configuró `new CookieCsrfTokenRepository()` (manteniendo `HttpOnly` activo, `java:S3330`) junto con `CsrfTokenRequestAttributeHandler` para soportar la validación CSRF en un esquema `SessionCreationPolicy.STATELESS`.
- **Cookie JWT (`AuthController.java`):** Se habilitó el atributo `Secure` (`setSecure(true)`) en la cookie del token JWT (`java:S2092`), conservando `HttpOnly`.

### Corregido
- **Prevención de ReDoS (`RegistrationServiceImpl.java`):** Se reemplazaron las tres expresiones regulares de validación de contraseña con riesgo de *backtracking* (`java:S5852`) por verificaciones de caracteres de complejidad lineal (`chars().noneMatch(...)` para mayúsculas, minúsculas y dígitos).

### Calidad y Documentación
- Se ejecutó auditoría estática (SAST) local con SonarQube Community Build (ciclo antes/después), reduciendo las incidencias abiertas de 5 a 0 (con 1 hallazgo en estado *Accepted* justificado por arquitectura en `/api/**`).

---

## [1.3.0] - Semana 3: Seguridad Avanzada, JWT y Endurecimiento (Hardening)

### Agregado
- **Autenticación Basada en Tokens JWT**: Implementación de filtros de seguridad (`JwtRequestFilter`) y almacenamiento seguro en cookies `HttpOnly` para la gestión de sesiones de usuario.
- **Endurecimiento de Cabeceras HTTP**: Configuración de cabeceras de Content Security Policy (CSP) en `SecurityConfig` para mitigar vectores de inyección y control de recursos.

### Seguridad y Remediación (OWASP ZAP)
- **Mitigación de Cookies**: Incorporación de los atributos de seguridad `SameSite=Lax` y `HttpOnly`, junto con la desactivación de la reescritura de URLs con `jsessionid`.
- **Independencia de Recursos**: Centralización y empaquetado local de librerías frontend (como SweetAlert2) para asegurar la integridad de los recursos estáticos.
- **Validación Arquitectónica**: Documentación y justificación técnica del uso de tokens JWT stateless frente a la gestión tradicional de formularios CSRF.

---

## [1.2.0] - Semana 2: Persistencia, Base de Datos y Autenticación Avanzada

### Agregado
- **Integración con Base de Datos**: Configuración de Spring Data JPA y soporte para H2 / MySQL para la persistencia de usuarios y eventos gamer.
- **DataInitializer**: Automatización para la creación de usuarios de prueba preconfigurados con encriptación BCrypt y roles (`ROLE_USER`) al iniciar la aplicación.
- **Filtros Avanzados de Búsqueda**: Implementación de filtros dinámicos en el repositorio y controladores para buscar eventos por categoría, ubicación y fecha.

### Seguridad
- Refuerzo en la configuración de `SecurityConfig` para proteger las rutas privadas (`/perfil`, `/eventos/**`) y mantener públicas las vistas de inicio y registro.

---

## [1.1.0] - Semana 1 (Actualización y Limpieza de Repositorio)

### Corregido
- Solución de conflictos de compilación al actualizar la versión base a **Spring Boot 3.3.5** y compatibilidad nativa con **Java 21**.
- Corrección de consultas derivadas (Query Methods) en `GamerEventRepository` para evitar conflictos entre tipos de fecha y booleanos.
- Exclusión de archivos pesados y binarios (`*.tar`) mediante el archivo `.gitignore` para cumplir con los límites de tamaño en GitHub.

---

## [1.0.0] - Semana 1: Estructura Base y Caso Semestral Gamer

### Agregado
- **Estructura del Proyecto**: Creación del proyecto base mediante Spring Initializr utilizando Maven, Spring Web, Spring Security y Thymeleaf.
- **Vistas del Caso Gamer**:
  - Página de inicio (`index.html`) con eventos destacados, próximos eventos y sección de comunidad.
  - Módulo de eventos y detalle de actividades (`eventos.html`, `detalle-evento.html`).
  - Gestión de perfil de usuario (`perfil.html`) con intereses, avatar y notificaciones.
  - Formularios de autenticación y registro (`login.html`, `registro.html`).
- **Controlador Principal**: Implementación de `WebController` para el enrutamiento de páginas y renderizado de modelos.