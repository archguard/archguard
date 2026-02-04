package org.archguard.trace.converter

import org.archguard.trace.model.*
import java.time.Instant
import java.util.*

/**
 * Converter from Agent Trace Records to OpenTelemetry Spans
 * 
 * This converter transforms Agent Trace records into OTEL format,
 * enabling integration with OTEL collectors and visualization tools.
 */
class AgentTraceToOtelConverter {
    
    /**
     * Convert Agent Trace record to OTEL spans
     * 
     * @param record Agent Trace record
     * @return List of OTEL spans representing the trace
     */
    fun convert(record: TraceRecord): List<OtelSpan> {
        val spans = mutableListOf<OtelSpan>()
        
        // Use existing trace ID if available, otherwise generate new one
        val traceId = record.metadata["otel_trace_id"] ?: generateTraceId()
        
        // Create root span representing the entire trace record
        val rootSpanId = record.metadata["otel_root_span_id"] ?: generateSpanId()
        val timestamp = parseTimestamp(record.timestamp)
        
        val rootSpan = createRootSpan(
            traceId = traceId,
            spanId = rootSpanId,
            record = record,
            timestamp = timestamp
        )
        spans.add(rootSpan)
        
        // Create spans for each conversation
        record.files.forEach { file ->
            file.conversations.forEach { conversation ->
                val conversationSpan = createConversationSpan(
                    traceId = traceId,
                    parentSpanId = rootSpanId,
                    file = file,
                    conversation = conversation,
                    record = record,
                    timestamp = timestamp
                )
                spans.add(conversationSpan)
            }
        }
        
        return spans
    }
    
    /**
     * Create root span for the trace record
     */
    private fun createRootSpan(
        traceId: String,
        spanId: String,
        record: TraceRecord,
        timestamp: Long
    ): OtelSpan {
        val attributes = OtelAttributeBuilder()
            .addVcsInfo(record.vcs)
            .addToolInfo(record.tool)
            .build()
            .mapValues { it.value.toString() }
        
        return OtelSpan(
            traceId = traceId,
            spanId = spanId,
            parentSpanId = null,
            name = OtelSemanticConventions.SpanNames.TRACE_RECORD,
            kind = SpanKind.INTERNAL,
            startTimeNanos = timestamp,
            endTimeNanos = timestamp,
            attributes = attributes
        )
    }
    
    /**
     * Create span for a conversation
     */
    private fun createConversationSpan(
        traceId: String,
        parentSpanId: String,
        file: TraceFile,
        conversation: Conversation,
        record: TraceRecord,
        timestamp: Long
    ): OtelSpan {
        val spanId = generateSpanId()
        
        // Build attributes
        val attributeBuilder = OtelAttributeBuilder()
            .addContributor(conversation.contributor)
            .addFile(file.path)
            .addConversation(conversation.url)
        
        // Add VCS info
        attributeBuilder.addVcsInfo(record.vcs)
        
        val attributes = attributeBuilder.build()
            .mapValues { it.value.toString() }
        
        // Create events for each range
        val events = conversation.ranges.map { range ->
            createRangeEvent(range, timestamp)
        }
        
        // Create links for related resources
        val links = conversation.related.map { related ->
            createRelatedLink(related)
        }
        
        return OtelSpan(
            traceId = traceId,
            spanId = spanId,
            parentSpanId = parentSpanId,
            name = OtelSemanticConventions.SpanNames.GENERATE_CODE,
            kind = SpanKind.CLIENT,
            startTimeNanos = timestamp,
            endTimeNanos = timestamp,
            attributes = attributes,
            events = events,
            links = links
        )
    }
    
    /**
     * Create span event for a code range
     */
    private fun createRangeEvent(range: Range, timestamp: Long): SpanEvent {
        val attributes = OtelAttributeBuilder()
            .addRange(range)
            .build()
            .mapValues { it.value.toString() }
        
        return SpanEvent(
            timeNanos = timestamp,
            name = OtelSemanticConventions.SpanNames.CODE_RANGE_GENERATED,
            attributes = attributes
        )
    }
    
    /**
     * Create span link for related resource
     */
    private fun createRelatedLink(related: RelatedResource): SpanLink {
        return SpanLink(
            traceId = generateTraceId(), // Generate new trace ID for external resource
            spanId = generateSpanId(),
            attributes = mapOf(
                OtelSemanticConventions.LinkTypes.TYPE to related.type,
                OtelSemanticConventions.LinkTypes.URL to related.url
            )
        )
    }
    
    /**
     * Parse RFC3339 timestamp to nanoseconds
     */
    private fun parseTimestamp(timestamp: String): Long {
        return try {
            Instant.parse(timestamp).toEpochMilli() * 1_000_000 // Convert to nanos
        } catch (e: Exception) {
            System.currentTimeMillis() * 1_000_000
        }
    }
    
    /**
     * Generate random trace ID (128-bit hex)
     */
    private fun generateTraceId(): String {
        return UUID.randomUUID().toString().replace("-", "") +
                UUID.randomUUID().toString().replace("-", "").substring(0, 16)
    }
    
    /**
     * Generate random span ID (64-bit hex)
     */
    private fun generateSpanId(): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16)
    }
}
