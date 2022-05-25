plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "org.archguard.aaac"

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0-M1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter:5.9.0-M1")
}
