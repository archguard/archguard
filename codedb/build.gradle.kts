import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	id("org.springframework.boot") version "2.7.6-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.0"
//	id("org.graalvm.buildtools.native") version "0.9.17"
	kotlin("jvm") version "1.7.20"
	kotlin("plugin.spring") version "1.7.20"
	kotlin("plugin.serialization") version "1.7.20"
}

group = "org.archguard"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("com.phodal.chapi:chapi-domain:2.0.0-beta.4")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
}

//tasks.withType<BootBuildImage> {
//	buildpacks.set(listOf("gcr.io/paketo-buildpacks/bellsoft-liberica:9.9.0-ea", "gcr.io/paketo-buildpacks/java-native-image"))
//}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
