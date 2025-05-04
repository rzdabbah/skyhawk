# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Copy the Maven cache from host if it exists
#COPY .m2/* /root/.m2

# Copy the pom.xml first to cache dependencies
COPY pom.xml /app/pom.xml
WORKDIR /app

# Download dependencies and cache them
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src /app/src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 