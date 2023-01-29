@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    alias(libs.plugins.shadow)
}

dependencies {
    api(project(":rule-core"))

    implementation(libs.serialization.json)

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

