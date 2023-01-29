@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "com.thoughtworks.aac"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("stdlib"))

    testImplementation(libs.bundles.test)
}
