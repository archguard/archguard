@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    api(projects.scannerCore)

    implementation("com.jayway.jsonpath:json-path:2.7.0")
    implementation("com.akuleshov7:ktoml-core:0.5.0")
    implementation("com.akuleshov7:ktoml-file:0.5.0")

    implementation("com.phodal.chapi:chapi-parser-toml:2.2.4")

    testImplementation(libs.bundles.test)
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
