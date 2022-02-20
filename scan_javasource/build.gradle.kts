plugins {
    id("application")
    id("com.thougthworks.archguard.java-conventions")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.thoughtworks.archguard.scanners"

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}