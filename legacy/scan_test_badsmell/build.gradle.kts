group = "com.thoughtworks.archguard.scanner"

plugins {
    id("application")
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    api(project(":rule-core"))
    api(project(":linter:rule-test-code"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("com.phodal.chapi:chapi-domain:1.5.6")
    implementation("com.phodal.chapi:chapi-application:1.5.6")
}

application {
    mainClass.set("org.archguard.scanner.tbs.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.tbs.RunnerKt"))
        }
    }
}
