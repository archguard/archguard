package com.thoughtworks.archguard.telemetry.infrastructure

import org.archguard.scanner.core.telemetry.*

/**
 * Repository interface for telemetry data persistence.
 */
interface TelemetryRepository {
    fun saveSpans(spans: List<Span>): Int
    fun saveMetrics(metrics: List<Metric>): Int
    fun saveLogs(logs: List<LogRecord>): Int
}
