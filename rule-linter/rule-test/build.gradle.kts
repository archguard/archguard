@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":rule-core"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation(libs.chapi.domain)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.bundles.test)
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

