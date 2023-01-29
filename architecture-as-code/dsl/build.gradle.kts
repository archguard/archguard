@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

dependencies {
    api(project(":architecture-as-code:domain"))

    implementation(libs.serialization.json)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.bundles.test)
}
