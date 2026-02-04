package org.archguard.trace.receiver

import kotlinx.serialization.Serializable
import mu.KotlinLogging
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.model.*
import org.archguard.trace.storage.TraceStorage

private val logger = KotlinLogging.logger {}

/**
 * OTEL Trace Receiver
 * 
 * Receives OpenTelemetry traces via OTLP protocol and converts them
 * to Agent Trace format for storage.
 */
class OtelTraceReceiver(
    private val converter: OtelToAgentTraceConverter,
    private val storage: TraceStorage
) {
    
    /**
     * Receive OTLP traces via HTTP
     * 
     * @param request OTLP export request
     * @return Response indicating acceptance status
     */
    suspend fun receiveOtlpTraces(request: OtlpExportRequest): OtlpExportResponse {
        return try {
            logger.info { "Receiving OTLP traces: ${request.resourceSpans.size} resource spans" }
            
            var acceptedCount = 0
            var rejectedCount = 0
            
            request.resourceSpans.forEach { resourceSpan ->
                resourceSpan.scopeSpans.forEach { scopeSpan ->
                    try {
                        // Group spans by trace ID
                        val spansByTrace = scopeSpan.spans.groupBy { it.traceId }
                        
                        spansByTrace.forEach { (traceId, spans) ->
                            // Convert OTEL spans to Agent Trace
                            val traceRecord = converter.convert(traceId, spans)
                            
                            // Store the trace record
                            storage.store(traceRecord)
                            
                            acceptedCount += spans.size
                            logger.debug { "Stored trace $traceId with ${spans.size} spans" }
                        }
                    } catch (e: Exception) {
                        rejectedCount += scopeSpan.spans.size
                        logger.error(e) { "Failed to process scope span: ${e.message}" }
                    }
                }
            }
            
            logger.info { "OTLP traces processed: $acceptedCount accepted, $rejectedCount rejected" }
            
            OtlpExportResponse(
                partialSuccess = PartialSuccess(
                    rejectedSpans = rejectedCount.toLong(),
                    errorMessage = if (rejectedCount > 0) {
                        "Failed to process $rejectedCount spans"
                    } else {
                        null
                    }
                )
            )
        } catch (e: Exception) {
            logger.error(e) { "Failed to receive OTLP traces: ${e.message}" }
            OtlpExportResponse(
                partialSuccess = PartialSuccess(
                    rejectedSpans = request.resourceSpans.sumOf { 
                        it.scopeSpans.sumOf { scope -> scope.spans.size.toLong() }
                    },
                    errorMessage = e.message
                )
            )
        }
    }
    
    /**
     * Get trace statistics
     */
    fun getStatistics(): ReceiverStatistics {
        return ReceiverStatistics(
            totalTraces = storage.count(),
            storageType = storage.type()
        )
    }
}

/**
 * OTLP Export Request
 * 
 * Follows OpenTelemetry Protocol specification for trace export.
 */
@Serializable
data class OtlpExportRequest(
    val resourceSpans: List<ResourceSpans>
)

/**
 * OTLP Export Response
 * 
 * Response for OTLP export requests with partial success support.
 */
@Serializable
data class OtlpExportResponse(
    val partialSuccess: PartialSuccess? = null
)

/**
 * Partial success information
 */
@Serializable
data class PartialSuccess(
    val rejectedSpans: Long = 0,
    val errorMessage: String? = null
)

/**
 * Receiver statistics
 */
@Serializable
data class ReceiverStatistics(
    val totalTraces: Long,
    val storageType: String
)
