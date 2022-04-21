plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":scanner_core"))

    implementation("com.phodal.chapi:chapi-ast-kotlin:1.5.6")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.core.AnalyserKt"))
        }
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
            exclude(dependency("org.jetbrains.kotlinx:.*:.*"))
        }
        minimize()

        // quick test
        // all in one: 24m
        // exclude kotlin: 17.9m
        // minimize: 11.4m
        // if want more...exclude test dependencies (junit, assertj,...) and anything can be reused in scanner_cli
    }
}
