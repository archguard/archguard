@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    api(libs.coroutines.core)
    api(libs.serialization.json)

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.+")

    implementation("org.jdbi:jdbi3-core:3.28.0")
    implementation("org.nield:kotlinstatistics:0.3.0")

    testImplementation(libs.bundles.test)
}
