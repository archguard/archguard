package org.archguard.trace.storage

import org.archguard.trace.model.TelemetryLogRecord
import org.archguard.trace.model.TelemetryMetricDataPoint
import java.util.ArrayDeque

class InMemoryTelemetryStorage : TelemetryStorage {
    private val lock = Any()
    private val logs = ArrayDeque<TelemetryLogRecord>()
    private val metrics = ArrayDeque<TelemetryMetricDataPoint>()

    // keep memory bounded
    private val maxLogs = 50_000
    private val maxMetrics = 50_000

    override suspend fun storeLogs(records: List<TelemetryLogRecord>) {
        if (records.isEmpty()) return
        synchronized(lock) {
            records.forEach { logs.addFirst(it) }
            trim(logs, maxLogs)
        }
    }

    override suspend fun storeMetrics(points: List<TelemetryMetricDataPoint>) {
        if (points.isEmpty()) return
        synchronized(lock) {
            points.forEach { metrics.addFirst(it) }
            trim(metrics, maxMetrics)
        }
    }

    override suspend fun listLogs(offset: Int, limit: Int, eventName: String?): List<TelemetryLogRecord> {
        val safeOffset = offset.coerceAtLeast(0)
        val safeLimit = limit.coerceIn(1, 5000)
        synchronized(lock) {
            val seq = logs.asSequence().let { s ->
                if (eventName.isNullOrBlank()) s else s.filter { it.eventName == eventName }
            }
            return seq.drop(safeOffset).take(safeLimit).toList()
        }
    }

    override suspend fun listMetrics(offset: Int, limit: Int, metricName: String?): List<TelemetryMetricDataPoint> {
        val safeOffset = offset.coerceAtLeast(0)
        val safeLimit = limit.coerceIn(1, 5000)
        synchronized(lock) {
            val seq = metrics.asSequence().let { s ->
                if (metricName.isNullOrBlank()) s else s.filter { it.metricName == metricName }
            }
            return seq.drop(safeOffset).take(safeLimit).toList()
        }
    }

    override fun logsCount(): Long = synchronized(lock) { logs.size.toLong() }
    override fun metricsCount(): Long = synchronized(lock) { metrics.size.toLong() }

    override fun type(): String = "in-memory"

    private fun <T> trim(deque: ArrayDeque<T>, max: Int) {
        while (deque.size > max) {
            deque.pollLast() ?: break
        }
    }
}

