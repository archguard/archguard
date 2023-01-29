@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
    id("io.kotest.multiplatform") version "5.5.4"
}

dependencies {
    api(project(":scanner_core"))
    implementation(libs.coroutines.core)

    implementation("com.google.re2j:re2j:1.7")

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.junit5)
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
