import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.thoughtworks.archguard.ident"

plugins {
    java
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation("com.github.jsqlparser:jsqlparser:4.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("org.slf4j:slf4j-jdk14:1.7.36")
//    implementation("org.slf4j:slf4j-api:1.7.20")
    implementation("io.netty:netty-all:4.1.42.Final")


    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
