# Stage 1: Build the full-stack project using Gradle & Node
FROM eclipse-temurin:25-jdk AS build
WORKDIR /taplink

# Install Node.js for building Angular
RUN apt-get update && apt-get install -y nodejs npm

# Copy project files
COPY . .

# Grant execution permissions to gradle wrapper
RUN chmod +x gradlew

# Clean, build Angular, and package the jar (skipping unit tests in container to speed up build)
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:25-jre
WORKDIR /taplink

# Copy the built jar from the build stage (using wildcard to match version 0.0.1)
COPY --from=build /taplink/build/libs/*.jar app.jar

EXPOSE 1005
ENTRYPOINT ["java", "-jar", "app.jar"]