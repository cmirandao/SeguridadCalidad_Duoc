# Etapa 1: compilación del proyecto con Maven
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar configuración de Maven Wrapper y dependencias del proyecto
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Descargar dependencias (aprovecha la caché de capas)
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# Copiar código fuente y construir el JAR (sin tests, ya que no hay BD en el build)
COPY src src
RUN ./mvnw -B clean package -DskipTests

# Etapa 2: imagen de ejecución
FROM eclipse-temurin:21-jre-alpine

# Creación de usuario y grupo no privilegiados
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]