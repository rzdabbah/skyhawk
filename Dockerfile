FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace/app

# Copy the entire project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 