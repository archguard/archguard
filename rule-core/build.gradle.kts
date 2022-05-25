plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
