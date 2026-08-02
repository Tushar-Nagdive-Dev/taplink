# Stage 1: Build the full-stack project using Gradle & Node
FROM eclipse-temurin:25-jdk AS build
WORKDIR /taplink

# Install Node.js and npm (using Node 20 as required by Angular)
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

# Copy project files
COPY . .

# Grant execution permissions to gradle wrapper
RUN chmod +x gradlew

# Install Angular / Frontend dependencies first
WORKDIR /taplink/views/taplink
RUN npm install

# Return to root directory for Gradle build
WORKDIR /taplink

# Clean, build Angular, and package the jar (skipping unit tests in container)
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:25-jre
WORKDIR /taplink

# Copy the built jar from the build stage
COPY --from=build /taplink/build/libs/*.jar app.jar

EXPOSE 1005
ENTRYPOINT ["java", "-jar", "app.jar"]