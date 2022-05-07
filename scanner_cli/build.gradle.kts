plugins {
    application
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":scanner_core"))
    implementation(project(":rule-core"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("com.phodal.chapi:chapi-domain:1.5.6")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
    implementation("ch.qos.logback:logback-core:1.3.0-alpha14")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

application {
    mainClass.set("org.archguard.scanner.ctl.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.ctl.RunnerKt"))
        }
        // minimize()
        dependencies {
            exclude(dependency("org.junit.jupiter:.*:.*"))
            exclude(dependency("org.junit:.*:.*"))
            exclude(dependency("junit:.*:.*"))
        }
    }
}
