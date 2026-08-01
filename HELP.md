# Taplink Full-Stack Setup & Operation Guide

Welcome to the **Taplink** project! This repository integrates a **Spring Boot 4.1 (Java 25)** backend API with an **Angular (Standalone)** frontend client into a single, unified build pipeline using Gradle.

---

## 🛠️ System Prerequisites

Ensure you have the following installed on your development machine:
* **Java Development Kit (JDK) 25**
* **Node.js** (v18+ recommended) & **npm**
* **Gradle** (or use the included `./gradlew wrapper`)
* **MySQL** (or H2 for local testing)

---

## ⚙️ Environment Configuration

Taplink uses a local `.env` file at the root of the project to inject database credentials and security secrets during development.

1. Create a `.env` file in the root directory:
   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/taplink_db?useSSL=false&serverTimezone=UTC
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=your_secure_password
   JWT_SECRET=your_jwt_secret_key_here_make_it_long_and_secure



2. *Note: Spring Boot's `build.gradle.kts` automatically loads these variables into `bootRun` and `test` tasks.*

---

## 🏗️ Build & Development Commands

All tasks are managed via Gradle from the project root directory.

### 1. Clean Everything

Wipes out old build outputs, compiled Angular bundles in static resources, local log files, and the structure text report:

```bash
./gradlew clean

```

### 2. Run Locally (Development Mode)

Automatically compiles the Angular frontend, sets up static resources, injects `.env` variables, and starts the Spring Boot server on port `1005`:

```bash
./gradlew bootRun

```

* **Frontend Dev Server (Optional):** If you want live-reloading for Angular separately, navigate to `views/taplink` and run `ng serve`, which proxies API requests to `http://localhost:1005`.

### 3. Production Build & Packaging

Triggers a production-optimized Angular build (`ng build --configuration production`), bundles the assets into Spring Boot's static directory, automatically prints and saves the project structure tree to `project-structure.txt`, and packages everything into a self-contained executable JAR:

```bash
./gradlew build

```

### 4. Inspect Project Structure

Manually generates or updates the **`project-structure.txt`** file and prints the full file tree in your console:

```bash
./gradlew printProjectStructure

```

---

## 🌐 Application URLs (Local Development)

Once `./gradlew bootRun` is running:

* **Web Application / UI:** `http://localhost:1005`
* **Swagger API Documentation:** `http://localhost:1005/swagger-ui/index.html`
* **REST API Endpoints:** `http://localhost:1005/api/v1/...`

---

## 📁 Key Directory Structure

* **`src/main/java/`** — Spring Boot backend controllers, services, security configs, and entities.
* **`src/main/resources/static/`** — Compiled Angular production assets (`index.html`, `.js`, `.css`) served natively by Spring Boot.
* **`views/taplink/`** — Angular standalone frontend application source code.
* **`project-structure.txt`** — Auto-generated layout tracker updated on every build.

```

```