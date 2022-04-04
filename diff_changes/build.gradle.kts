plugins {
    id("antlr")
    id("application")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")

    implementation("com.phodal.chapi:chapi-application:1.5.0")
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
