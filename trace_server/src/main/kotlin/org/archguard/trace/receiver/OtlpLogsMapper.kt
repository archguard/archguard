package org.archguard.trace.receiver

import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
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

