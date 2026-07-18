plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.co"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-h2console")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
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
            // Skip empty lines and comments
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

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    val env = loadEnvFile()
    println("🔍 DIAGNOSTIC: Loaded ${env.size} variables from .env file!")

    env.forEach { (key, value) ->
        environment(key, value)
    }
}