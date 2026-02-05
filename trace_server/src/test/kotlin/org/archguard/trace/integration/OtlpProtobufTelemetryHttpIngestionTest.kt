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
import org.archguard.trace.server.installOtlpHttpTelemetryRoutes
import org.archguard.trace.storage.InMemoryTelemetryStorage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OtlpProtobufTelemetryHttpIngestionTest {
    @Test
    fun `should accept OTLP HTTP protobuf logs and store records`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()

        application {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            routing {
                installOtlpHttpTelemetryRoutes(telemetry)
            }
        }

        val reqBytes = buildMinimalLogsExportRequest()
        val resp = client.post("/v1/logs") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        assertEquals(1, telemetry.logsCount())
    }

    @Test
    fun `should accept OTLP HTTP protobuf metrics and store points`() = testApplication {
        val telemetry = InMemoryTelemetryStorage()

        application {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            routing {
                installOtlpHttpTelemetryRoutes(telemetry)
            }
        }

        val reqBytes = buildMinimalMetricsExportRequest()
        val resp = client.post("/v1/metrics") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(reqBytes)
        }

        assertEquals(HttpStatusCode.OK, resp.status)
        // One metric -> one datapoint
        assertEquals(1, telemetry.metricsCount())
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
}

