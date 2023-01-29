@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

dependencies {
    api(project(":architecture-as-code:domain"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation(libs.kotlin.reflect)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter:5.8.2")
}
