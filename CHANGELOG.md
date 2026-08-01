# Changelog

All notable changes to the **TapLink** project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to Semantic Versioning.

---

## [0.1.0] - 2026-08-02

### Added
* **Unified Full-Stack Architecture:** Integrated Spring Boot 4.1 (Java 25) backend API with Angular (Standalone) frontend client via Gradle automation.
* **Smart Dev Orchestration:** Added `npm run start:all` task powered by `concurrently` to run Angular live-reload server (Port 4200) alongside Spring Boot dev server.
* **Direct UI Access Guard:** Implemented smart security intercepts/filters in dev mode (`taplink.mode=local-dev`) to safely block accidental direct browser navigation to UI routes on backend port 1005 with redirect links to port 4200, while leaving APIs and Swagger fully open.
* **Automated Production Packaging:** Configured custom Gradle `build` tasks to trigger optimized Angular production builds (`npm run build:prod`) and bundle assets directly into Spring Boot static resources.
* **Professional Documentation & Governance:** Added comprehensive `README.md`, `CONTRIBUTING.md`, India-jurisdiction compliant MIT `LICENSE.md`, and robust `.gitignore`.