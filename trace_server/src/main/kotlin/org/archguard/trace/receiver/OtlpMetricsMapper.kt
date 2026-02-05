package org.archguard.trace.receiver

import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
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
            // Store count, sum, and bucket data as separate points
            val pts = ArrayList<TelemetryMetricDataPoint>()
            val temporality = this.histogram.aggregationTemporality.toStringValue()
            for (dp in this.histogram.dataPointsList) {
                val attrs = dp.attributesList.toStringMap()
                // Count
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
                        attributes = attrs,
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
                // Sum (if present)
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
                            attributes = attrs,
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
                // Bucket data (if present)
                if (dp.bucketCountsCount > 0) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "histogram_buckets",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            attributes = attrs + mapOf(
                                "_bucket_counts" to dp.bucketCountsList.joinToString(","),
                                "_explicit_bounds" to dp.explicitBoundsList.joinToString(",")
                            ),
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
                // Min/Max (if present)
                if (dp.hasMin()) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "histogram_min",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            valueDouble = dp.min,
                            attributes = attrs,
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
                if (dp.hasMax()) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "histogram_max",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            valueDouble = dp.max,
                            attributes = attrs,
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
            }
            pts
        }
        Metric.DataCase.EXPONENTIAL_HISTOGRAM -> {
            val pts = ArrayList<TelemetryMetricDataPoint>()
            val temporality = this.exponentialHistogram.aggregationTemporality.toStringValue()
            for (dp in this.exponentialHistogram.dataPointsList) {
                val attrs = dp.attributesList.toStringMap()
                // Count
                pts.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = name,
                        description = desc,
                        unit = unit,
                        metricType = "exponential_histogram_count",
                        startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                        timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                        valueLong = dp.count,
                        attributes = attrs + mapOf(
                            "_scale" to dp.scale.toString(),
                            "_zero_count" to dp.zeroCount.toString()
                        ),
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion,
                        aggregationTemporality = temporality
                    )
                )
                // Sum (if present)
                if (dp.hasSum()) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "exponential_histogram_sum",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            valueDouble = dp.sum,
                            attributes = attrs,
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
                // Positive buckets
                if (dp.positive.bucketCountsCount > 0) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "exponential_histogram_positive",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            attributes = attrs + mapOf(
                                "_bucket_offset" to dp.positive.offset.toString(),
                                "_bucket_counts" to dp.positive.bucketCountsList.joinToString(",")
                            ),
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
                // Negative buckets
                if (dp.negative.bucketCountsCount > 0) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "exponential_histogram_negative",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            attributes = attrs + mapOf(
                                "_bucket_offset" to dp.negative.offset.toString(),
                                "_bucket_counts" to dp.negative.bucketCountsList.joinToString(",")
                            ),
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion,
                            aggregationTemporality = temporality
                        )
                    )
                }
            }
            pts
        }
        Metric.DataCase.SUMMARY -> {
            val pts = ArrayList<TelemetryMetricDataPoint>()
            for (dp in this.summary.dataPointsList) {
                val attrs = dp.attributesList.toStringMap()
                // Count
                pts.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = name,
                        description = desc,
                        unit = unit,
                        metricType = "summary_count",
                        startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                        timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                        valueLong = dp.count,
                        attributes = attrs,
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion
                    )
                )
                // Sum
                pts.add(
                    TelemetryMetricDataPoint(
                        id = UUID.randomUUID().toString(),
                        metricName = name,
                        description = desc,
                        unit = unit,
                        metricType = "summary_sum",
                        startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                        timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                        valueDouble = dp.sum,
                        attributes = attrs,
                        resourceAttributes = resourceAttrs,
                        scopeName = scopeName,
                        scopeVersion = scopeVersion
                    )
                )
                // Quantile values
                for (qv in dp.quantileValuesList) {
                    pts.add(
                        TelemetryMetricDataPoint(
                            id = UUID.randomUUID().toString(),
                            metricName = name,
                            description = desc,
                            unit = unit,
                            metricType = "summary_quantile",
                            startTimeUnixNano = dp.startTimeUnixNano.takeIf { it != 0L },
                            timeUnixNano = dp.timeUnixNano.takeIf { it != 0L },
                            valueDouble = qv.value,
                            attributes = attrs + mapOf("_quantile" to qv.quantile.toString()),
                            resourceAttributes = resourceAttrs,
                            scopeName = scopeName,
                            scopeVersion = scopeVersion
                        )
                    )
                }
            }
            pts
        }
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

