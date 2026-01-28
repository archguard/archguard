package com.thoughtworks.archguard.telemetry.domain

import org.archguard.scanner.core.telemetry.*

/**
 * Service layer for telemetry data ingestion and processing.
 */
interface TelemetryService {
    fun ingestTraces(spans: List<Span>): Int
    fun ingestMetrics(metrics: List<Metric>): Int
    fun ingestLogs(logs: List<LogRecord>): Int
}
