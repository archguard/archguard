@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    application
    alias(libs.plugins.shadow)
}

application {
    mainClass.set("org.archguard.mcp.MainKt")
}

// Use Ktor 2.3.x which is compatible with Kotlin 1.8
val ktorVersion = "2.3.7"

dependencies {
    // ArchGuard dependencies
    api(projects.ruleCore)
    api(projects.ruleLinter.ruleCode)
    api(projects.ruleLinter.ruleWebapi)
    api(projects.ruleLinter.ruleSql)
    api(projects.ruleLinter.ruleTest)
    api(projects.ruleLinter.ruleLayer)
    api(projects.ruleLinter.ruleComment)
    api(projects.ruleLinter.ruleProtobuf)
    api(projects.scannerCore)
    api(projects.architectureAsCode.model)

    // Ktor for HTTP server (compatible with Kotlin 1.8)
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Kotlin coroutines
    implementation(libs.coroutines.core)

    // JSON serialization
    implementation(libs.serialization.json)

    // Logging
    implementation(libs.logback.classic)

    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation(libs.bundles.test)
}

tasks {
    shadowJar {
        archiveBaseName.set("archguard-mcp-server")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = "org.archguard.mcp.MainKt"
        }
    }
}
