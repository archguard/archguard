package org.archguard.trace.storage

import org.archguard.trace.model.TelemetryLogRecord
import org.archguard.trace.model.TelemetryMetricDataPoint

interface TelemetryStorage {
    suspend fun storeLogs(records: List<TelemetryLogRecord>)
    suspend fun storeMetrics(points: List<TelemetryMetricDataPoint>)

    suspend fun listLogs(offset: Int = 0, limit: Int = 100, eventName: String? = null): List<TelemetryLogRecord>
    suspend fun listMetrics(offset: Int = 0, limit: Int = 100, metricName: String? = null): List<TelemetryMetricDataPoint>

    fun logsCount(): Long
    fun metricsCount(): Long

    fun type(): String
}

