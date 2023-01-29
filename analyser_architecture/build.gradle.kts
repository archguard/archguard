@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.meta)
    implementation(projects.scannerCore)

    testImplementation(libs.bundles.test)
}
