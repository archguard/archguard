group = "org.archguard.scanner"

plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    // for test chapi in Local
    mavenLocal()
}

dependencies {
    api(project(":common_code_repository"))
    api(project(":scan_mysql"))

    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("io.netty:netty-all:4.1.42.Final")

    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // todo: split mybatis to a plugins
    implementation("org.mybatis:mybatis:3.5.9")
    implementation("ognl:ognl:3.3.2") // for mybatis expression

    implementation("org.jdbi:jdbi3-core:3.8.2")

    implementation("com.phodal.chapi:chapi-application:1.5.6")
    implementation("com.phodal.chapi:chapi-domain:1.5.6")
}

application {
    mainClass.set("org.archguard.scanner.sourcecode.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.scanner.sourcecode.RunnerKt"))
        }
    }
}

