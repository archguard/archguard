package org.archguard.trace.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Claude Code telemetry is exported via OpenTelemetry **metrics** and **logs/events**.
 * This file defines simplified storage/query models for those signals.
 *
 * We intentionally store OTLP attribute values as strings (best-effort) to keep
 * the API stable and storage implementation lightweight.
 */

@Serializable
data class TelemetryLogRecord(
    val id: String,
    @SerialName("time_unix_nano")
    val timeUnixNano: Long? = null,
    @SerialName("observed_time_unix_nano")
    val observedTimeUnixNano: Long? = null,
    @SerialName("severity_text")
    val severityText: String? = null,
    @SerialName("severity_number")
    val severityNumber: Int? = null,
    val body: String? = null,
    val attributes: Map<String, String> = emptyMap(),
    @SerialName("resource_attributes")
    val resourceAttributes: Map<String, String> = emptyMap(),
    @SerialName("scope_name")
    val scopeName: String? = null,
    @SerialName("scope_version")
    val scopeVersion: String? = null,
    @SerialName("event_name")
    val eventName: String? = null
)

@Serializable
data class TelemetryMetricDataPoint(
    val id: String,
    @SerialName("metric_name")
    val metricName: String,
    val description: String? = null,
    val unit: String? = null,
    @SerialName("metric_type")
    val metricType: String,
    @SerialName("start_time_unix_nano")
    val startTimeUnixNano: Long? = null,
    @SerialName("time_unix_nano")
    val timeUnixNano: Long? = null,
    val valueDouble: Double? = null,
    val valueLong: Long? = null,
    val attributes: Map<String, String> = emptyMap(),
    @SerialName("resource_attributes")
    val resourceAttributes: Map<String, String> = emptyMap(),
    @SerialName("scope_name")
    val scopeName: String? = null,
    @SerialName("scope_version")
    val scopeVersion: String? = null,
    // Sum-only fields (best-effort)
    @SerialName("is_monotonic")
    val isMonotonic: Boolean? = null,
    @SerialName("aggregation_temporality")
    val aggregationTemporality: String? = null
)

@Serializable
data class TelemetryListResponse<T>(
    val items: List<T>,
    val offset: Int,
    val limit: Int,
    val total: Long
)

