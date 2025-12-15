# =========
# Build stage
# =========
FROM maven:3-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy the Maven descriptor and download dependencies first (better caching)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the rest of the project
COPY . .

# Build the Spring Boot application (skip tests for faster CI build)
RUN mvn -B clean package -DskipTests

# =========
# Runtime stage
# =========
FROM eclipse-temurin:17-jdk-alpine

# Set working directory for the runtime container
WORKDIR /app

# Copy the built jar from the build stage
# IMPORTANT: replace 'apistry-0.0.1-SNAPSHOT.jar'
# with the actual JAR file name in target/ after you build locally.
COPY --from=build /app/target/apistry-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
