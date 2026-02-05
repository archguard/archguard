package org.archguard.trace.receiver

import org.archguard.trace.model.TelemetryLogRecord
import org.archguard.trace.model.TelemetryMetricDataPoint
import org.archguard.trace.server.*
import java.util.UUID

/**
 * Convert JSON OTLP metrics request to internal TelemetryMetricDataPoint list
 */
fun OtlpJsonMetricsRequest.toTelemetryMetricPoints(): List<TelemetryMetricDataPoint> {
    val out = ArrayList<TelemetryMetricDataPoint>()

    for (rm in resourceMetrics) {
        val resourceAttrs = rm.resource?.attributes?.toStringMap() ?: emptyMap()

        for (sm in rm.scopeMetrics) {
            val scopeName = sm.scope?.name?.takeIf { it.isNotBlank() }
            val scopeVersion = sm.scope?.version?.takeIf { it.isNotBlank() }

            for (metric in sm.metrics) {
                out.addAll(metric.flatten(resourceAttrs, scopeName, scopeVersion))
            }
        }
    }

    return out
}

private fun JsonMetric.flatten(
    resourceAttrs: Map<String, String>,
    scopeName: String?,
    scopeVersion: String?
): List<TelemetryMetricDataPoint> {
    val metricName = this.name
    val desc = this.description?.takeIf { it.isNotBlank() }
    val unit = this.unit?.takeIf { it.isNotBlank() }

    val points = ArrayList<TelemetryMetricDataPoint>()

    // Handle Gauge
    gauge?.dataPoints?.forEach { dp ->
        points.add(
            dp.toPoint(
                metricName = metricName,
                description = desc,
                unit = unit,
                metricType = "gauge",
                resourceAttrs = resourceAttrs,
                scopeName = scopeName,
                scopeVersion = scopeVersion,
                isMonotonic = null,
                aggregationTemporality = null
            )
        )
    }

    // Handle Sum
    sum?.let { s ->
        val mono = s.isMonotonic
        val temporality = s.aggregationTemporality.toTemporalityString()
        s.dataPoints.forEach { dp ->
            points.add(
                dp.toPoint(
                    metricName = metricName,
                    description = desc,
                    unit = unit,
                    metricType = "sum",
                    resourceAttrs = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion,
                    isMonotonic = mono,
                    aggregationTemporality = temporality
                )
            )
        }
    }

    // Handle Histogram
    histogram?.let { h ->
        val temporality = h.aggregationTemporality.toTemporalityString()
        h.dataPoints.forEach { dp ->
            points.add(
                TelemetryMetricDataPoint(
                    id = UUID.randomUUID().toString(),
                    metricName = metricName,
                    description = desc,
                    unit = unit,
                    metricType = "histogram_count",
                    startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                    timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                    valueLong = dp.count?.toLongOrNull(),
                    attributes = dp.attributes.toStringMap(),
                    resourceAttributes = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion,
                    aggregationTemporality = temporality
                )
            )
            if (dp.sum != null) {
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "histogram_sum",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        valueDouble = dp.sum,
                        attributes = dp.attributes.toStringMap(),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
            }
            // Store bucket information
            if (dp.bucketCounts.isNotEmpty()) {
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "histogram_buckets",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        attributes = dp.attributes.toStringMap() + mapOf(
                            "_bucket_counts" to dp.bucketCounts.joinToString(","),
                            "_explicit_bounds" to dp.explicitBounds.joinToString(",")
                        ),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
            }
        }
    }

    // Handle Exponential Histogram
    exponentialHistogram?.let { eh ->
        val temporality = eh.aggregationTemporality.toTemporalityString()
        eh.dataPoints.forEach { dp ->
            points.add(
                TelemetryMetricDataPoint(
                    id = UUID.randomUUID().toString(),
                    metricName = metricName,
                    description = desc,
                    unit = unit,
                    metricType = "exponential_histogram_count",
                    startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                    timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                    valueLong = dp.count?.toLongOrNull(),
                    attributes = dp.attributes.toStringMap() + mapOf(
                        "_scale" to dp.scale.toString(),
                        "_zero_count" to (dp.zeroCount ?: "0")
                    ),
                    resourceAttributes = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion,
                    aggregationTemporality = temporality
                )
            )
            if (dp.sum != null) {
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "exponential_histogram_sum",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        valueDouble = dp.sum,
                        attributes = dp.attributes.toStringMap(),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
            }
            // Store bucket information for positive/negative
            dp.positive?.let { pos ->
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "exponential_histogram_positive",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        attributes = dp.attributes.toStringMap() + mapOf(
                            "_bucket_offset" to pos.offset.toString(),
                            "_bucket_counts" to pos.bucketCounts.joinToString(",")
                        ),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
            }
            dp.negative?.let { neg ->
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "exponential_histogram_negative",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        attributes = dp.attributes.toStringMap() + mapOf(
                            "_bucket_offset" to neg.offset.toString(),
                            "_bucket_counts" to neg.bucketCounts.joinToString(",")
                        ),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
            }
        }
    }

    // Handle Summary
    summary?.let { s ->
        s.dataPoints.forEach { dp ->
            points.add(
                TelemetryMetricDataPoint(
                    id = UUID.randomUUID().toString(),
                    metricName = metricName,
                    description = desc,
                    unit = unit,
                    metricType = "summary_count",
                    startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                    timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                    valueLong = dp.count?.toLongOrNull(),
                    attributes = dp.attributes.toStringMap(),
                    resourceAttributes = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion
                )
            )
            if (dp.sum != null) {
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "summary_sum",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        valueDouble = dp.sum,
                        attributes = dp.attributes.toStringMap(),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion
                    )
                )
            }
            // Store quantile values
            dp.quantileValues.forEach { qv ->
                points.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = metricName,
                        description = desc,
                        unit = unit,
                        metricType = "summary_quantile",
                        startTimeUnixNano = dp.startTimeUnixNano?.toLongOrNull(),
                        timeUnixNano = dp.timeUnixNano?.toLongOrNull(),
                        valueDouble = qv.value,
                        attributes = dp.attributes.toStringMap() + mapOf(
                            "_quantile" to qv.quantile.toString()
                        ),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion
                    )
                )
            }
        }
    }

    return points
}

private fun JsonNumberDataPoint.toPoint(
    metricName: String,
    description: String?,
    unit: String?,
    metricType: String,
    resourceAttrs: Map<String, String>,
    scopeName: String?,
    scopeVersion: String?,
    isMonotonic: Boolean?,
    aggregationTemporality: String?
): TelemetryMetricDataPoint {
    return TelemetryMetricDataPoint(
        id = UUID.randomUUID().toString(),
        metricName = metricName,
        description = description,
        unit = unit,
        metricType = metricType,
        startTimeUnixNano = startTimeUnixNano?.toLongOrNull(),
        timeUnixNano = timeUnixNano?.toLongOrNull(),
        valueDouble = asDouble,
        valueLong = asInt?.toLongOrNull(),
        attributes = attributes.toStringMap(),
        resourceAttributes = resourceAttrs,
        scopeName = scopeName,
        scopeVersion = scopeVersion,
        isMonotonic = isMonotonic,
        aggregationTemporality = aggregationTemporality
    )
}

private fun Int.toTemporalityString(): String = when (this) {
    1 -> "DELTA"
    2 -> "CUMULATIVE"
    else -> "UNSPECIFIED"
}

private fun List<JsonKeyValue>.toStringMap(): Map<String, String> =
    this.associate { kv -> kv.key to (kv.value?.toStringValue() ?: "") }

private fun JsonAnyValue.toStringValue(): String = when {
    stringValue != null -> stringValue
    boolValue != null -> boolValue.toString()
    intValue != null -> intValue
    doubleValue != null -> doubleValue.toString()
    arrayValue != null -> arrayValue.values.joinToString(prefix = "[", postfix = "]") { it.toStringValue() }
    kvlistValue != null -> kvlistValue.values.joinToString(prefix = "{", postfix = "}") { "${it.key}:${it.value?.toStringValue() ?: ""}" }
    else -> ""
}

/**
 * Convert JSON OTLP logs request to internal TelemetryLogRecord list
 */
fun OtlpJsonLogsRequest.toTelemetryLogRecords(): List<TelemetryLogRecord> {
    val out = ArrayList<TelemetryLogRecord>()

    for (rl in resourceLogs) {
        val resourceAttrs = rl.resource?.attributes?.toStringMap() ?: emptyMap()

        for (sl in rl.scopeLogs) {
            val scopeName = sl.scope?.name?.takeIf { it.isNotBlank() }
            val scopeVersion = sl.scope?.version?.takeIf { it.isNotBlank() }

            for (lr in sl.logRecords) {
                val attrs = lr.attributes.toStringMap()
                // OTEL events have "event.name" attribute
                val eventName = attrs["event.name"]

                out.add(
                    TelemetryLogRecord(
                        id = UUID.randomUUID().toString(),
                        timeUnixNano = lr.timeUnixNano?.toLongOrNull(),
                        observedTimeUnixNano = lr.observedTimeUnixNano?.toLongOrNull(),
                        severityText = lr.severityText,
                        severityNumber = lr.severityNumber,
                        body = lr.body?.toStringValue(),
                        attributes = attrs,
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        eventName = eventName
                    )
                )
            }
        }
    }

    return out
}
