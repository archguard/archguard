plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation(project(":rule_core"))

    implementation("com.github.jsqlparser:jsqlparser:4.4")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("io.netty:netty-all:4.1.42.Final")
}
