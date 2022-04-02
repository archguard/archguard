plugins {
    antlr
    application

    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.6.10"
}

dependencies {
    antlr("org.antlr:antlr4:4.9.3")
    implementation("org.antlr:antlr4-runtime:4.9.3")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

application {
    mainClass.set("com.thoughtworks.archguard.git.scanner.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.thoughtworks.archguard.git.scanner.RunnerKt"))
        }
    }
}

sourceSets.main {
    java.srcDirs("${project.buildDir}/generated-src")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-package", "dev.evolution") + listOf("-visitor", "-long-messages")
    outputDirectory  = file("${project.buildDir}/generated-src/dev/evolution")
}

tasks.named("compileKotlin") {
    dependsOn(tasks.withType<AntlrTask>())
}

tasks.withType<AntlrTask> {

}
