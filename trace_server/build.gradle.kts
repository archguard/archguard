@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    application
}

application {
    mainClass.set("org.archguard.trace.MainKt")
}

// OpenTelemetry version
val otelVersion = "1.34.1"
val ktorVersion = "2.3.7"

dependencies {
    // ArchGuard dependencies
    api(projects.scannerCore)
    
    // OpenTelemetry Core
    implementation("io.opentelemetry:opentelemetry-api:$otelVersion")
    implementation("io.opentelemetry:opentelemetry-sdk:$otelVersion")
    implementation("io.opentelemetry:opentelemetry-sdk-trace:$otelVersion")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:$otelVersion")
    
    // OpenTelemetry Exporters
    implementation("io.opentelemetry:opentelemetry-exporter-jaeger:$otelVersion")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin:$otelVersion")
    
    // Ktor for HTTP server
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
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.test {
    useJUnitPlatform()
}
