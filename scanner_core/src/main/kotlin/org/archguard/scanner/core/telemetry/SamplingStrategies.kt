package org.archguard.scanner.core.telemetry

/**
 * Sampling strategies.
 */
interface SamplingStrategy {
    fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind): Boolean
}

/**
 * Always sample (100%).
 */
class AlwaysOnSampler : SamplingStrategy {
    override fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind) = true
}

/**
 * Never sample (0%).
 */
class AlwaysOffSampler : SamplingStrategy {
    override fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind) = false
}

/**
 * Probability-based sampling based on trace ID.
 */
class TraceIdRatioBasedSampler(private val ratio: Double) : SamplingStrategy {
    init {
        require(ratio in 0.0..1.0) { "Sampling ratio must be between 0.0 and 1.0" }
    }

    override fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind): Boolean {
        if (ratio == 0.0) return false
        if (ratio == 1.0) return true

        // Use trace ID for consistent sampling decisions across the trace.
        // takeLast(16) => 64-bit hex suffix
        val traceIdLong = context.traceId.takeLast(16).toLong(16)
        val threshold = (ratio * Long.MAX_VALUE).toLong()
        return traceIdLong < threshold
    }
}

/**
 * Parent-based sampler: respects parent span's sampling decision.
 */
class ParentBasedSampler(
    private val root: SamplingStrategy = TraceIdRatioBasedSampler(0.1),
    private val remoteParentSampled: SamplingStrategy = AlwaysOnSampler(),
    private val remoteParentNotSampled: SamplingStrategy = AlwaysOffSampler(),
    private val localParentSampled: SamplingStrategy = AlwaysOnSampler(),
    private val localParentNotSampled: SamplingStrategy = AlwaysOffSampler()
) : SamplingStrategy {

    override fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind): Boolean {
        return if (context.parentSpanId == null) {
            root.shouldSample(context, spanName, spanKind)
        } else {
            val parentSampled = (context.traceFlags.toInt() and 0x01) == 1
            if (parentSampled) {
                remoteParentSampled.shouldSample(context, spanName, spanKind)
            } else {
                remoteParentNotSampled.shouldSample(context, spanName, spanKind)
            }
        }
    }
}

/**
 * Adaptive sampler: adjusts sampling rate based on error rate and latency.
 *
 * Note: currently only provides [shouldSampleSpan] for post-hoc decisions.
 */
class AdaptiveSampler(
    private val baseSamplingRate: Double = 0.1,
    private val errorSamplingRate: Double = 1.0,
    private val slowRequestThresholdMs: Long = 1000
) : SamplingStrategy {

    override fun shouldSample(context: TraceContext, spanName: String, spanKind: SpanKind): Boolean {
        // Placeholder: real adaptive sampling needs runtime stats.
        return Math.random() < baseSamplingRate
    }

    fun shouldSampleSpan(span: Span): Boolean {
        if (span.status.code == StatusCode.ERROR) {
            return true
        }

        val durationMs = span.durationMs ?: 0
        if (durationMs > slowRequestThresholdMs) {
            return true
        }

        // TODO: consider errorSamplingRate; kept for future integration.
        return Math.random() < baseSamplingRate
    }
}
