import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val ktlint by configurations.creating


buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.19")
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    // springfox not support spring boot 2.6, see in https://github.com/springfox/springfox/issues/3462
    alias(libs.plugins.springboot)

    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.jvm)
    alias(libs.plugins.dokka)

    id("org.flywaydb.flyway").version("7.15.0")
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.avast.gradle.docker-compose") version "0.15.2"

    id("com.google.protobuf") version "0.8.19"
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
    // architecture as code part
    api(projects.scannerCore)
    api(projects.server.metricService)

    api(projects.architectureAsCode.domain)
    api(projects.architectureAsCode.dsl)
    api(projects.architectureAsCode.replApi)

    ktlint("com.pinterest:ktlint:0.44.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // kotlin configs
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // kotlin coroutine
    implementation(libs.coroutines.core)

    implementation("org.jdbi:jdbi3-core:3.28.0")
    implementation("org.jdbi:jdbi3-spring4:3.19.0")    // provide JdbiFactoryBean
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.28.0")
    implementation("org.jdbi:jdbi3-kotlin:3.10.1")
    implementation("org.jdbi:jdbi3-testing:3.28.0")

    implementation("com.google.protobuf:protobuf-java:3.21.5")
    implementation("com.googlecode.protobuf-java-format:protobuf-java-format:1.4")

    implementation(libs.springboot.jdbc)
    implementation(libs.springboot.web)
    implementation(libs.springboot.actuator)
    implementation(libs.springboot.validation)
    implementation(libs.springboot.websocket)
    // cache for overview api
    implementation(libs.springboot.cache)
    testImplementation(libs.springboot.test)

    implementation("org.nield:kotlinstatistics:0.3.0")
    implementation("com.alibaba:druid-spring-boot-starter:1.2.8")

    implementation("dom4j:dom4j:1.6.1")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.+")

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    implementation("org.flywaydb:flyway-core:7.15.0")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.github.database-rider:rider-spring:1.16.1")
    testImplementation("com.h2database:h2:1.4.200")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR6")
    }
}
