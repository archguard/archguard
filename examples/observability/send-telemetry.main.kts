#!/usr/bin/env kotlin

@file:DependsOn("io.ktor:ktor-client-core:2.3.7")
@file:DependsOn("io.ktor:ktor-client-cio:2.3.7")
@file:DependsOn("io.ktor:ktor-client-content-negotiation:2.3.7")
@file:DependsOn("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.*

val ARCHGUARD_BACKEND = System.getenv("ARCHGUARD_BACKEND_URL") ?: "http://localhost:8080"

@Serializable
data class AttributeValue(val stringValue: String? = null)

@Serializable
data class TraceContext(
    val traceId: String,
    val spanId: String,
    val parentSpanId: String? = null,
    val traceFlags: Byte = 1
)

@Serializable
data class SpanStatus(val code: String)

@Serializable
data class Resource(val attributes: Map<String, AttributeValue>)

@Serializable
data class Span(
    val context: TraceContext,
    val name: String,
    val kind: String,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
    val attributes: Map<String, AttributeValue>,
    val status: SpanStatus,
    val resource: Resource
)

@Serializable
data class TraceBatch(val spans: List<Span>)

@Serializable
data class Counter(
    val name: String,
    val description: String = "",
    val unit: String = "1",
    val attributes: Map<String, AttributeValue>,
    val timeUnixNano: Long,
    val value: Long
)

@Serializable
data class MetricBatch(val metrics: List<Counter>)

fun generateTraceId(): String = UUID.randomUUID().toString().replace("-", "")
fun generateSpanId(): String = UUID.randomUUID().toString().replace("-", "").substring(0, 16)
fun currentNano(): Long = System.currentTimeMillis() * 1_000_000

suspend fun sendTelemetry() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    try {
        // Test 1: Send traces
        println("\n=== Sending Traces ===")
        val traceId = generateTraceId()
        val parentSpanId = generateSpanId()
        val startTime = currentNano()
        
        val resource = Resource(
            attributes = mapOf(
                "service.name" to AttributeValue("archguard-example"),
                "service.version" to AttributeValue("1.0.0"),
                "deployment.environment" to AttributeValue("dev")
            )
        )

        val parentSpan = Span(
            context = TraceContext(traceId, parentSpanId),
            name = "process-request",
            kind = "SERVER",
            startTimeUnixNano = startTime,
            endTimeUnixNano = startTime + 150_000_000, // 150ms
            attributes = mapOf(
                "http.method" to AttributeValue("POST"),
                "http.url" to AttributeValue("/api/data"),
                "http.status_code" to AttributeValue("200")
            ),
            status = SpanStatus("OK"),
            resource = resource
        )

        val childSpan = Span(
            context = TraceContext(traceId, generateSpanId(), parentSpanId),
            name = "database-query",
            kind = "CLIENT",
            startTimeUnixNano = startTime + 10_000_000,
            endTimeUnixNano = startTime + 80_000_000, // 70ms
            attributes = mapOf(
                "db.system" to AttributeValue("postgresql"),
                "db.statement" to AttributeValue("SELECT * FROM users WHERE id = ?"),
                "db.name" to AttributeValue("archguard")
            ),
            status = SpanStatus("OK"),
            resource = resource
        )

        val traceResponse = client.post("$ARCHGUARD_BACKEND/api/telemetry/v1/traces") {
            contentType(ContentType.Application.Json)
            setBody(TraceBatch(listOf(parentSpan, childSpan)))
        }
        println("Trace ingestion: ${traceResponse.status}")
        println("Response: ${traceResponse.bodyAsText()}")

        // Test 2: Send metrics
        println("\n=== Sending Metrics ===")
        val metrics = listOf(
            Counter(
                name = "archguard_requests_total",
                description = "Total number of requests",
                unit = "1",
                attributes = mapOf(
                    "service.name" to AttributeValue("archguard-example"),
                    "http.method" to AttributeValue("POST"),
                    "http.status_code" to AttributeValue("200")
                ),
                timeUnixNano = currentNano(),
                value = 42
            ),
            Counter(
                name = "archguard_errors_total",
                description = "Total number of errors",
                unit = "1",
                attributes = mapOf(
                    "service.name" to AttributeValue("archguard-example"),
                    "error.type" to AttributeValue("validation_error")
                ),
                timeUnixNano = currentNano(),
                value = 3
            )
        )

        val metricResponse = client.post("$ARCHGUARD_BACKEND/api/telemetry/v1/metrics") {
            contentType(ContentType.Application.Json)
            setBody(MetricBatch(metrics))
        }
        println("Metric ingestion: ${metricResponse.status}")
        println("Response: ${metricResponse.bodyAsText()}")

        // Test 3: Health check
        println("\n=== Health Check ===")
        val healthResponse = client.get("$ARCHGUARD_BACKEND/api/telemetry/health")
        println("Health status: ${healthResponse.status}")
        println("Response: ${healthResponse.bodyAsText()}")

        println("\n✅ All telemetry data sent successfully!")
        println("\nView the data:")
        println("  - Traces: http://localhost:16686")
        println("  - Metrics: http://localhost:9090")
        println("  - Grafana: http://localhost:3000")

    } finally {
        client.close()
    }
}

runBlocking {
    sendTelemetry()
}
