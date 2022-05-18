plugins {
    application
    id("org.jetbrains.kotlin.jupyter.api") version "0.11.0-89-1"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

application {
    mainClass.set("org.archguard.archdoc.ApplicationKt")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-jupyter-api-gradle-plugin:0.11.0-89-1")
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    implementation("org.archguard.scanner:doc-executor:2.0.0-alpha.6")

    implementation("io.ktor:ktor-server-core:2.0.1")
    implementation("io.ktor:ktor-server-netty:2.0.1")
    implementation("io.ktor:ktor-server-websockets:2.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")

    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:0.11.0-89-1")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-kernel:0.11.0-89-1")

    implementation("io.ktor:ktor-server-netty:1.6.7")
    implementation("io.ktor:ktor-html-builder:1.6.7")

    implementation(kotlin("stdlib"))
    // test
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
