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
    // --- Spring Boot Core ---
    implementation("org.springframework.boot:spring-boot-starter-web")

    // --- Database, JPA, & Migrations ---
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // --- Security & Authentication (JWT) ---
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // --- QR & Barcode Generation (ZXing) ---
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    // --- API Documentation (Swagger) ---
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // --- Tooling & Utilities ---
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // --- Testing ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Helper function to load .env file into a Map
fun loadEnvFile(): Map<String, String> {
    val envMap = mutableMapOf<String, String>()
    val envFile = File(projectDir, ".env")

    if (envFile.exists()) {
        envFile.forEachLine { line ->
            val trimmed = line.trim()
            if (trimmed.isNotBlank() && !trimmed.startsWith("#")) {
                val parts = trimmed.split("=", limit = 2)
                if (parts.size == 2) {
                    envMap[parts[0].trim()] = parts[1].trim()
                }
            }
        }
    }
    return envMap
}

// Inject environment variables into the 'bootRun' task (local development)
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    loadEnvFile().forEach { (key, value) ->
        environment(key, value)
    }
}

// Inject environment variables into the 'test' task (local testing)
tasks.withType<Test> {
    loadEnvFile().forEach { (key, value) ->
        environment(key, value)
    }
}

// ==========================================
// --- Step 1: Custom Clean Configuration ---
// ==========================================
tasks.named<Delete>("clean") {
    // 1. Clear compiled Angular SPA bundles from Spring Boot's static folder
    delete("src/main/resources/static")

    // 2. Clear generated structure text file
    delete("taplink-application-structure.txt")

    // 3. Clear local log files and any logs directory
    delete(fileTree(projectDir){
        include("**/*.log")
        include("logs/**")
    })
}

// ==========================================
// --- Step 2: Angular Build Automation ---
// ==========================================
val buildAngular = tasks.register<Exec>("buildAngular") {
    group = "build"
    description = "Builds the Angular frontend for production and outputs into Spring Boot static resources"

    workingDir = file("views/taplink")

    val isWindows = System.getProperty("os.name").lowercase().contains("win")
    if (isWindows) {
        commandLine("cmd", "/c", "npm", "run", "build", "--", "--configuration", "production")
    } else {
        commandLine("npm", "run", "build", "--", "--configuration", "production")
    }
}

// Ensure production builds correctly bundle Angular into static resources for JAR packaging
tasks.named("processResources") {
    dependsOn(buildAngular)
}

// ==========================================
// --- Step 3: Project Structure Summary ---
// ==========================================
tasks.register("printProjectStructure") {
    group = "help"
    description = "Displays and saves the full file and path structure of the project after a build."

    doLast {
        val structureBuilder = StringBuilder()
        structureBuilder.append("\n=== 📁 TAPLINK FULL-STACK PROJECT STRUCTURE ===\n")

        projectDir.walkTopDown()
            .maxDepth(6)
            .filter { file ->
                val path = file.absolutePath
                !path.contains("node_modules") && !path.contains(".angular") && !path.contains(".git") &&
                        !path.contains("build") && !path.contains(".gradle") && !path.contains(".vscode") &&
                        !path.contains("dist") && !path.contains(".idea")
            }
            .forEach { file ->
                val relativePath = file.relativeTo(projectDir)
                if (relativePath.path.isNotEmpty()) {
                    val indent = "  ".repeat(relativePath.invariantSeparatorsPath.count { it == '/' })
                    val prefix = if (file.isDirectory) "📂" else "📄"
                    structureBuilder.append("$indent$prefix ${file.name}\n")
                }
            }
        structureBuilder.append("===================================================\n")

        val outputText = structureBuilder.toString()
        println(outputText)
        file("taplink-application-structure.txt").writeText(outputText)
    }
}

tasks.named("build") {
    finalizedBy("printProjectStructure")
}