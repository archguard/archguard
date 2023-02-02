@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(libs.coroutines.core)
    api(libs.serialization.json)

    implementation(libs.springboot.jdbc)
    implementation(libs.springboot.web)

    testImplementation(libs.bundles.test)
    testImplementation(libs.springboot.test)
}
