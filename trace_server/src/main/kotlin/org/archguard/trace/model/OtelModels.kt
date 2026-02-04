package org.archguard.trace.model

import kotlinx.serialization.Serializable

/**
 * Simplified OTEL Span Data model for Agent Trace integration
 * 
 * This is a simplified representation of OpenTelemetry Span.
 * For full OTEL integration, use io.opentelemetry.sdk.trace.data.SpanData
 */
@Serializable
data class OtelSpan(
    val traceId: String,
    val spanId: String,
    val parentSpanId: String? = null,
    val name: String,
    val kind: SpanKind = SpanKind.CLIENT,
    val startTimeNanos: Long,
    val endTimeNanos: Long,
    val attributes: Map<String, String>,
    val events: List<SpanEvent> = emptyList(),
    val links: List<SpanLink> = emptyList(),
    val status: SpanStatus = SpanStatus(StatusCode.OK)
)

/**
 * Span kind
 */
@Serializable
enum class SpanKind {
    INTERNAL,
    SERVER,
    CLIENT,
    PRODUCER,
    CONSUMER
}

/**
 * Span event
 */
@Serializable
data class SpanEvent(
    val timeNanos: Long,
    val name: String,
    val attributes: Map<String, String> = emptyMap()
)

/**
 * Span link
 */
@Serializable
data class SpanLink(
    val traceId: String,
    val spanId: String,
    val attributes: Map<String, String> = emptyMap()
)

/**
 * Span status
 */
@Serializable
data class SpanStatus(
    val code: StatusCode,
    val message: String? = null
)

/**
 * Status code
 */
@Serializable
enum class StatusCode {
    UNSET,
    OK,
    ERROR
}

/**
 * OTLP Export Request (simplified)
 */
@Serializable
data class OtlpTraceRequest(
    val resourceSpans: List<ResourceSpans>
)

@Serializable
data class ResourceSpans(
    val resource: Resource,
    val scopeSpans: List<ScopeSpans>
)

@Serializable
data class Resource(
    val attributes: Map<String, String>
)

@Serializable
data class ScopeSpans(
    val scope: InstrumentationScope,
    val spans: List<OtelSpan>
)

@Serializable
data class InstrumentationScope(
    val name: String,
    val version: String? = null
)

/**
 * OTLP Export Response
 */
@Serializable
data class OtlpTraceResponse(
    val accepted: Boolean,
    val message: String? = null
)
