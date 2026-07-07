# --- ETAPA 1: Compilación interna en Docker ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el archivo de configuración de dependencias y el código fuente
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto generando el archivo binario .jar (omitiendo los tests)
RUN mvn clean package -DskipTests

# --- ETAPA 2: Imagen final ligera para ejecución ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el .jar generado dinámicamente en la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto (informativo, Spring Boot usará el de su properties)
EXPOSE 8080

# Variables de entorno para optimizar el uso de memoria de Java en Docker
ENV JAVA_OPTS="-XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

# Comando definitivo para arrancar el microservicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]