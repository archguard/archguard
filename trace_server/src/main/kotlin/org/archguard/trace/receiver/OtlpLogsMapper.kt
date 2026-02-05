package org.archguard.trace.receiver

import com.google.protobuf.ByteString
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.common.v1.KeyValue
import org.archguard.trace.model.TelemetryLogRecord
import java.util.UUID

fun ExportLogsServiceRequest.toTelemetryLogRecords(): List<TelemetryLogRecord> {
    val out = ArrayList<TelemetryLogRecord>(256)
    for (rl in this.resourceLogsList) {
        val resourceAttrs = rl.resource.attributesList.toStringMap()
        for (sl in rl.scopeLogsList) {
            val scopeName = sl.scope.name.takeIf { it.isNotBlank() }
            val scopeVersion = sl.scope.version.takeIf { it.isNotBlank() }
            for (lr in sl.logRecordsList) {
                val attrs = lr.attributesList.toStringMap()
                val eventName = attrs["event.name"] ?: attrs["event_name"]
                out.add(
                    TelemetryLogRecord(
                        id = UUID.randomUUID().toString(),
                        timeUnixNano = lr.timeUnixNano.takeIf { it != 0L },
                        observedTimeUnixNano = lr.observedTimeUnixNano.takeIf { it != 0L },
                        severityText = lr.severityText.takeIf { it.isNotBlank() },
                        severityNumber = lr.severityNumberValue.takeIf { it != 0 },
                        body = lr.body.toStringValue().takeIf { it.isNotBlank() },
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

