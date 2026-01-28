package com.thoughtworks.archguard.telemetry.infrastructure

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.telemetry.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

/**
 * In-memory implementation of TelemetryRepository for testing/demo.
 * In production, this should be replaced with proper database persistence.
 */
@Repository
class InMemoryTelemetryRepository : TelemetryRepository {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val json = Json { prettyPrint = true }

    private val spans = mutableListOf<Span>()
    private val metrics = mutableListOf<Metric>()
    private val logs = mutableListOf<LogRecord>()

    override fun saveSpans(spans: List<Span>): Int {
        synchronized(this.spans) {
            this.spans.addAll(spans)
            logger.info("Saved ${spans.size} spans. Total spans: ${this.spans.size}")
            
            // Log sample span for debugging
            if (spans.isNotEmpty()) {
                val sample = spans.first()
                logger.debug("Sample span: ${sample.name} [${sample.context.traceId}/${sample.context.spanId}]")
            }
            
            return spans.size
        }
    }

    override fun saveMetrics(metrics: List<Metric>): Int {
        synchronized(this.metrics) {
            this.metrics.addAll(metrics)
            logger.info("Saved ${metrics.size} metrics. Total metrics: ${this.metrics.size}")
            
            // Log sample metric for debugging
            if (metrics.isNotEmpty()) {
                val sample = metrics.first()
                logger.debug("Sample metric: ${sample.name} (${sample::class.simpleName})")
            }
            
            return metrics.size
        }
    }

    override fun saveLogs(logs: List<LogRecord>): Int {
        synchronized(this.logs) {
            this.logs.addAll(logs)
            logger.info("Saved ${logs.size} logs. Total logs: ${this.logs.size}")
            
            // Log sample log for debugging
            if (logs.isNotEmpty()) {
                val sample = logs.first()
                logger.debug("Sample log: ${sample.severityText} - ${sample.body}")
            }
            
            return logs.size
        }
    }

    // Query methods for testing/debugging
    fun getAllSpans(): List<Span> = synchronized(spans) { spans.toList() }
    fun getAllMetrics(): List<Metric> = synchronized(metrics) { metrics.toList() }
    fun getAllLogs(): List<LogRecord> = synchronized(logs) { logs.toList() }
    
    fun clear() {
        synchronized(spans) { spans.clear() }
        synchronized(metrics) { metrics.clear() }
        synchronized(logs) { logs.clear() }
        logger.info("Cleared all telemetry data")
    }
}
