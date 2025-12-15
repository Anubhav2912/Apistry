# =========
# Build stage
# =========
FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /app

# Download dependencies first (layer caching)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the rest of the source and build
COPY . .
RUN mvn -B clean package -DskipTests

# =========
# Runtime stage
# =========
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the Spring Boot fat JAR built in the first stage
# Maven builds: target/Apistry-0.0.1-SNAPSHOT.jar (from your screenshot)
COPY --from=build /app/target/Apistry-0.0.1-SNAPSHOT.jar app.jar
# (If you ever change the version, you can instead use: COPY --from=build /app/target/*.jar app.jar)

# Render will route traffic to this port; Spring Boot default is 8080
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
