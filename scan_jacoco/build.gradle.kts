group = "com.thougthworks.archguard.scanners"
description = "scan_jacoco"

plugins {
    id("antlr")
    id("application")
    id("com.thougthworks.archguard.java-conventions")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.30")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

application {
    mainClass.set("com.thoughtworks.archguard.scanner.jacoco.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.thoughtworks.archguard.scan_jacoco.RunnerKt"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

