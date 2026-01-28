package com.thoughtworks.archguard.telemetry.domain

import com.thoughtworks.archguard.telemetry.infrastructure.TelemetryRepository
import org.archguard.scanner.core.telemetry.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TelemetryServiceImpl(
    private val repository: TelemetryRepository
) : TelemetryService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun ingestTraces(spans: List<Span>): Int {
        if (spans.isEmpty()) return 0

        logger.debug("Processing ${spans.size} spans")
        
        // Validate and enrich spans
        val validSpans = spans.filter { span ->
            span.context.traceId.isNotEmpty() && 
            span.context.spanId.isNotEmpty() &&
            span.name.isNotEmpty()
        }

        if (validSpans.size < spans.size) {
            logger.warn("Filtered out ${spans.size - validSpans.size} invalid spans")
        }

        // Store spans
        return repository.saveSpans(validSpans)
    }

    override fun ingestMetrics(metrics: List<Metric>): Int {
        if (metrics.isEmpty()) return 0

        logger.debug("Processing ${metrics.size} metrics")

        // Validate metrics
        val validMetrics = metrics.filter { metric ->
            metric.name.isNotEmpty()
        }

        if (validMetrics.size < metrics.size) {
            logger.warn("Filtered out ${metrics.size - validMetrics.size} invalid metrics")
        }

        // Store metrics
        return repository.saveMetrics(validMetrics)
    }

    override fun ingestLogs(logs: List<LogRecord>): Int {
        if (logs.isEmpty()) return 0

        logger.debug("Processing ${logs.size} log records")

        // Store logs
        return repository.saveLogs(logs)
    }
}
