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
    val jdbiVersion = "3.14.1"
    antlr("org.antlr:antlr4:4.7.2")
    implementation("org.antlr:antlr4-runtime:4.7.2")

    implementation("io.pebbletemplates:pebble:3.0.8")

    implementation("org.jdbi:jdbi3-core:$jdbiVersion")
    implementation("org.jdbi:jdbi3-spring4:$jdbiVersion") // provide JdbiFactoryBean
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.10.1")

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
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion")
    implementation("org.jdbi:jdbi3-testing:$jdbiVersion")
    implementation("org.nield:kotlinstatistics:0.3.0")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:2.2.3.RELEASE")
    implementation("com.google.code.gson:gson:2.8.6")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.flywaydb:flyway-core:6.2.2")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.30")
    testImplementation("io.mockk:mockk:1.10.0")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR6")
    }
}

tasks.getByName<BootJar>("bootJar") {
    requiresUnpack("**/kotlin-compiler-embeddable-*.jar")
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

