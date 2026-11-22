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

Sigue estos pasos para levantar el proyecto en tu entorno local.

### Prerrequisitos
- **Java JDK 21** o superior.
- **Maven 3.9+** instalado (`mvn -v`) **o** Maven Wrapper configurado (`./mvnw`) en `app1/`.
- **Docker y Docker Compose** (para levantar MySQL y/o desplegar la app en contenedores).

> **Nota (macOS/Linux):** si al ejecutar `./mvnw` obtienes `Permission denied`, otorga permiso de ejecución con `chmod +x mvnw` desde la carpeta `app1/`. Si el wrapper indica que falta `./.mvn/wrapper/maven-wrapper.properties`, puedes usar `mvn` directamente o regenerarlo con `mvn -N wrapper:wrapper`.

### Configuración de base de datos
- La aplicación utiliza **MySQL 8** y, por defecto, se conecta a `jdbc:mysql://localhost:3306/portal_gamer` con usuario `root` (ver `app1/src/main/resources/application.properties`).
- En `app1/compose.yaml` el servicio `mysql` expone el puerto `3306` al host y crea la base `portal_gamer`. La contraseña de `root` se define con la variable `MYSQL_ROOT_PASSWORD`.
- Puedes definirla en un archivo `app1/.env`:
  ```env
  MYSQL_ROOT_PASSWORD=root
  ```
  o exportarla en la terminal antes de ejecutar Docker Compose.

### 1. Levantar MySQL (requerido para ejecución local y tests)
Desde la carpeta `app1/`:

```bash
# Usa el valor definido en .env o exporta MYSQL_ROOT_PASSWORD antes
docker compose up -d mysql
```

Espera a que el contenedor esté saludable (`healthy`) antes de iniciar la aplicación o ejecutar los tests:

```bash
docker compose ps
```

### 2. Ejecución local de la aplicación (Spring Boot)
Con MySQL accesible en `localhost:3306`:

```bash
cd app1
mvn spring-boot:run
# o, si usas el wrapper configurado:
# ./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8081/`.

Si necesitas usar credenciales o URL distintas a las predeterminadas, puedes sobreescribirlas con variables de entorno, por ejemplo:
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.

### 3. Compilación y pruebas
Los tests de integración/contexto (`@SpringBootTest` y tests de repositorio con base real) requieren **MySQL en ejecución y accesible** en `localhost:3306` con la configuración esperada.

```bash
cd app1
# Compilar y ejecutar tests
mvn clean test
# o compilar, ejecutar tests y generar el JAR
mvn clean install
```

Si solo deseas compilar y generar el artefacto sin ejecutar tests (por ejemplo, sin base de datos disponible):

```bash
mvn clean package -DskipTests
```

---

## Despliegue en Contenedores (Docker)

El `app1/Dockerfile` utiliza un **build multi-etapa**: en la primera etapa compila el proyecto con Maven Wrapper (`./mvnw`) y genera el JAR, y en la segunda crea la imagen de ejecución con JRE. No es necesario compilar ni generar `target/*.jar` manualmente antes de construir la imagen.

Desde la carpeta `app1`, con `MYSQL_ROOT_PASSWORD` definida (en `app1/.env` o exportada en la terminal):

```bash
docker compose up --build -d
```

Esto construirá la imagen de la aplicación y levantará los servicios (`mysql` y `app`). La primera compilación puede tardar más mientras se descargan dependencias de Maven.

Ejemplos para definir `MYSQL_ROOT_PASSWORD` al vuelo:

* **Windows (PowerShell):**
  ```powershell
  $env:MYSQL_ROOT_PASSWORD = "elige-una-contrasena"
  docker compose up --build -d
  ```
* **Linux / macOS:**
  ```bash
  MYSQL_ROOT_PASSWORD='elige-una-contrasena' docker compose up --build -d
  ```

La aplicación estará disponible en `http://localhost:8081/`. Los datos de MySQL persisten en el volumen Docker `mysql-data`.

Para ver el estado de los servicios:

```bash
docker compose ps
```

Para detener los servicios:

```bash
docker compose down
```

> **Nota:** el build de la imagen ejecuta `mvnw clean package -DskipTests` (sin tests) porque no hay una base de datos disponible durante la construcción. Para validar el proyecto con tests, ejecútalos localmente con MySQL en marcha como se indica en la sección **Compilación y pruebas**.

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