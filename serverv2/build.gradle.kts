plugins {
	id("org.springframework.boot") version "2.7.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"

	id("org.jetbrains.dokka") version "1.6.21"
}

group = "org.archguard"

repositories {
	mavenCentral()
	mavenLocal()
}

configurations {
	all {
		exclude(group = "junit", module = "junit")
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

val ktlint by configurations.creating

dependencies {
	api(project(":scanner_core"))

	ktlint("com.pinterest:ktlint:0.44.0") {
		attributes {
			attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
		}
	}

	// kotlin configs
	implementation(kotlin("stdlib"))
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))

	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
