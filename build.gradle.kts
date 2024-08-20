import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.jpa") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// yml 파일 암호화하기 위해
	implementation ("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3")
	// WebClient 사용하기 위해
	implementation("org.springframework:spring-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("org.jetbrains.kotlin", "kotlin-stdlib","1.9.24")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	runtimeOnly ("com.h2database:h2")
	testImplementation ("com.h2database", "h2", "2.3.232")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

noArg {
	annotation("jakarta.persistence.Entity")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
