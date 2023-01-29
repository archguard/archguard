@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    base
    java
    id("org.jetbrains.kotlin.jupyter.api") version "0.11.0-89-1"

    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

dependencies {
    api(project(":architecture-as-code:dsl"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:0.11.0-89-1")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-kernel:0.11.0-89-1")

    // tips: don't add follow deps to project will cause issues
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-jvm:1.6.21")

    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation(kotlin("stdlib"))

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
