@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

dependencies {
    api(libs.serialization.json)
    implementation(libs.chapi.domain)
    implementation(project(":server:metric-service"))

    testImplementation(libs.bundles.test)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}