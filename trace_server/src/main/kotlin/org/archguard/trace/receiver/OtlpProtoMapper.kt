package org.archguard.trace.receiver

import com.google.protobuf.ByteString
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.common.v1.KeyValue
import io.opentelemetry.proto.trace.v1.Span
import io.opentelemetry.proto.trace.v1.Status
import org.archguard.trace.model.*

/**
 * Convert OTLP protobuf request into this module's simplified model request.
 *
 * This is used by BOTH:
 * - OTLP/HTTP protobuf ingestion (`Content-Type: application/x-protobuf`)
 * - OTLP/gRPC ingestion
 */
fun ExportTraceServiceRequest.toModelRequest(): OtlpExportRequest {
    return OtlpExportRequest(
        resourceSpans = this.resourceSpansList.map { rs ->
            ResourceSpans(
                resource = Resource(
                    attributes = rs.resource.attributesList.toStringMap()
                ),
                scopeSpans = rs.scopeSpansList.map { ss ->
                    ScopeSpans(
                        scope = InstrumentationScope(
                            name = ss.scope.name,
                            version = ss.scope.version.takeIf { it.isNotBlank() }
                        ),
                        spans = ss.spansList.map { it.toModelSpan() }
                    )
                }
            )
        }
    )
}

private fun Span.toModelSpan(): OtelSpan {
    return OtelSpan(
        traceId = this.traceId.toHex(),
        spanId = this.spanId.toHex(),
        parentSpanId = if (this.parentSpanId.isEmpty) null else this.parentSpanId.toHex(),
        name = this.name,
        kind = this.kind.toModelKind(),
        startTimeNanos = this.startTimeUnixNano,
        endTimeNanos = this.endTimeUnixNano,
        attributes = this.attributesList.toStringMap(),
        events = this.eventsList.map { ev ->
            SpanEvent(
                timeNanos = ev.timeUnixNano,
                name = ev.name,
                attributes = ev.attributesList.toStringMap()
            )
        },
        links = this.linksList.map { link ->
            SpanLink(
                traceId = link.traceId.toHex(),
                spanId = link.spanId.toHex(),
                attributes = link.attributesList.toStringMap()
            )
        },
        status = this.status.toModelStatus()
    )
}

private fun Span.SpanKind.toModelKind(): SpanKind = when (this) {
    Span.SpanKind.SPAN_KIND_INTERNAL -> SpanKind.INTERNAL
    Span.SpanKind.SPAN_KIND_SERVER -> SpanKind.SERVER
    Span.SpanKind.SPAN_KIND_CLIENT -> SpanKind.CLIENT
    Span.SpanKind.SPAN_KIND_PRODUCER -> SpanKind.PRODUCER
    Span.SpanKind.SPAN_KIND_CONSUMER -> SpanKind.CONSUMER
    Span.SpanKind.SPAN_KIND_UNSPECIFIED,
    Span.SpanKind.UNRECOGNIZED -> SpanKind.INTERNAL
}

private fun Status.toModelStatus(): SpanStatus {
    val code = when (this.code) {
        Status.StatusCode.STATUS_CODE_OK -> StatusCode.OK
        Status.StatusCode.STATUS_CODE_ERROR -> StatusCode.ERROR
        Status.StatusCode.STATUS_CODE_UNSET,
        Status.StatusCode.UNRECOGNIZED -> StatusCode.UNSET
    }
    val msg = this.message.takeIf { it.isNotBlank() }
    return SpanStatus(code = code, message = msg)
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

