plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    api("com.phodal.chapi:chapi-domain:1.5.6")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}
