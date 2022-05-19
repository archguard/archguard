plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "org.archguard.aaac"

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation(kotlin("stdlib"))
    // test
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}
