val ktlint by configurations.creating

plugins {
    // springfox not support spring boot 2.6, see in https://github.com/springfox/springfox/issues/3462
    id("org.springframework.boot") version "2.7.0"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("jvm") version "1.6.21"

    // flyway 7.0 require spring .boot > 2.4
    id("org.flywaydb.flyway").version("7.15.0")
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.avast.gradle.docker-compose") version "0.15.2"

    id("org.jetbrains.dokka") version "1.6.21"
}

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.withType<Javadoc>().all { enabled = false }
tasks.getByName("javadocJar") { enabled = false }
tasks.getByName("sourcesJar") { enabled = false }
tasks.getByName<Jar>("jar") {
    enabled = false
}

configurations {
    all {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

dependencies {
    // architecture as code part
//    implementation("org.archguard.scanner:scanner_core:2.0.0-alpha.7")
    api(project(":scanner_core"))

    api(project(":architecture-as-code:dsl"))
    api(project(":architecture-as-code:repl-api"))

    ktlint("com.pinterest:ktlint:0.44.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // kotlin configs
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // test
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    implementation("org.junit.platform:junit-platform-commons:1.8.2")

    // kotlin coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    implementation("org.jdbi:jdbi3-core:3.28.0")
    implementation("org.jdbi:jdbi3-spring4:3.19.0")    // provide JdbiFactoryBean
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.28.0")
    implementation("org.jdbi:jdbi3-kotlin:3.10.1")
    implementation("org.jdbi:jdbi3-testing:3.28.0")

//    implementation("io.springfox:springfox-boot-starter:3.0.0")
//    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-websocket:2.7.0")
    // cache for overview api
    implementation("org.springframework.boot:spring-boot-starter-cache:2.7.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")

    implementation("org.nield:kotlinstatistics:0.3.0")
    implementation("com.alibaba:druid-spring-boot-starter:1.2.8")

    implementation("dom4j:dom4j:1.6.1")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    implementation("org.flywaydb:flyway-core:7.15.0")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.github.database-rider:rider-spring:1.16.1")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.flywaydb:flyway-core:6.5.7")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR6")
    }
}
