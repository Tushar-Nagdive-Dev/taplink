# Contributing to TapLink

Thank you for your interest in contributing to **TapLink**! To ensure a smooth development process and maintain architectural integrity, please follow the guidelines outlined below.

---

## 🛠️ Development Environment Setup

1. **Prerequisites:** Ensure you have **Java 25**, **Node.js (v18+)**, and **Gradle** installed.
2. **Clone & Configure:**
   * Clone the repository to your local machine.
   * Create a `.env` file in the root directory (refer to the `README.md` for required variables like database credentials and JWT secrets).

---

## 🚀 Running the Project Locally

* **Full-Stack Development Mode (Recommended):**
  Navigate to the Angular frontend directory (`views/taplink`) or run the root orchestration command to launch the Angular live-reload server (`http://localhost:4200`) alongside the Spring Boot dev backend (`http://localhost:1005`):
  ```bash
  npm run start:all
    ````

*(Note: The backend runs in `local-dev` mode, which includes security guards blocking direct UI access on port 1005).*

---

## 🏗️ Build & Testing Standards

Before submitting any code changes, ensure your code compiles and passes all checks:

* **Clean Build Artifacts:**
```bash
./gradlew clean

```


* **Run Production Build & Package:**
```bash
./gradlew build

```


*(This compiles the Angular production bundle into Spring Boot's static resources and packages the executable JAR).*

---

## 🌿 Branching Strategy & Pull Requests

1. **Branch Naming:** Create feature or bugfix branches from `main` using clear prefixes:
* `feature/your-feature-name`
* `bugfix/issue-description`


2. **Commit Messages:** Write concise, descriptive commit messages (e.g., `feat: add QR code generation endpoint` or `fix: resolve jwt expiration filter bug`).
3. **Pull Requests:** Open your pull request against the `main` branch with a clear description of changes, testing steps, and screenshots if UI components were modified.
