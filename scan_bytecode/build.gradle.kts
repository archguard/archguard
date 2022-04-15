group = "org.archguard.scanner"

plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    api(project(":common_code_repository"))

    implementation("io.netty:netty-all:4.1.42.Final")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.jdbi:jdbi3-core:3.8.2")

    implementation("com.phodal.chapi:chapi-domain:1.5.6")

    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-util:9.2")
}

application {
    mainClass.set("org.archguard.scanner.bytecode.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.bytecode.RunnerKt"))
        }
    }
}
