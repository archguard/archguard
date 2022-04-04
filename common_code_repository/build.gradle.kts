plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("io.netty:netty-all:4.1.42.Final")

    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.jdbi:jdbi3-core:3.8.2")
    implementation("mysql:mysql-connector-java:8.0.28")

    implementation("com.phodal.chapi:chapi-domain:1.5.1")
}
