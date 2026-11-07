# Portal Gamer - Seguridad y Calidad en el Desarrollo de Software

<p align="center">
  <b>Duoc UC</b> · Ingeniería en Desarrollo de Software<br>
  Asignatura: Seguridad y Calidad en el Desarrollo de Software (ISY2202)
</p>

---

## Descripción del Proyecto
**Portal Gamer** es una aplicación web desarrollada como caso semestral para la comunidad de videojuegos y esports. Su propósito principal es reunir, difundir y gestionar información sobre torneos, ferias, competencias y encuentros de gaming de forma interactiva, intuitiva y altamente segura.

El sistema contempla un modelo de control de accesos basado en roles, protección contra vulnerabilidades web comunes, gestión de perfiles de usuario personalizados, un sistema robusto de consultas y filtros de eventos, y una capa de autenticación basada en **JSON Web Tokens (JWT)** integrada tanto para navegación web segura (vía cookies `HttpOnly`) como para consumo de servicios backend.

---

## Tecnologías y Frameworks Utilizados

El proyecto está construido bajo los estándares de desarrollo moderno en Java y Spring Framework:

* **Java 21**: Lenguaje de programación principal (versión LTS).
* **Spring Boot 3.3.5**: Framework base para la creación de la aplicación web y microservicios.
* **Spring Security**: Marco de autenticación y control de acceso robusto (protección de URLs, proveedor personalizado y filtros).
* **Spring Data JPA / Hibernate**: Capa de persistencia para la interacción con la base de datos relacional.
* **Thymeleaf**: Motor de plantillas moderno del lado del servidor para el renderizado del frontend.
* **Bases de Datos**: 
  * *Producción / Contenedor:* MySQL 8.0 (con conector JDBC y red Docker aislada).
* **Maven**: Herramienta de gestión de dependencias y automatización de compilación.

---

## Arquitectura y Estructuración por Capas

El proyecto sigue una arquitectura limpia orientada a componentes y separación de responsabilidades:

```text
cl.duoc.app1
│
├── config/         # Clases de configuración y componentes de inicialización de datos (DataInitializer)
├── controller/     # Controladores web y REST (WebController, AuthController)
├── model/          # Entidades de persistencia y modelos de dominio (AppUser, GamerEvent)
├── repository/     # Interfaces de Spring Data JPA para el acceso a datos (AppUserRepository, GamerEventRepository)
├── security/       # Componentes de seguridad JWT, filtros y proveedores (JwtTokenUtil, CustomAuthenticationProvider, TokenStore, SecurityConfig)
└── service/        # Lógica de negocio y contratos de servicio (CustomUserDetailsService, RegistrationServiceImpl)
```

---

## Características de Seguridad Implementadas

-   **Control de Accesos Basado en Rutas**: Separación estricta entre endpoints públicos (`/`, `/inicio`, `/eventos`, `/login`, `/registro`) y privados (`/perfil`, `/eventos/**`, `/greetings`).
-   **Autenticación Basada en JWT y Cookies Seguras**: Emisión de tokens firmados mediante HMAC-SHA256 y almacenamiento seguro en cookies `HttpOnly` para mitigar ataques de tipo XSS.
-   **Gestión Segura de Contraseñas**: Codificación mediante `BCryptPasswordEncoder` para el almacenamiento seguro de credenciales en MySQL.
-   **Control de Sesiones**: Manejo de sesiones HTTP seguras con invalidación y control estricto de autorización mediante `SecurityFilterChain`.

---

## Instrucciones de Ejecución

Sigue estos pasos para levantar el proyecto en tu entorno local:

### Prerrequisitos
-   Tener instalado **Java JDK 21** o superior.
-   Tener instalado **Docker y Docker Compose**.

---

## Despliegue en Contenedores (Docker)

Desde la carpeta `app1`, define una contraseña para MySQL y levanta ambos servicios con Docker Compose. El contenedor se encargará de compilar y desplegar la aplicación de forma integrada:

* **Windows (PowerShell):**
  ```powershell
  $env:MYSQL_ROOT_PASSWORD = "elige-una-contrasena"
  docker compose up --build -d
  ```
* **Linux / macOS:**
  ```bash
  MYSQL_ROOT_PASSWORD='elige-una-contrasena' docker compose up --build -d
  ```

La aplicación estará disponible en `http://localhost:8081/`. Los datos de MySQL persisten en un volumen Docker dedicado.

---

## Usuarios de Prueba Preconfigurados

El sistema inicializa automáticamente los siguientes usuarios de prueba al arrancar para facilitar las pruebas de autenticación y roles:

1. **Usuario Demo**: 
   * Correo: `demo@gamer.cl`
   * Contraseña: `Gamer123!`
2. **Ana Gamer**: 
   * Correo: `ana@gamer.cl`
   * Contraseña: `Gamer123!`
3. **Marco Gamer**: 
   * Correo: `marco@gamer.cl`
   * Contraseña: `Gamer123!`