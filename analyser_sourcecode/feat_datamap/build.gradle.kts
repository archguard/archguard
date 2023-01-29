@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":scanner_core"))

    implementation("org.mybatis:mybatis:3.5.9")
    implementation("ognl:ognl:3.3.2") // for mybatis expression
    implementation("com.github.jsqlparser:jsqlparser:4.4")

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
