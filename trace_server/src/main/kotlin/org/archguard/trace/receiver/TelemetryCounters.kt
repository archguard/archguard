package org.archguard.trace.receiver

import java.util.concurrent.atomic.AtomicLong

class TelemetryCounters {
    val otlpGrpcMetricsRequests = AtomicLong(0)
    val otlpGrpcLogsRequests = AtomicLong(0)
    val otlpGrpcTraceRequests = AtomicLong(0)

    val otlpHttpMetricsRequests = AtomicLong(0)
    val otlpHttpLogsRequests = AtomicLong(0)
    val otlpHttpTraceRequests = AtomicLong(0)
}

