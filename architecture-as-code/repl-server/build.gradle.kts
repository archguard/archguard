plugins {
    application
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

application {
    mainClass.set("org.archguard.archdoc.ApplicationKt")
}

dependencies {
    api(project(":architecture-as-code:repl-api"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    implementation("io.ktor:ktor-server-core:2.0.1")
    implementation("io.ktor:ktor-server-netty:2.0.1")
    implementation("io.ktor:ktor-server-websockets:2.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")

    implementation("ch.qos.logback:logback-classic:1.2.11")

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
