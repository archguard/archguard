plugins {
    id("application")
    kotlin("jvm") version "1.6.21"
    alias(libs.plugins.shadow)
    alias(libs.plugins.serialization)
}

dependencies {
    api(project(":rule-core"))
    api(project(":scanner_cli"))
    api(project(":rule-linter:rule-code"))
    api(project(":rule-linter:rule-sql"))
    api(project(":rule-linter:rule-test"))
    api(project(":rule-linter:rule-webapi"))

    api("org.jetbrains.kotlin:kotlin-compiler:1.6.21")

    testImplementation(libs.bundles.test)

    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation(libs.clikt)
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.30")
}

application {
    mainClass.set("org.archguard.doc.generator.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "org.archguard.doc.generator.RunnerKt"))
        }
    }
}
