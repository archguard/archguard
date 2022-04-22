plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":scanner_core"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    // http client, on trial
    implementation("io.ktor:ktor-client-core:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
    // chapi domain
    implementation("com.phodal.chapi:chapi-domain:1.5.6")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.core.AnalyserKt"))
        }
    }
}
