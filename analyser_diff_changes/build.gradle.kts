@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    api(projects.scannerCore)

    // should get the code data from language analyser
    implementation(libs.chapi.kotlin) {
        exclude(group = "com.ibm.icu", module = "icu4j")
    }
    implementation(libs.chapi.java) {
        exclude(group = "com.ibm.icu", module = "icu4j")
    }

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r")

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
