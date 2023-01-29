@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    api(project(":scanner_core"))

    implementation("org.mybatis:mybatis:3.5.9")
    implementation("ognl:ognl:3.3.2") // for mybatis expression
    implementation(libs.jsqlparser)

    testImplementation(libs.bundles.test)
}

application {
    mainClass.set("org.archguard.scanner.core.AnalyserKt")
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
            exclude(dependency("org.jetbrains.kotlinx:.*:.*"))
        }
        minimize()
    }
}
