# Step 1: Use Maven to build the JAR
FROM maven:3.9.4-openjdk-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the JAR with a lightweight JDK
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy only the built JAR from builder stage
COPY --from=build /app/target/personal-finance-app-0.0.1-SNAPSHOT.jar

# Expose the port (default Spring Boot port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/personal-finance-app-0.0.1-SNAPSHOT.jar"]
