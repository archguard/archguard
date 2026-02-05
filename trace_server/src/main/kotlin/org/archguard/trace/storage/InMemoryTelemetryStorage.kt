package org.archguard.trace.storage

import org.archguard.trace.model.TelemetryLogRecord
import org.archguard.trace.model.TelemetryMetricDataPoint
import java.util.concurrent.ConcurrentLinkedDeque

class InMemoryTelemetryStorage : TelemetryStorage {
    private val logs = ConcurrentLinkedDeque<TelemetryLogRecord>()
    private val metrics = ConcurrentLinkedDeque<TelemetryMetricDataPoint>()

    // keep memory bounded
    private val maxLogs = 50_000
    private val maxMetrics = 50_000

    override suspend fun storeLogs(records: List<TelemetryLogRecord>) {
        if (records.isEmpty()) return
        records.forEach { logs.addFirst(it) }
        trim(logs, maxLogs)
    }

    override suspend fun storeMetrics(points: List<TelemetryMetricDataPoint>) {
        if (points.isEmpty()) return
        points.forEach { metrics.addFirst(it) }
        trim(metrics, maxMetrics)
    }

    override suspend fun listLogs(offset: Int, limit: Int, eventName: String?): List<TelemetryLogRecord> {
        val safeOffset = offset.coerceAtLeast(0)
        val safeLimit = limit.coerceIn(1, 5000)
        val seq = logs.asSequence().let { s ->
            if (eventName.isNullOrBlank()) s else s.filter { it.eventName == eventName }
        }
        return seq.drop(safeOffset).take(safeLimit).toList()
    }

    override suspend fun listMetrics(offset: Int, limit: Int, metricName: String?): List<TelemetryMetricDataPoint> {
        val safeOffset = offset.coerceAtLeast(0)
        val safeLimit = limit.coerceIn(1, 5000)
        val seq = metrics.asSequence().let { s ->
            if (metricName.isNullOrBlank()) s else s.filter { it.metricName == metricName }
        }
        return seq.drop(safeOffset).take(safeLimit).toList()
    }

    override fun logsCount(): Long = logs.size.toLong()
    override fun metricsCount(): Long = metrics.size.toLong()

    override fun type(): String = "in-memory"

    private fun <T> trim(deque: ConcurrentLinkedDeque<T>, max: Int) {
        while (deque.size > max) {
            try {
                deque.removeLast()
            } catch (_: NoSuchElementException) {
                break
            }
        }
    }
}

