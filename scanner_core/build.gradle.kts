@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(projects.ruleCore)

    api(libs.chapi.domain) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }
    
    api(libs.coroutines.core)
    api(libs.serialization.json)

    testImplementation(libs.bundles.test)
}
