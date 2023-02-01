@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(libs.coroutines.core)
    api(libs.serialization.json)

    testImplementation(libs.bundles.test)
}
