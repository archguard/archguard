import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.thougthworks.archguard.scanners"

plugins {
    id("antlr")
    id("application")
    id("com.thougthworks.archguard.java-conventions")
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    antlr("org.antlr:antlr4:4.9.3")

    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.3.1.201904271842-r")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.30")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")
    implementation("org.slf4j:slf4j-jdk14:1.7.13")
    implementation("org.antlr:antlr4-runtime:4.9.3")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("com.thoughtworks.archguard.git.scanner.RunnerKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.thoughtworks.archguard.scan_jacoco.RunnerKt"))
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
