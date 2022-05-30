plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    api(project(":rule-core"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.phodal.chapi:chapi-domain:2.0.0-beta.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
}
