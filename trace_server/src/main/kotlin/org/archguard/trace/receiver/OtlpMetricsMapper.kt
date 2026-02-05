package org.archguard.trace.receiver

import com.google.protobuf.ByteString
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.common.v1.KeyValue
import io.opentelemetry.proto.metrics.v1.AggregationTemporality
import io.opentelemetry.proto.metrics.v1.Metric
import io.opentelemetry.proto.metrics.v1.NumberDataPoint
import org.archguard.trace.model.TelemetryMetricDataPoint
import java.util.UUID

fun ExportMetricsServiceRequest.toTelemetryMetricPoints(): List<TelemetryMetricDataPoint> {
    val out = ArrayList<TelemetryMetricDataPoint>(256)

    for (rm in this.resourceMetricsList) {
        val resourceAttrs = rm.resource.attributesList.toStringMap()
        for (sm in rm.scopeMetricsList) {
            val scopeName = sm.scope.name.takeIf { it.isNotBlank() }
            val scopeVersion = sm.scope.version.takeIf { it.isNotBlank() }

            for (metric in sm.metricsList) {
                out.addAll(metric.flatten(resourceAttrs, scopeName, scopeVersion))
            }
        }
    }

    return out
}

private fun Metric.flatten(
    resourceAttrs: Map<String, String>,
    scopeName: String?,
    scopeVersion: String?
): List<TelemetryMetricDataPoint> {
    val name = this.name
    val desc = this.description.takeIf { it.isNotBlank() }
    val unit = this.unit.takeIf { it.isNotBlank() }

    return when (this.dataCase) {
        Metric.DataCase.GAUGE -> {
            this.gauge.dataPointsList.map { dp ->
                dp.toPoint(
                    metricName = name,
                    description = desc,
                    unit = unit,
                    metricType = "gauge",
                    resourceAttrs = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion,
                    isMonotonic = null,
                    aggregationTemporality = null
                )
            }
        }
        Metric.DataCase.SUM -> {
            val mono = this.sum.isMonotonic
            val temporality = this.sum.aggregationTemporality.toStringValue()
            this.sum.dataPointsList.map { dp ->
                dp.toPoint(
                    metricName = name,
                    description = desc,
                    unit = unit,
                    metricType = "sum",
                    resourceAttrs = resourceAttrs,
                    scopeName = scopeName,
                    scopeVersion = scopeVersion,
                    isMonotonic = mono,
                    aggregationTemporality = temporality
                )
            }
        }
        Metric.DataCase.HISTOGRAM -> {
            // Best-effort: store count and sum (if present) as separate points
            val pts = ArrayList<TelemetryMetricDataPoint>()
            for (dp in this.histogram.dataPointsList) {
                pts.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = name,
                        description = desc,
                        unit = unit,
                        metricType = "histogram_count",
                        startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                        timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                        valueLong = dp.count,
                        attributes = dp.attributesList.toStringMap(),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion
                    )
                )
                if (dp.hasSum()) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "histogram_sum",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            valueDouble = dp.sum,
                            attributes = dp.attributesList.toStringMap(),
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion
                        )
                    )
                }
            }
            pts
        }
        Metric.DataCase.EXPONENTIAL_HISTOGRAM,
        Metric.DataCase.SUMMARY,
        Metric.DataCase.DATA_NOT_SET,
        null -> emptyList()
    }
}

private fun NumberDataPoint.toPoint(
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
    val (vDouble, vLong) = when (this.valueCase) {
        NumberDataPoint.ValueCase.AS_DOUBLE -> this.asDouble to null
        NumberDataPoint.ValueCase.AS_INT -> null to this.asInt
        NumberDataPoint.ValueCase.VALUE_NOT_SET,
        null -> null to null
    }

    return TelemetryMetricDataPoint(
        id = UUID.randomUUID().toString(),
        metricName = metricName,
        description = description,
        unit = unit,
        metricType = metricType,
        startTimeUnixNano = this.startTimeUnixNano.takeIf { it != 0L },
        timeUnixNano = this.timeUnixNano.takeIf { it != 0L },
        valueDouble = vDouble,
        valueLong = vLong,
        attributes = this.attributesList.toStringMap(),
        resourceAttributes = resourceAttrs,
        scopeName = scopeName,
        scopeVersion = scopeVersion,
        isMonotonic = isMonotonic,
        aggregationTemporality = aggregationTemporality
    )
}

private fun AggregationTemporality.toStringValue(): String = when (this) {
    AggregationTemporality.AGGREGATION_TEMPORALITY_DELTA -> "DELTA"
    AggregationTemporality.AGGREGATION_TEMPORALITY_CUMULATIVE -> "CUMULATIVE"
    AggregationTemporality.AGGREGATION_TEMPORALITY_UNSPECIFIED,
    AggregationTemporality.UNRECOGNIZED -> "UNSPECIFIED"
}

private fun List<KeyValue>.toStringMap(): Map<String, String> =
    this.associate { kv -> kv.key to kv.value.toStringValue() }

private fun AnyValue.toStringValue(): String = when (this.valueCase) {
    AnyValue.ValueCase.STRING_VALUE -> this.stringValue
    AnyValue.ValueCase.BOOL_VALUE -> this.boolValue.toString()
    AnyValue.ValueCase.INT_VALUE -> this.intValue.toString()
    AnyValue.ValueCase.DOUBLE_VALUE -> this.doubleValue.toString()
    AnyValue.ValueCase.BYTES_VALUE -> this.bytesValue.toHex()
    AnyValue.ValueCase.ARRAY_VALUE -> this.arrayValue.valuesList.joinToString(prefix = "[", postfix = "]") { it.toStringValue() }
    AnyValue.ValueCase.KVLIST_VALUE -> this.kvlistValue.valuesList.joinToString(prefix = "{", postfix = "}") { "${it.key}:${it.value.toStringValue()}" }
    AnyValue.ValueCase.VALUE_NOT_SET,
    null -> ""
}

private fun ByteString.toHex(): String {
    if (this.isEmpty) return ""
    val sb = StringBuilder(this.size() * 2)
    for (b in this.toByteArray()) {
        sb.append(((b.toInt() shr 4) and 0xF).toString(16))
        sb.append((b.toInt() and 0xF).toString(16))
    }
    return sb.toString()
}

