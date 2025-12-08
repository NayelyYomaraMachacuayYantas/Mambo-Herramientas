# -----------------------------
# Etapa 1: Build (compilación)
# -----------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos solo el pom.xml para cachear dependencias
COPY pom.xml .

# Descargamos las dependencias
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto
RUN mvn clean package -DskipTests

# -----------------------------
# Etapa 2: Runtime (ejecución)
# -----------------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copiamos el JAR desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
