import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	antlr
}

group = "com.thoughtworks.archguard"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	jcenter {
		url = uri("https://jcenter.bintray.com")
	}
	maven {
		url = uri("https://oss.sonatype.org/content/repositories/snapshots")
	}
	mavenCentral()
}

dependencies {
	antlr("org.antlr:antlr4:4.7.2")
	implementation("org.antlr:antlr4-runtime:4.7.2")

	implementation("io.pebbletemplates:pebble:3.0.8")

	implementation("org.jdbi:jdbi3-core:3.8.2")

	implementation("io.ktor:ktor-server-core:1.1.4")
	implementation("io.ktor:ktor-server-netty:1.1.4")
	implementation("io.ktor:ktor-gson:1.1.4")

	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.30")

	implementation("com.pinterest.ktlint:ktlint-core:0.31.0-SNAPSHOT")
	implementation("com.pinterest.ktlint:ktlint-ruleset-standard:0.31.0-SNAPSHOT")
	implementation("com.pinterest.ktlint:ktlint-ruleset-experimental:0.31.0-SNAPSHOT")

	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jdbi:jdbi3-kotlin:3.10.1")
	implementation("org.jdbi:jdbi3-testing:3.10.1")
	runtimeOnly("mysql:mysql-connector-java")

	testImplementation("com.h2database:h2")
	testImplementation("org.flywaydb:flyway-core:6.2.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("junit:junit:4.11")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.30")

}


tasks.getByName<BootJar>("bootJar") {
	requiresUnpack("org.jetbrains.kotlin:kotlin-compiler")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	dependsOn(":generateGrammarSource")
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.generateGrammarSource {
	maxHeapSize = "64m"
	arguments = arguments + listOf("-visitor", "-long-messages")
	arguments = arguments + listOf("-package", "com.thoughtworks.archguard.plsql")
}

