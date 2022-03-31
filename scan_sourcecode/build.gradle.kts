group = "org.archguard.scanner"

plugins {
    java
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    // for test chapi in Local
    mavenLocal()
}

dependencies {
    api(project(":source_repository"))
    api(project(":scan_mysql"))

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation(kotlin("stdlib"))

    implementation("org.slf4j:slf4j-api:1.7.20")
    implementation("io.netty:netty-all:4.1.42.Final")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // run `./gradlew publishToMavenLocal` to publish Chapi in local version
//    implementation("com.phodal.chapi:chapi-application:1.3.0-SNAPSHOT")
//    implementation("com.phodal.chapi:chapi-domain:1.3.0-SNAPSHOT")

    implementation("com.phodal.chapi:chapi-application:1.4.2")
    implementation("com.phodal.chapi:chapi-domain:1.4.2")

    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

application {
    mainClass.set("org.archguard.scanner.sourcecode.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.sourcecode.RunnerKt"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


