# Registro de Cambios (Changelog)

Todas las modificaciones notables de este proyecto serán documentadas en este archivo. El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/), y este proyecto adhiere al versionado semántico.

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