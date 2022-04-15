plugins {
    id("antlr")
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    api(project(":common_code_repository"))
    implementation("org.jdbi:jdbi3-core:3.8.2")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r")

    implementation("com.phodal.chapi:chapi-application:1.5.6")
    implementation("com.phodal.chapi:chapi-domain:1.5.6")
}

application {
    mainClass.set("org.archguard.diff.changes.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.diff.changes.RunnerKt"))
        }
    }
}
