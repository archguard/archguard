plugins {
    application
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":scanner_core"))

    implementation("com.phodal.chapi:chapi-ast-csharp:1.5.6") {
        // around 10mb, only documents files, exclude (reuse in cli?)
        exclude(group = "com.ibm.icu", module = "icu4j")
    }

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

application {
    mainClass.set("org.archguard.scanner.core.AnalyserKt")
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
            exclude(dependency("org.jetbrains.kotlinx:.*:.*"))
        }
        minimize()
    }
}
