plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-compiler:1.6.10")
    api(project(":common_code_repository"))

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")
}

application {
    mainClass.set("org.archguard.doc.generator.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.doc.generator.RunnerKt"))
        }
    }
}
