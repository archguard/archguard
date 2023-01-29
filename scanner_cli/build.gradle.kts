@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":scanner_core"))
    implementation(project(":rule-core"))

    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)
    implementation(libs.chapi.domain)

    implementation(libs.serialization.json)
    implementation(libs.serialization.protobuf)

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
    implementation("ch.qos.logback:logback-core:1.3.0-alpha14")

    testImplementation(libs.bundles.test)
}

application {
    mainClass.set("org.archguard.scanner.ctl.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.ctl.RunnerKt"))
        }
        // minimize()
        dependencies {
            exclude(dependency("org.junit.jupiter:.*:.*"))
            exclude(dependency("org.junit:.*:.*"))
            exclude(dependency("junit:.*:.*"))
        }
    }
}
