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
val grpcVersion = "1.63.0"

dependencies {
    // gRPC (for OTLP gRPC receiver)
    implementation(platform("io.grpc:grpc-bom:$grpcVersion"))
    implementation("io.grpc:grpc-netty-shaded")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")

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
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    
    // Protocol Buffers
    implementation("io.opentelemetry.proto:opentelemetry-proto:1.0.0-alpha")
    
    // Database - Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.45.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.45.0")
    implementation("org.jetbrains.exposed:exposed-json:0.45.0")
    
    // Database drivers
    implementation("com.h2database:h2:2.2.224")              // H2 for testing
    implementation("org.postgresql:postgresql:42.7.1")        // PostgreSQL for production
    implementation("com.zaxxer:HikariCP:5.1.0")              // Connection pool
    
    // Kotlin coroutines
    implementation(libs.coroutines.core)
    
    // JSON serialization
    implementation(libs.serialization.json)
    
    // Logging
    implementation(libs.logback.classic)
    
    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation(libs.bundles.test)
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.test {
    useJUnitPlatform()
}
