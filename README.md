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