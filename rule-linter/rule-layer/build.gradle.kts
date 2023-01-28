@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)

    kotlin("plugin.serialization") version "1.6.21"

    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":rule-core"))
    api(project(":scanner_core"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.phodal.chapi:chapi-domain:2.0.0-beta.9")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
}

application {
    mainClass.set("org.archguard.rule.RulerKt")
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

