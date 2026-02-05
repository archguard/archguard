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
import mu.KotlinLogging
import org.archguard.trace.receiver.toTelemetryLogRecords
import org.archguard.trace.receiver.toTelemetryMetricPoints
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

/**
 * OTLP/HTTP telemetry endpoints for metrics and logs.
 *
 * Claude Code docs default to OTLP/gRPC (4317). These endpoints exist for completeness
 * and to support `OTEL_EXPORTER_OTLP_*_ENDPOINT` configurations using HTTP/protobuf.
 */
fun Routing.installOtlpHttpTelemetryRoutes(telemetryStorage: TelemetryStorage) {
    post("/v1/metrics") {
        val ct = call.request.contentType().toString()
        if (!ct.contains("application/x-protobuf") && !ct.contains("application/octet-stream") && !ct.contains("application/protobuf")) {
            call.respond(
                HttpStatusCode.UnsupportedMediaType,
                mapOf("error" to "OTLP/HTTP metrics supports protobuf only. Send Content-Type: application/x-protobuf")
            )
            return@post
        }

        val bytes = call.receive<ByteArray>()
        val req = ExportMetricsServiceRequest.parseFrom(bytes)
        val points = req.toTelemetryMetricPoints()
        telemetryStorage.storeMetrics(points)

        // Spec-ish response: protobuf empty response.
        val respBytes = ExportMetricsServiceResponse.newBuilder().build().toByteArray()
        call.respondBytes(respBytes, ContentType.parse("application/x-protobuf"))
    }

    post("/v1/logs") {
        val ct = call.request.contentType().toString()
        if (!ct.contains("application/x-protobuf") && !ct.contains("application/octet-stream") && !ct.contains("application/protobuf")) {
            call.respond(
                HttpStatusCode.UnsupportedMediaType,
                mapOf("error" to "OTLP/HTTP logs supports protobuf only. Send Content-Type: application/x-protobuf")
            )
            return@post
        }

        val bytes = call.receive<ByteArray>()
        val req = ExportLogsServiceRequest.parseFrom(bytes)
        val records = req.toTelemetryLogRecords()
        telemetryStorage.storeLogs(records)

        val respBytes = ExportLogsServiceResponse.newBuilder().build().toByteArray()
        call.respondBytes(respBytes, ContentType.parse("application/x-protobuf"))
    }
}

