# Construcción del proyecto con Maven
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar los archivos de Maven wrapper y dependencias
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dar permisos de ejecución al wrapper y compilar el proyecto sin ejecutar pruebas
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Imagen final ligera para ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]