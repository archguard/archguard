@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("application")
    alias(libs.plugins.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.serialization)
}

dependencies {
    api(projects.ruleCore)
    api(projects.scannerCli)
    api(projects.ruleLinter.ruleCode)
    api(projects.ruleLinter.ruleSql)
    api(projects.ruleLinter.ruleTest)
    api(projects.ruleLinter.ruleWebapi)
    api(projects.ruleLinter.ruleComment)

    api("org.jetbrains.kotlin:kotlin-compiler:1.6.21")

    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation(libs.clikt)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.bundles.test)
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
