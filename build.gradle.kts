plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.projectlombok:lombok:1.18.30")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.retry:spring-retry") // Retry
	implementation("org.springframework.boot:spring-boot-starter-aop") // Retry
	testImplementation("org.awaitility:awaitility:4.2.0")
	implementation("org.springframework.boot:spring-boot-starter-web") // For REST API
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb") // For MongoDB integration
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-validation") // Validation
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-core:5.5.0")
    implementation("com.google.firebase:firebase-admin:9.1.1") // Firebase Admin SDK for Authentication
    implementation("org.springframework.boot:spring-boot-starter-security") // Spring Boot Security
	implementation("org.springframework.boot:spring-boot-starter-actuator") // Mapping endpoints
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Auto restart - doesn't work
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
