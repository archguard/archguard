@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(project(":meta"))
    implementation(project(":scanner_core"))

    testImplementation(libs.bundles.test)
}
