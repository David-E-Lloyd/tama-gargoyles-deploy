# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Skip tests to speed up deployment and avoid build failures due to missing env vars
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Note the addition of -Dspring.profiles.active=prod
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]