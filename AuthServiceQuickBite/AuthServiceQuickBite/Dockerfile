# --- ETAPA 1: Compilación ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiamos el pom.xml para preparar la descarga de dependencias
COPY pom.xml .

# Descargamos las dependencias necesarias (incluyendo el parent SNAPSHOT)
RUN mvn dependency:go-offline -B

# 2. Copiamos el código fuente del servicio
COPY src ./src

# 3. Compilamos generando el archivo .jar (omitiendo los tests)
RUN mvn clean package -DskipTests

# --- ETAPA 2: Ejecución ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el .jar compilado en la etapa anterior
COPY --from=build /app/target/AuthServiceQuickBite-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto interno por defecto de Spring Boot
EXPOSE 8080

# Variables de entorno recomendadas para optimizar Java en contenedores
ENV JAVA_OPTS="-XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]