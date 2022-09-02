import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import java.util.regex.Pattern.compile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id("org.jetbrains.kotlin.kapt") version "1.7.10"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("kapt") version "1.6.21"
}

group = "com.example.doma"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
	compile("org.seasar.doma.boot:doma-spring-boot-starter:1.1.1")
	implementation("org.seasar.doma:doma:2.24.0")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks

project.extensions.findByType(org.jetbrains.kotlin.gradle.plugin.KaptExtension::class.java)?.let { kapt ->
	kapt.arguments {
		arg("sample.argument", "sample value")
	}
}

tasks.register("copyDomaResources",Sync::class){
	from("src/main/resources")
	into(compileKotlin.destinationDir)
	include("doma.compile.config")
	include("META-INF/**/*.sql")
	include("META-INF/**/*.script")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	dependsOn(tasks.getByName("copyDomaResources"))
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}