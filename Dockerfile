# Step 1: Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set environment variable for Render's dynamic port
ENV PORT=8082
EXPOSE 8082

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]
