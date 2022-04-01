group = "com.thoughtworks.archguard.scanners"
description = "scan_jacoco"

plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")

    implementation("org.jacoco:org.jacoco.core:0.8.7")
}

application {
    mainClass.set("com.thoughtworks.archguard.scanner.jacoco.RunnerKt")
}
