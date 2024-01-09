@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.meta)
    implementation(projects.scannerCore)

    // analysis software composition analysis
    implementation(projects.analyserSca)

    // architecture will load Database, HttpAPIs
    implementation(projects.analyserSourcecode.featDatamap)
    implementation(projects.analyserSourcecode.featApicalls)

    // analysis source code
    implementation(projects.analyserSourcecode.langJava)
    implementation(projects.analyserSourcecode.langKotlin)

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
