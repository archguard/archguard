group = "com.thoughtworks.archguard.scanner"

plugins {
    id("application")
    id("com.thougthworks.archguard.java-conventions")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":common"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation(kotlin("stdlib"))

    implementation("com.phodal.chapi:chapi-domain:0.2.0")
    implementation("com.phodal.chapi:chapi-ast-typescript:0.2.0")
    implementation("com.phodal.chapi:chapi-application:0.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

application {
    mainClass.set("com.thoughtworks.archguard.scanner.httpapi.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.thoughtworks.archguard.scanner.javasource.RunnerKt"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


