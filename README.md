# Portal Gamer - Seguridad y Calidad en el Desarrollo de Software

<p align="center">
  <b>Duoc UC</b> · Ingeniería en Desarrollo de Software<br>
  Asignatura: Seguridad y Calidad en el Desarrollo de Software (ISY2202)
</p>

---

## Descripción del Proyecto
**Portal Gamer** es una aplicación web desarrollada como caso semestral para la comunidad de videojuegos y esports. Su propósito principal es reunir, difundir y gestionar información sobre torneos, ferias, competencias y encuentros de gaming de forma interactiva, intuitiva y altamente segura.

El sistema contempla un modelo de control de accesos basado en roles, protección contra vulnerabilidades web comunes, gestión de perfiles de usuario personalizados y un sistema robusto de consultas y filtros de eventos orientados tanto a usuarios públicos como autenticados.

---

## Tecnologías y Frameworks Utilizados

El proyecto está construido bajo los estándares de desarrollo moderno en Java y Spring Framework:

-   **Java 21**: Lenguaje de programación principal (versión LTS).
-   **Spring Boot 3.3.5**: Framework base para la creación de la aplicación web y microservicios.
-   **Spring Security**: Marco de autenticación y control de acceso robusto (protección de URLs y sesiones).
-   **Spring Data JPA / Hibernate**: Capa de persistencia para la interacción con la base de datos relacional.
-   **Thymeleaf**: Motor de plantillas moderno del lado del servidor para el renderizado del frontend.
-   **Bases de Datos**: 
    *   *Desarrollo / Pruebas:* H2 Database (en memoria).
    *   *Producción:* MySQL Driver y conector relacional.
-   **Maven**: Herramienta de gestión de dependencias y automatización de compilación.

---

## Arquitectura y Estructuración por Capas

El proyecto sigue una arquitectura limpia orientada a componentes y separación de responsabilidades:

```text
cl.duoc.app1
│
├── config/          # Clases de configuración (Seguridad, inicialización de datos con DataInitializer)
├── controller/      # Controladores web encargados de gestionar las peticiones HTTP y vistas (WebController)
├── model/           # Entidades de persistencia y modelos de dominio (AppUser, GamerEvent)
├── repository/      # Interfaces de Spring Data JPA para el acceso a datos (AppUserRepository, GamerEventRepository)
└── service/         # Lógica de negocio y contratos de servicio (CustomUserDetailsService, RegistrationService)
```

---

## Características de Seguridad Implementadas

-   **Control de Accesos Basado en Rutas**: Separación estricta entre endpoints públicos (`/`, `/recetas`, `/login`, `/registro`) y privados (`/perfil`, `/eventos/**`).
-   **Gestión Segura de Contraseñas**: Codificación mediante `BCryptPasswordEncoder` para el almacenamiento seguro de credenciales.
-   **Control de Sesiones**: Manejo de sesiones HTTP seguras con invalidación de sesión y expiración de cookies (`JSESSIONID`) al cerrar sesión.
-   **Validaciones Robustas**: Filtrado y validación de entradas de usuario en formularios de registro y actualización de perfiles.

---

## Instrucciones de Ejecución

Sigue estos pasos para levantar el proyecto en tu entorno local:

### Prerrequisitos
-   Tener instalado **Java JDK 21** o superior.
-   Tener instalado **Maven** (o utilizar el wrapper incluido `mvnw`).

### Pasos para iniciar:
1. Clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/cmirandao/SeguridadCalidad_Formativa.git
   ```
2. Entra a la carpeta del proyecto:
   ```bash
   cd Formativa_grupo1/app1
   ```
3. Ejecuta la aplicación utilizando Maven Wrapper:
   * **En Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   * **En Windows (CMD / PowerShell):**
     ```cmd
     mvnw.cmd spring-boot:run
     ```
4. Abre tu navegador web e ingresa a la siguiente URL:
   ```text
   http://localhost:8080/
   ```

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