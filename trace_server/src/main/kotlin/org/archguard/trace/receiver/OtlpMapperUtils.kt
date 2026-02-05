package org.archguard.trace.receiver

import com.google.protobuf.ByteString
import io.opentelemetry.proto.common.v1.AnyValue
import io.opentelemetry.proto.common.v1.KeyValue

/**
 * Shared OTLP mapping utilities used by multiple mapper files.
 *
 * Keeping these in one place reduces duplication and avoids accidental drift.
 */
internal fun List<KeyValue>.toStringMap(): Map<String, String> =
    this.associate { kv -> kv.key to kv.value.toStringValue() }

internal fun AnyValue.toStringValue(): String = when (this.valueCase) {
    AnyValue.ValueCase.STRING_VALUE -> this.stringValue
    AnyValue.ValueCase.BOOL_VALUE -> this.boolValue.toString()
    AnyValue.ValueCase.INT_VALUE -> this.intValue.toString()
    AnyValue.ValueCase.DOUBLE_VALUE -> this.doubleValue.toString()
    AnyValue.ValueCase.BYTES_VALUE -> this.bytesValue.toHex()
    AnyValue.ValueCase.ARRAY_VALUE ->
        this.arrayValue.valuesList.joinToString(prefix = "[", postfix = "]") { it.toStringValue() }
    AnyValue.ValueCase.KVLIST_VALUE ->
        this.kvlistValue.valuesList.joinToString(prefix = "{", postfix = "}") { "${it.key}:${it.value.toStringValue()}" }
    AnyValue.ValueCase.VALUE_NOT_SET,
    null -> ""
}

internal fun ByteString.toHex(): String {
    if (this.isEmpty) return ""
    val sb = StringBuilder(this.size() * 2)
    for (b in this.toByteArray()) {
        sb.append(((b.toInt() shr 4) and 0xF).toString(16))
        sb.append((b.toInt() and 0xF).toString(16))
    }
    return sb.toString()
}

