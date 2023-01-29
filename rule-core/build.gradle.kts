@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(project(":meta"))

    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    testImplementation("org.reflections:reflections:0.10.2")

    testImplementation(libs.bundles.test)

    implementation(libs.serialization.json)
}
