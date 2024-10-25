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
    implementation(projects.analyserEstimate)

    // architecture will load Database, HttpAPIs
    implementation(projects.analyserSourcecode.featDatamap)
    implementation(projects.analyserSourcecode.featApicalls)

    // analysis source code
    implementation(projects.analyserSourcecode.langJava)
    implementation(projects.analyserSourcecode.langKotlin)
    implementation(projects.analyserSourcecode.langGolang)

    implementation(projects.analyserSourcecode.idlProtobuf)

    implementation(projects.architectureAsCode.model)

    implementation(libs.chapi.protobuf) {
        exclude(group = "com.ibm.icu", module = "icu4j")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }

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
