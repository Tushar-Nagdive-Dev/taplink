# Taplink Full-Stack Setup & Operation Guide

Welcome to the **Taplink** project! This repository integrates a **Spring Boot (Java 25)** backend API with an **Angular (Standalone)** frontend client into a unified, high-performance architecture.

---

## 🛠️ System Prerequisites

Ensure you have the following installed on your development machine:

* **Java Development Kit (JDK) 25**
* **Node.js** (v18+ recommended) & **npm**
* **Gradle** (or use the included `./gradlew wrapper`)
* **MySQL** (or H2 for local testing)

---

## ⚙️ Environment Configuration

Taplink uses a local `.env` file at the root of the project to inject database credentials and security secrets safely during development and testing.

1. Create a `.env` file in the root directory:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/taplink_db?useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key_here_make_it_long_and_secure

```


2. *Note: Spring Boot's `build.gradle.kts` automatically parses and injects these variables into your `bootRun`, `runOnlyDev`, and `test` tasks.*

---

## 🚀 Build & Development Workflows

### 1. Local Development Workflow (Recommended)

For day-to-day coding with live-reloading, hot-module replacement, and secure direct-access guards, navigate to your Angular project directory (`views/taplink`) or use your root orchestration script:

* **Start Full-Stack Development:**
```bash
npm run start:all

```


* *What it does:* Concurrently launches the Angular live-reload development server on port `4200` via `ng serve` and fires up the Spring Boot backend in secure dev mode (`./gradlew runOnlyDev`) on port `1005`.


* **About `runOnlyDev`:** This dedicated Gradle task (`./gradlew runOnlyDev`) injects the `taplink.mode=local-dev` property. It activates an intelligent safeguard that instantly blocks any accidental direct browser UI navigation to port `1005` (e.g., trying to visit `http://localhost:1005/signin`), presenting a clean developer redirect portal with a direct clickable link back to port `4200`. Meanwhile, API routes (`/api/**`) and Swagger (`/swagger-ui/**`) remain fully accessible for proxying.

### 2. Production Build & Packaging

When you want to bundle your Angular frontend into a production-ready single-jar package:

* **Build Production Bundle & JAR:**
```bash
./gradlew build

```


* *What it does:* Automatically executes `npm run build:prod` to compile optimized production Angular bundles directly into Spring Boot's static resources (`src/main/resources/static`), triggers project structure reporting, and packages everything into an executable JAR.



### 3. Running the Production/Bundled App

To run the fully compiled standalone application (where Spring Boot serves both the API and the static UI from port `1005`):

```bash
./gradlew bootRun

```

### 4. Cleaning Build Outputs

To clear old build artifacts, compiled Angular bundles from static folders, logs, and generated structure reports:

```bash
./gradlew clean

```

---

## 🌐 Application URLs & Ports

* **Angular Dev Server (Local Development UI):** `http://localhost:4200`
* **Spring Boot API Server / Direct Access Dev Portal:** `http://localhost:1005`
* **Swagger API Documentation:** `http://localhost:1005/swagger-ui/index.html`
* **REST API Endpoints:** `http://localhost:1005/api/v1/...`

---

## 📁 Enhanced Project Structure

```text
📂 taplink/
├── 📂 .gradle/ & build/              # Gradle compilation and build caches
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/org/co/taplink/  # Spring Boot backend source code
│   │   │   ├── 📂 config/           # Security, WebMVC, and Interceptor configurations
│   │   │   ├── 📂 controller/       # REST controllers and dev security routers
│   │   │   ├── 📂 model/            # JPA Entities and database models
│   │   │   ├── 📂 repository/       # Spring Data JPA repositories
│   │   │   └── 📂 service/          # Business logic implementation layer
│   │   └── 📂 resources/
│   │       ├── 📂 static/           # Auto-populated during `./gradlew build` (Production SPA)
│   │       ├── 📂 db/migration/     # Flyway SQL migration scripts
│   │       └── application.yml      # Spring Boot core properties
│   └── 📂 test/                     # Backend unit and integration tests
├── 📂 views/
│   └── 📂 taplink/                  # Angular Standalone Frontend Application
│       ├── 📂 src/                  # Components, routes, environments, and proxy config
│       └── package.json             # Frontend dependencies and npm scripts (`start:all`, `build:prod`)
├── .env                             # Local environment secrets (Git-ignored)
├── build.gradle.kts                 # Root Gradle build script with custom task definitions
└── taplink-application-structure.txt # Auto-generated project layout tracker

```
```python
markdown_content = """# TapLink Full-Stack Deployment & CI/CD Documentation

This document records the complete deployment, automation, and cloud infrastructure setup for the **TapLink** full-stack application (Spring Boot backend + Angular frontend).

---

## 1. Architecture Overview

The TapLink project is a full-stack web application structured as follows:
* **Backend:** Java 25, Spring Boot 4.1.0, Spring Data JPA, Flyway Migrations, Spring Security (JWT), MySQL.
* **Frontend:** Angular SPA located under `views/taplink`.
* **Containerization:** Multi-stage Dockerfile compiling both Angular and Gradle inside an Eclipse Temurin JDK 25 environment, running on a JRE 25 runtime container.
* **Cloud Infrastructure:**
  * **Render:** Hosts the production Web Service via Docker container.
  * **Aiven:** Provides the managed cloud MySQL database instance.
  * **GitHub / GitHub Actions:** Manages version control, automated CI checks, SonarCloud quality gates, and CD triggers.

---

## 2. Gradle Build Automation (`build.gradle.kts`)

The custom `build.gradle.kts` configuration automates environment variable loading, Angular frontend production compilation, and project file structure logging.


```

```text
Markdown documentation generated successfully.

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.co"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Custom clean task clearing Angular static bundles and logs
tasks.named<Delete>("clean") {
    delete("src/main/resources/static")
    delete("taplink-application-structure.txt")
    delete(fileTree(projectDir){
        include("**/*.log")
        include("logs/**")
    })
}

// Angular frontend build integration
val buildAngular = tasks.register<Exec>("buildAngular") {
    group = "build"
    description = "Builds Angular frontend and outputs into Spring Boot static resources"
    workingDir = file("views/taplink")
    val isWindows = System.getProperty("os.name").lowercase().contains("win")
    if (isWindows) {
        commandLine("cmd", "/c", "npm", "run", "build", "--", "--configuration", "production")
    } else {
        commandLine("npm", "run", "build:prod")
    }
}

tasks.named("processResources") {
    if (gradle.startParameter.taskNames.any { it.contains("build") || it.contains("assemble") || it.contains("jar") }) {
        dependsOn(buildAngular)
    }
}

```

---

## 3. Containerization (`Dockerfile`)

The project uses a two-stage Dockerfile that installs Node.js, compiles the Angular SPA, packages the Spring Boot application jar via Gradle, and packages it into a lightweight runtime container.

```dockerfile
# Stage 1: Build the full-stack project using Gradle & Node
FROM eclipse-temurin:25-jdk AS build
WORKDIR /taplink

# Install Node.js and npm for Angular
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL [https://deb.nodesource.com/setup_20.x](https://deb.nodesource.com/setup_20.x) | bash - && \
    apt-get install -y nodejs

# Copy project files
COPY . .

# Grant execution permissions to Gradle wrapper
RUN chmod +x gradlew

# Install Angular / Frontend dependencies
WORKDIR /taplink/views/taplink
RUN npm install

# Return to root for Gradle build
WORKDIR /taplink

# Clean, build Angular, and package jar (skipping unit tests in container)
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:25-jre
WORKDIR /taplink

# Copy built jar from build stage
COPY --from=build /taplink/build/libs/*.jar app.jar

EXPOSE 1005
ENTRYPOINT ["java", "-jar", "app.jar"]

```

---

## 4. Production Profile Configuration (`application-prod.yml`)

Configured with explicit database driver requirements and fallback mechanisms to ensure safe initialization:

```yaml
server:
  port: ${PORT:1005}

spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    defer-datasource-initialization: false
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: false

  flyway:
    enabled: true
    locations: 'classpath:db/migration'
    baseline-on-migrate: true
    baseline-version: "0"

application:
  security:
    jwt:
      secret-key: ${JWT_SECRET_KEY}
      expiration: ${JWT_EXPIRATION:86400000}

```

---

## 5. Cloud Database Setup (Aiven MySQL)

* **Provider:** Aiven Managed MySQL (Free Tier).
* **Connection Parameters:**
* **Driver:** `com.mysql.cj.jdbc.Driver`
* **URL Format:** `jdbc:mysql://<host>:<port>/defaultdb?ssl-mode=REQUIRED`
* **Authentication:** Configured via `DATABASE_USERNAME` and `DATABASE_PASSWORD` environment variables.



---

## 6. Render Deployment & CD Pipeline

* **Hosting Service:** Render Web Service (Docker Environment).
* **Environment Variables Configured on Render:**
* `SPRING_PROFILES_ACTIVE` = `prod`
* `DATABASE_DRIVER` = `com.mysql.cj.jdbc.Driver`
* `DATABASE_URL` = `jdbc:mysql://...`
* `DATABASE_USERNAME` = `avnadmin`
* `DATABASE_PASSWORD` = `[SECURE]`
* `JWT_SECRET_KEY` = `[SECURE]`


* **CD Management:** Auto-Deploy configured to controlled/manual states to prevent unintended builds while cloud database instances are powered down.
  """
  with open("taplink-deployment-docs.md", "w") as f:
  f.write(markdown_content)
  print("Markdown documentation generated successfully.")

```
Your comprehensive deployment documentation file has been successfully generated and saved!

Your documentation file is ready
[file-tag: code-generated-file-0-1785664867066959165]

Here is a preview of the documentation summarizing your full pipeline, Gradle configuration, Docker multi-stage build, production properties, Aiven MySQL integration, and Render CD setup:

### **TapLink Full-Stack Deployment & CI/CD Documentation Summary**
1. **Architecture Overview:** Details the Java 25 backend, Angular 20 frontend, Docker containerization, and cloud services.
2. **Gradle Build Automation:** Highlights your custom clean rules, Angular build process execution (`npm run build:prod`), and resource mapping into Spring Boot's static folder.
3. **Containerization (`Dockerfile`):** Documents the two-stage build installing Node.js 20, executing `./gradlew clean build -x test`, and packaging the final artifact.
4. **Production Configuration (`application-prod.yml`):** Outlines the strict `com.mysql.cj.jdbc.Driver` requirement, Flyway database migrations configuration, and security properties.
5. **Aiven MySQL Integration:** Records connection specifications and SSL requirements (`ssl-mode=REQUIRED`).
6. **Render CD Pipeline:** Explains environment variable injection and deployment control settings.

```