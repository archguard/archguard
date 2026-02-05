package org.archguard.trace.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceResponse
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import org.archguard.trace.receiver.TelemetryCounters
import org.archguard.trace.receiver.toTelemetryLogRecords
import org.archguard.trace.receiver.toTelemetryMetricPoints
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

/**
 * OTEL-compliant export response for JSON format
 */
@Serializable
data class OtlpJsonExportResponse(
    val partialSuccess: OtlpPartialSuccess? = null
)

@Serializable
data class OtlpPartialSuccess(
    val rejectedDataPoints: Long = 0,
    val rejectedLogRecords: Long = 0,
    val errorMessage: String? = null
)

/**
 * JSON models for OTLP metrics/logs (simplified for compatibility)
 */
@Serializable
data class OtlpJsonMetricsRequest(
    val resourceMetrics: List<JsonResourceMetrics> = emptyList()
)

@Serializable
data class JsonResourceMetrics(
    val resource: JsonResource? = null,
    val scopeMetrics: List<JsonScopeMetrics> = emptyList()
)

@Serializable
data class JsonResource(
    val attributes: List<JsonKeyValue> = emptyList()
)

@Serializable
data class JsonScopeMetrics(
    val scope: JsonScope? = null,
    val metrics: List<JsonMetric> = emptyList()
)

@Serializable
data class JsonScope(
    val name: String? = null,
    val version: String? = null
)

@Serializable
data class JsonMetric(
    val name: String,
    val description: String? = null,
    val unit: String? = null,
    val gauge: JsonGauge? = null,
    val sum: JsonSum? = null,
    val histogram: JsonHistogram? = null,
    val exponentialHistogram: JsonExponentialHistogram? = null,
    val summary: JsonSummary? = null
)

@Serializable
data class JsonGauge(
    val dataPoints: List<JsonNumberDataPoint> = emptyList()
)

@Serializable
data class JsonSum(
    val dataPoints: List<JsonNumberDataPoint> = emptyList(),
    val isMonotonic: Boolean = false,
    val aggregationTemporality: Int = 0
)

@Serializable
data class JsonHistogram(
    val dataPoints: List<JsonHistogramDataPoint> = emptyList(),
    val aggregationTemporality: Int = 0
)

@Serializable
data class JsonHistogramDataPoint(
    val attributes: List<JsonKeyValue> = emptyList(),
    val startTimeUnixNano: String? = null,
    val timeUnixNano: String? = null,
    val count: String? = null,
    val sum: Double? = null,
    val bucketCounts: List<String> = emptyList(),
    val explicitBounds: List<Double> = emptyList()
)

@Serializable
data class JsonExponentialHistogram(
    val dataPoints: List<JsonExponentialHistogramDataPoint> = emptyList(),
    val aggregationTemporality: Int = 0
)

@Serializable
data class JsonExponentialHistogramDataPoint(
    val attributes: List<JsonKeyValue> = emptyList(),
    val startTimeUnixNano: String? = null,
    val timeUnixNano: String? = null,
    val count: String? = null,
    val sum: Double? = null,
    val scale: Int = 0,
    val zeroCount: String? = null,
    val positive: JsonBuckets? = null,
    val negative: JsonBuckets? = null
)

@Serializable
data class JsonBuckets(
    val offset: Int = 0,
    val bucketCounts: List<String> = emptyList()
)

@Serializable
data class JsonSummary(
    val dataPoints: List<JsonSummaryDataPoint> = emptyList()
)

@Serializable
data class JsonSummaryDataPoint(
    val attributes: List<JsonKeyValue> = emptyList(),
    val startTimeUnixNano: String? = null,
    val timeUnixNano: String? = null,
    val count: String? = null,
    val sum: Double? = null,
    val quantileValues: List<JsonQuantileValue> = emptyList()
)

@Serializable
data class JsonQuantileValue(
    val quantile: Double = 0.0,
    val value: Double = 0.0
)

@Serializable
data class JsonNumberDataPoint(
    val attributes: List<JsonKeyValue> = emptyList(),
    val startTimeUnixNano: String? = null,
    val timeUnixNano: String? = null,
    val asDouble: Double? = null,
    val asInt: String? = null
)

@Serializable
data class JsonKeyValue(
    val key: String,
    val value: JsonAnyValue? = null
)

@Serializable
data class JsonAnyValue(
    val stringValue: String? = null,
    val boolValue: Boolean? = null,
    val intValue: String? = null,
    val doubleValue: Double? = null,
    val arrayValue: JsonArrayValue? = null,
    val kvlistValue: JsonKvList? = null
)

@Serializable
data class JsonArrayValue(
    val values: List<JsonAnyValue> = emptyList()
)

@Serializable
data class JsonKvList(
    val values: List<JsonKeyValue> = emptyList()
)

// JSON models for logs
@Serializable
data class OtlpJsonLogsRequest(
    val resourceLogs: List<JsonResourceLogs> = emptyList()
)

@Serializable
data class JsonResourceLogs(
    val resource: JsonResource? = null,
    val scopeLogs: List<JsonScopeLogs> = emptyList()
)

@Serializable
data class JsonScopeLogs(
    val scope: JsonScope? = null,
    val logRecords: List<JsonLogRecord> = emptyList()
)

@Serializable
data class JsonLogRecord(
    val timeUnixNano: String? = null,
    val observedTimeUnixNano: String? = null,
    val severityNumber: Int? = null,
    val severityText: String? = null,
    val body: JsonAnyValue? = null,
    val attributes: List<JsonKeyValue> = emptyList(),
    val traceId: String? = null,
    val spanId: String? = null
)

/**
 * OTLP/HTTP telemetry endpoints for metrics and logs.
 *
 * Supports both JSON and Protobuf formats per OTEL spec.
 * Claude Code docs default to OTLP/gRPC (4317). These endpoints exist for completeness
 * and to support `OTEL_EXPORTER_OTLP_*_ENDPOINT` configurations using HTTP.
 */
fun Routing.installOtlpHttpTelemetryRoutes(
    telemetryStorage: TelemetryStorage,
    counters: TelemetryCounters
) {
    post("/v1/metrics") {
        counters.otlpHttpMetricsRequests.incrementAndGet()
        val ct = call.request.contentType()
        
        try {
            when {
                ct.match(ContentType.Application.Json) -> {
                    // Handle JSON format
                    val request = call.receive<OtlpJsonMetricsRequest>()
                    val points = request.toTelemetryMetricPoints()
                    telemetryStorage.storeMetrics(points)
                    logger.info { "OTLP/HTTP JSON metrics: ${points.size} data points stored" }
                    call.respond(HttpStatusCode.OK, OtlpJsonExportResponse())
                }
                ct.toString().contains("application/x-protobuf") ||
                ct.toString().contains("application/octet-stream") ||
                ct.match(ContentType.Application.ProtoBuf) -> {
                    // Handle Protobuf format
                    val bytes = call.receive<ByteArray>()
                    val req = ExportMetricsServiceRequest.parseFrom(bytes)
                    val points = req.toTelemetryMetricPoints()
                    telemetryStorage.storeMetrics(points)
                    logger.info { "OTLP/HTTP protobuf metrics: ${points.size} data points stored" }
                    
                    val respBytes = ExportMetricsServiceResponse.newBuilder().build().toByteArray()
                    call.respondBytes(respBytes, ContentType.parse("application/x-protobuf"))
                }
                else -> {
                    call.respond(
                        HttpStatusCode.UnsupportedMediaType,
                        OtlpJsonExportResponse(
                            partialSuccess = OtlpPartialSuccess(
                                errorMessage = "Unsupported content type: $ct. Use application/json or application/x-protobuf"
                            )
                        )
                    )
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to process OTLP metrics: ${e.message}" }
            call.respond(
                HttpStatusCode.InternalServerError,
                OtlpJsonExportResponse(
                    partialSuccess = OtlpPartialSuccess(errorMessage = e.message)
                )
            )
        }
    }

    post("/v1/logs") {
        counters.otlpHttpLogsRequests.incrementAndGet()
        val ct = call.request.contentType()
        
        try {
            when {
                ct.match(ContentType.Application.Json) -> {
                    // Handle JSON format
                    val request = call.receive<OtlpJsonLogsRequest>()
                    val records = request.toTelemetryLogRecords()
                    telemetryStorage.storeLogs(records)
                    logger.info { "OTLP/HTTP JSON logs: ${records.size} records stored" }
                    call.respond(HttpStatusCode.OK, OtlpJsonExportResponse())
                }
                ct.toString().contains("application/x-protobuf") ||
                ct.toString().contains("application/octet-stream") ||
                ct.match(ContentType.Application.ProtoBuf) -> {
                    // Handle Protobuf format
                    val bytes = call.receive<ByteArray>()
                    val req = ExportLogsServiceRequest.parseFrom(bytes)
                    val records = req.toTelemetryLogRecords()
                    telemetryStorage.storeLogs(records)
                    logger.info { "OTLP/HTTP protobuf logs: ${records.size} records stored" }
                    
                    val respBytes = ExportLogsServiceResponse.newBuilder().build().toByteArray()
                    call.respondBytes(respBytes, ContentType.parse("application/x-protobuf"))
                }
                else -> {
                    call.respond(
                        HttpStatusCode.UnsupportedMediaType,
                        OtlpJsonExportResponse(
                            partialSuccess = OtlpPartialSuccess(
                                errorMessage = "Unsupported content type: $ct. Use application/json or application/x-protobuf"
                            )
                        )
                    )
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to process OTLP logs: ${e.message}" }
            call.respond(
                HttpStatusCode.InternalServerError,
                OtlpJsonExportResponse(
                    partialSuccess = OtlpPartialSuccess(errorMessage = e.message)
                )
            )
        }
    }
}

