@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.scannerCore)
    implementation(projects.ruleCore)

    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)
    implementation(libs.chapi.domain)

    implementation(libs.serialization.json)
    implementation(libs.serialization.protobuf)

    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.format.csv)

    implementation(libs.clikt)

    implementation(libs.logback.classic)

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
