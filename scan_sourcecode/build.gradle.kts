group = "com.thoughtworks.archguard.scanner"

plugins {
    id("application")
    id("com.thougthworks.archguard.java-conventions")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    api(project(":common"))

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.phodal.chapi:chapi-application:1.2.3")
    implementation("com.phodal.chapi:chapi-domain:1.2.3")

    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

application {
    mainClass.set("com.thoughtworks.archguard.scanner.sourcecode.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.thoughtworks.archguard.scanner.sourcecode.RunnerKt"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


