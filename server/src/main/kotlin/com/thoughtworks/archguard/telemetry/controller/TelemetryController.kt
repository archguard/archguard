package com.thoughtworks.archguard.telemetry.controller

import com.thoughtworks.archguard.telemetry.domain.TelemetryService
import org.archguard.scanner.core.telemetry.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * OTLP-compatible telemetry ingestion endpoint.
 *
 * Supports OpenTelemetry Protocol (OTLP) over HTTP/JSON for:
 * - Traces (distributed tracing)
 * - Metrics (counters, gauges, histograms)
 * - Logs (structured logging)
 *
 * Endpoint patterns follow OTLP spec:
 * - POST /v1/traces
 * - POST /v1/metrics
 * - POST /v1/logs
 */
@RestController
@RequestMapping("/api/telemetry")
class TelemetryController(
    private val telemetryService: TelemetryService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Receive OTLP traces (spans).
     * Compatible with OpenTelemetry SDKs sending to /v1/traces endpoint.
     */
    @PostMapping("/v1/traces")
    fun ingestTraces(@RequestBody batch: TraceBatch): ResponseEntity<IngestResponse> {
        return try {
            val count = telemetryService.ingestTraces(batch.spans)
            logger.info("Ingested ${count} traces")
            ResponseEntity.ok(IngestResponse(success = true, itemsIngested = count))
        } catch (e: Exception) {
            logger.error("Failed to ingest traces", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(IngestResponse(success = false, error = e.message))
        }
    }

    /**
     * Receive OTLP metrics.
     * Compatible with OpenTelemetry SDKs sending to /v1/metrics endpoint.
     */
    @PostMapping("/v1/metrics")
    fun ingestMetrics(@RequestBody batch: MetricBatch): ResponseEntity<IngestResponse> {
        return try {
            val count = telemetryService.ingestMetrics(batch.metrics)
            logger.info("Ingested ${count} metrics")
            ResponseEntity.ok(IngestResponse(success = true, itemsIngested = count))
        } catch (e: Exception) {
            logger.error("Failed to ingest metrics", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(IngestResponse(success = false, error = e.message))
        }
    }

    /**
     * Receive OTLP logs.
     * Compatible with OpenTelemetry SDKs sending to /v1/logs endpoint.
     */
    @PostMapping("/v1/logs")
    fun ingestLogs(@RequestBody batch: LogBatch): ResponseEntity<IngestResponse> {
        return try {
            val count = telemetryService.ingestLogs(batch.logs)
            logger.info("Ingested ${count} logs")
            ResponseEntity.ok(IngestResponse(success = true, itemsIngested = count))
        } catch (e: Exception) {
            logger.error("Failed to ingest logs", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(IngestResponse(success = false, error = e.message))
        }
    }

    /**
     * Health check endpoint for telemetry service.
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "status" to "UP",
                "service" to "archguard-telemetry",
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}

// Request DTOs
data class TraceBatch(val spans: List<Span>)
data class MetricBatch(val metrics: List<Metric>)
data class LogBatch(val logs: List<LogRecord>)

// Response DTO
data class IngestResponse(
    val success: Boolean,
    val itemsIngested: Int = 0,
    val error: String? = null
)
