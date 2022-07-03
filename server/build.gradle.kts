import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val ktlint by configurations.creating

plugins {
    // springfox not support spring boot 2.6, see in https://github.com/springfox/springfox/issues/3462
    id("org.springframework.boot") version "2.7.0"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("jvm") version "1.6.21"

    // flyway 7.0 require spring .boot > 2.4
    id("org.flywaydb.flyway").version("7.15.0")
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
//    id("com.avast.gradle.docker-compose") version "0.15.2"

    id("org.jetbrains.dokka") version "1.6.21"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

// allow script to unpack
// when spring boot start, those packages will unpack to some dir, so we can call it REPL.
tasks.withType<BootJar> {
    requiresUnpack("**/kotlin-compiler-*.jar")
    requiresUnpack("**/kotlin-script-*.jar")
    requiresUnpack("**/kotlin-jupyter-*.jar")
    requiresUnpack("**/dsl-*.jar")
}

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

dependencies {
    // internal dependencies
    api(project(":scanner_core"))
    api(project(":architecture-as-code:domain"))
    api(project(":architecture-as-code:dsl"))
    api(project(":architecture-as-code:repl-api"))

    ktlint("com.pinterest:ktlint:0.44.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // kotlin core
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    // kotlin core test
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    implementation("org.junit.platform:junit-platform-commons:1.8.2")
    testImplementation("io.mockk:mockk:1.12.3")

    // kotlin coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")

    // deprecated: mysql and jdbi
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.alibaba:druid-spring-boot-starter:1.2.8")
    implementation("org.jdbi:jdbi3-core:3.28.0")
    implementation("org.jdbi:jdbi3-spring4:3.19.0")    // provide JdbiFactoryBean
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.28.0")
    implementation("org.jdbi:jdbi3-kotlin:3.10.1")
    implementation("org.jdbi:jdbi3-testing:3.28.0")
    implementation("org.flywaydb:flyway-core:7.15.0")
    runtimeOnly("mysql:mysql-connector-java:8.0.29")
    testImplementation("com.github.database-rider:rider-spring:1.16.1")
    testImplementation("com.h2database:h2:1.4.200")
//    testImplementation("org.flywaydb:flyway-core:6.5.7")

    // utils
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("dom4j:dom4j:1.6.1")
    implementation("org.nield:kotlinstatistics:0.3.0")
    implementation("com.google.code.gson:gson:2.8.6")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR6")
    }
}
