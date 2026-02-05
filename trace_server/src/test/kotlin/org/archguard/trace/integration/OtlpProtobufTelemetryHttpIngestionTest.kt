package org.archguard.trace.integration

import com.google.protobuf.ByteString
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.archguard.trace.receiver.TelemetryCounters
import org.archguard.trace.server.installOtlpHttpTelemetryRoutes
import org.archguard.trace.storage.InMemoryTelemetryStorage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OtlpProtobufTelemetryHttpIngestionTest {

    private fun createTestApplication(
        telemetry: InMemoryTelemetryStorage,
        counters: TelemetryCounters
    ): ApplicationTestBuilder.() -> Unit = {
        application {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; encodeDefaults = true })
            }
            routing {
                installOtlpHttpTelemetryRoutes(telemetry, counters)
            }
        }
    }

    @Test
    fun `should accept OTLP HTTP protobuf logs and store records`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val reqBytes = buildMinimalLogsExportRequest()
        val resp = client.post("/v1/logs") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        assertEquals(1, telemetry.logsCount())
        assertEquals(1, counters.otlpHttpLogsRequests.get())
    }

    @Test
    fun `should accept OTLP HTTP protobuf metrics and store points`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val reqBytes = buildMinimalMetricsExportRequest()
        val resp = client.post("/v1/metrics") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        // One metric -> one datapoint
        assertEquals(1, telemetry.metricsCount())
        assertEquals(1, counters.otlpHttpMetricsRequests.get())
    }

    @Test
    fun `should accept OTLP HTTP JSON logs and store records`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val jsonBody = """
        {
            "resourceLogs": [{
                "resource": {
                    "attributes": [
                        {"key": "service.name", "value": {"stringValue": "claude-code"}}
                    ]
                },
                "scopeLogs": [{
                    "scope": {"name": "com.anthropic.claude_code", "version": "1.0"},
                    "logRecords": [{
                        "timeUnixNano": "1234567890",
                        "severityText": "INFO",
                        "body": {"stringValue": "test log message"},
                        "attributes": [
                            {"key": "event.name", "value": {"stringValue": "claude_code.user_prompt"}}
                        ]
                    }]
                }]
            }]
        }
        """.trimIndent()

        val resp = client.post("/v1/logs") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        assertEquals(1, telemetry.logsCount())
        assertEquals(1, counters.otlpHttpLogsRequests.get())

        val logs = telemetry.listLogs()
        assertEquals("INFO", logs.first().severityText)
        assertEquals("claude_code.user_prompt", logs.first().eventName)
    }

    @Test
    fun `should accept OTLP HTTP JSON metrics and store points`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val jsonBody = """
        {
            "resourceMetrics": [{
                "resource": {
                    "attributes": [
                        {"key": "service.name", "value": {"stringValue": "claude-code"}}
                    ]
                },
                "scopeMetrics": [{
                    "scope": {"name": "com.anthropic.claude_code", "version": "1.0"},
                    "metrics": [{
                        "name": "claude_code.session.count",
                        "description": "Count of sessions",
                        "unit": "1",
                        "sum": {
                            "dataPoints": [{
                                "timeUnixNano": "1234567890",
                                "asDouble": 42.0,
                                "attributes": []
                            }],
                            "isMonotonic": true,
                            "aggregationTemporality": 2
                        }
                    }]
                }]
            }]
        }
        """.trimIndent()

        val resp = client.post("/v1/metrics") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        assertEquals(1, telemetry.metricsCount())
        assertEquals(1, counters.otlpHttpMetricsRequests.get())

        val metrics = telemetry.listMetrics()
        assertEquals("claude_code.session.count", metrics.first().metricName)
        assertEquals("sum", metrics.first().metricType)
        assertEquals(42.0, metrics.first().valueDouble)
    }

    @Test
    fun `should reject unsupported content type`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val resp = client.post("/v1/logs") {
            contentType(ContentType.Text.Plain)
            setBody("plain text")
        }

        assertEquals(HttpStatusCode.UnsupportedMediaType, resp.status)
        assertEquals(0, telemetry.logsCount())
    }

    @Test
    fun `should handle exponential histogram metrics via protobuf`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val reqBytes = buildExponentialHistogramRequest()
        val resp = client.post("/v1/metrics") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        // Exponential histogram creates multiple points: count, sum, positive buckets, etc.
        assertTrue(telemetry.metricsCount() >= 1)

        val metrics = telemetry.listMetrics()
        assertTrue(metrics.any { it.metricType.startsWith("exponential_histogram") })
    }

    @Test
    fun `should handle summary metrics via protobuf`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()
        val counters = TelemetryCounters()
        createTestApplication(telemetry, counters)()

        val reqBytes = buildSummaryRequest()
        val resp = client.post("/v1/metrics") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        // Summary creates: count, sum, and quantile points
        assertTrue(telemetry.metricsCount() >= 2)

        val metrics = telemetry.listMetrics()
        assertTrue(metrics.any { it.metricType == "summary_count" })
        assertTrue(metrics.any { it.metricType == "summary_sum" })
    }

    private fun buildMinimalLogsExportRequest(): ByteArray {
        val logRecord = io.opentelemetry.proto.logs.v1.LogRecord.newBuilder()
            .setTimeUnixNano(1L)
            .setSeverityText("INFO")
            .setBody(io.opentelemetry.proto.common.v1.AnyValue.newBuilder().setStringValue("hello").build())
            .addAttributes(
                io.opentelemetry.proto.common.v1.KeyValue.newBuilder()
                    .setKey("event.name")
                    .setValue(io.opentelemetry.proto.common.v1.AnyValue.newBuilder().setStringValue("claude_code.user_prompt").build())
                    .build()
            )
            .build()

        val scopeLogs = io.opentelemetry.proto.logs.v1.ScopeLogs.newBuilder()
            .setScope(
                io.opentelemetry.proto.common.v1.InstrumentationScope.newBuilder()
                    .setName("com.anthropic.claude_code")
                    .setVersion("test")
                    .build()
            )
            .addLogRecords(logRecord)
            .build()

        val resourceLogs = io.opentelemetry.proto.logs.v1.ResourceLogs.newBuilder()
            .setResource(io.opentelemetry.proto.resource.v1.Resource.newBuilder().build())
            .addScopeLogs(scopeLogs)
            .build()

        return io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest.newBuilder()
            .addResourceLogs(resourceLogs)
            .build()
            .toByteArray()
    }

    private fun buildMinimalMetricsExportRequest(): ByteArray {
        val dp = io.opentelemetry.proto.metrics.v1.NumberDataPoint.newBuilder()
            .setTimeUnixNano(2L)
            .setAsDouble(1.0)
            .build()

        val sum = io.opentelemetry.proto.metrics.v1.Sum.newBuilder()
            .setIsMonotonic(true)
            .setAggregationTemporality(io.opentelemetry.proto.metrics.v1.AggregationTemporality.AGGREGATION_TEMPORALITY_DELTA)
            .addDataPoints(dp)
            .build()

        val metric = io.opentelemetry.proto.metrics.v1.Metric.newBuilder()
            .setName("claude_code.session.count")
            .setDescription("Count of CLI sessions started")
            .setUnit("1")
            .setSum(sum)
            .build()

        val scopeMetrics = io.opentelemetry.proto.metrics.v1.ScopeMetrics.newBuilder()
            .setScope(
                io.opentelemetry.proto.common.v1.InstrumentationScope.newBuilder()
                    .setName("com.anthropic.claude_code")
                    .setVersion("test")
                    .build()
            )
            .addMetrics(metric)
            .build()

        val resourceMetrics = io.opentelemetry.proto.metrics.v1.ResourceMetrics.newBuilder()
            .setResource(io.opentelemetry.proto.resource.v1.Resource.newBuilder().build())
            .addScopeMetrics(scopeMetrics)
            .build()

        return io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest.newBuilder()
            .addResourceMetrics(resourceMetrics)
            .build()
            .toByteArray()
    }

    private fun buildExponentialHistogramRequest(): ByteArray {
        val dp = io.opentelemetry.proto.metrics.v1.ExponentialHistogramDataPoint.newBuilder()
            .setTimeUnixNano(3L)
            .setStartTimeUnixNano(1L)
            .setCount(100)
            .setSum(500.0)
            .setScale(2)
            .setZeroCount(5)
            .setPositive(
                io.opentelemetry.proto.metrics.v1.ExponentialHistogramDataPoint.Buckets.newBuilder()
                    .setOffset(0)
                    .addAllBucketCounts(listOf(10L, 20L, 30L))
                    .build()
            )
            .setNegative(
                io.opentelemetry.proto.metrics.v1.ExponentialHistogramDataPoint.Buckets.newBuilder()
                    .setOffset(-1)
                    .addAllBucketCounts(listOf(5L, 10L))
                    .build()
            )
            .build()

        val expHist = io.opentelemetry.proto.metrics.v1.ExponentialHistogram.newBuilder()
            .setAggregationTemporality(io.opentelemetry.proto.metrics.v1.AggregationTemporality.AGGREGATION_TEMPORALITY_CUMULATIVE)
            .addDataPoints(dp)
            .build()

        val metric = io.opentelemetry.proto.metrics.v1.Metric.newBuilder()
            .setName("request.latency.exponential")
            .setDescription("Request latency using exponential histogram")
            .setUnit("ms")
            .setExponentialHistogram(expHist)
            .build()

        val scopeMetrics = io.opentelemetry.proto.metrics.v1.ScopeMetrics.newBuilder()
            .setScope(
                io.opentelemetry.proto.common.v1.InstrumentationScope.newBuilder()
                    .setName("test")
                    .build()
            )
            .addMetrics(metric)
            .build()

        val resourceMetrics = io.opentelemetry.proto.metrics.v1.ResourceMetrics.newBuilder()
            .setResource(io.opentelemetry.proto.resource.v1.Resource.newBuilder().build())
            .addScopeMetrics(scopeMetrics)
            .build()

        return io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest.newBuilder()
            .addResourceMetrics(resourceMetrics)
            .build()
            .toByteArray()
    }

    private fun buildSummaryRequest(): ByteArray {
        val qv1 = io.opentelemetry.proto.metrics.v1.SummaryDataPoint.ValueAtQuantile.newBuilder()
            .setQuantile(0.5)
            .setValue(100.0)
            .build()
        val qv2 = io.opentelemetry.proto.metrics.v1.SummaryDataPoint.ValueAtQuantile.newBuilder()
            .setQuantile(0.99)
            .setValue(500.0)
            .build()

        val dp = io.opentelemetry.proto.metrics.v1.SummaryDataPoint.newBuilder()
            .setTimeUnixNano(4L)
            .setStartTimeUnixNano(1L)
            .setCount(1000)
            .setSum(50000.0)
            .addQuantileValues(qv1)
            .addQuantileValues(qv2)
            .build()

        val summary = io.opentelemetry.proto.metrics.v1.Summary.newBuilder()
            .addDataPoints(dp)
            .build()

        val metric = io.opentelemetry.proto.metrics.v1.Metric.newBuilder()
            .setName("request.latency.summary")
            .setDescription("Request latency summary")
            .setUnit("ms")
            .setSummary(summary)
            .build()

        val scopeMetrics = io.opentelemetry.proto.metrics.v1.ScopeMetrics.newBuilder()
            .setScope(
                io.opentelemetry.proto.common.v1.InstrumentationScope.newBuilder()
                    .setName("test")
                    .build()
            )
            .addMetrics(metric)
            .build()

        val resourceMetrics = io.opentelemetry.proto.metrics.v1.ResourceMetrics.newBuilder()
            .setResource(io.opentelemetry.proto.resource.v1.Resource.newBuilder().build())
            .addScopeMetrics(scopeMetrics)
            .build()

        return io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest.newBuilder()
            .addResourceMetrics(resourceMetrics)
            .build()
            .toByteArray()
    }
}

