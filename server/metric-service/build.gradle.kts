@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(libs.coroutines.core)
    api(libs.serialization.json)

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(libs.springboot.jdbc)
    implementation(libs.springboot.web)
    implementation("org.jdbi:jdbi3-core:3.28.0")

    testImplementation(libs.bundles.test)
    testImplementation(libs.springboot.test)
}
