plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    implementation(project(":scanner_core"))

    implementation("com.phodal.chapi:chapi-ast-kotlin:1.5.6")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}
