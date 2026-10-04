# Stage 1: Build stage using Maven and Eclipse Temurin JDK 21
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy project files and pre-built target if available
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn
COPY src ./src
COPY target/weddingPlanner-0.0.1-SNAPSHOT.jar* ./target/

# Package jar if pre-built artifact is missing
RUN if [ ! -f target/weddingPlanner-0.0.1-SNAPSHOT.jar ]; then ./mvnw package -DskipTests; fi

# Stage 2: Run stage using lightweight Eclipse Temurin JRE 21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy built artifact from builder stage
COPY --from=builder /app/target/weddingPlanner-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
