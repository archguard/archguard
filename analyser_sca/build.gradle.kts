plugins {
    application

    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.jayway.jsonpath:json-path:2.7.0")
}

application {
    mainClass.set("org.archguard.analyser.sca.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.analyser.sca.RunnerKt"))
        }
    }
}
