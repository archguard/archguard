@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(libs.bundles.test)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}