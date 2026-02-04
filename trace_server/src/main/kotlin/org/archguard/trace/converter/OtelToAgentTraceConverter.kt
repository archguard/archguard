package org.archguard.trace.converter

import org.archguard.trace.model.*
import java.time.Instant
import java.util.*

/**
 * Converter from OpenTelemetry Spans to Agent Trace Records
 * 
 * This converter transforms OTEL trace data into Agent Trace format,
 * extracting code attribution information from OTEL semantic conventions.
 */
class OtelToAgentTraceConverter {
    
    /**
     * Convert OTEL spans to Agent Trace record
     * 
     * @param traceId OTEL trace ID
     * @param spans List of OTEL spans (should all belong to the same trace)
     * @return Agent Trace record
     */
    fun convert(traceId: String, spans: List<OtelSpan>): TraceRecord {
        // Find root span (no parent)
        val rootSpan = spans.find { it.parentSpanId == null } ?: spans.first()
        
        // Extract VCS info from root span
        val vcs = extractVcsInfo(rootSpan)
        
        // Extract tool info from root span
        val tool = extractToolInfo(rootSpan)
        
        // Group spans by file
        val fileGroups = spans
            .filter { it.attributes.containsKey(OtelSemanticConventions.Code.GENERATION_FILE) }
            .groupBy { it.attributes[OtelSemanticConventions.Code.GENERATION_FILE]!! }
        
        // Convert each file group to TraceFile
        val files = fileGroups.map { (filePath, fileSpans) ->
            TraceFile(
                path = filePath,
                conversations = fileSpans.map { span -> convertSpanToConversation(span) }
            )
        }
        
        // Build metadata
        val metadata = mutableMapOf(
            "otel_trace_id" to traceId,
            "otel_root_span_id" to rootSpan.spanId
        )
        
        return TraceRecord(
            version = "0.1.0",
            id = UUID.randomUUID().toString(), // Generate new UUID for Agent Trace
            timestamp = Instant.ofEpochSecond(0, rootSpan.startTimeNanos).toString(),
            vcs = vcs,
            tool = tool,
            files = files,
            metadata = metadata
        )
    }
    
    /**
     * Extract VCS information from span attributes
     */
    private fun extractVcsInfo(span: OtelSpan): VcsInfo {
        return VcsInfo(
            type = span.attributes[OtelSemanticConventions.Vcs.TYPE] ?: VcsType.GIT,
            revision = span.attributes[OtelSemanticConventions.Vcs.REVISION] ?: "unknown",
            repository = span.attributes[OtelSemanticConventions.Vcs.REPOSITORY],
            branch = span.attributes[OtelSemanticConventions.Vcs.BRANCH]
        )
    }
    
    /**
     * Extract tool information from span attributes
     */
    private fun extractToolInfo(span: OtelSpan): ToolInfo {
        return ToolInfo(
            name = span.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TOOL] ?: "unknown",
            version = span.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TOOL_VERSION]
        )
    }
    
    /**
     * Convert OTEL span to Conversation
     */
    private fun convertSpanToConversation(span: OtelSpan): Conversation {
        // Extract contributor info
        val contributor = extractContributor(span)
        
        // Extract ranges from events
        val ranges = extractRangesFromEvents(span)
        
        // Extract related resources from links
        val related = extractRelatedResources(span)
        
        // Get conversation URL
        val conversationUrl = span.attributes[OtelSemanticConventions.Conversation.URL]
            ?: "unknown"
        
        return Conversation(
            url = conversationUrl,
            contributor = contributor,
            ranges = ranges,
            related = related
        )
    }
    
    /**
     * Extract contributor information from span attributes
     */
    private fun extractContributor(span: OtelSpan): Contributor {
        val type = span.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TYPE]
            ?: ContributorType.UNKNOWN
        
        val modelId = span.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_MODEL]
            ?: span.attributes[OtelSemanticConventions.GenAI.REQUEST_MODEL]
        
        return Contributor(
            type = type,
            modelId = modelId
        )
    }
    
    /**
     * Extract code ranges from span events
     */
    private fun extractRangesFromEvents(span: OtelSpan): List<Range> {
        return span.events
            .filter { it.name == OtelSemanticConventions.SpanNames.CODE_RANGE_GENERATED }
            .mapNotNull { event ->
                val startLine = event.attributes[OtelSemanticConventions.Code.RANGE_START]?.toIntOrNull()
                val endLine = event.attributes[OtelSemanticConventions.Code.RANGE_END]?.toIntOrNull()
                
                if (startLine != null && endLine != null) {
                    Range(
                        startLine = startLine,
                        endLine = endLine,
                        contentHash = event.attributes[OtelSemanticConventions.Code.RANGE_HASH]
                    )
                } else {
                    null
                }
            }
    }
    
    /**
     * Extract related resources from span links
     */
    private fun extractRelatedResources(span: OtelSpan): List<RelatedResource> {
        return span.links.mapNotNull { link ->
            val url = link.attributes[OtelSemanticConventions.LinkTypes.URL]
            val type = link.attributes[OtelSemanticConventions.LinkTypes.TYPE]
                ?: RelationType.REFERENCE
            
            if (url != null) {
                RelatedResource(
                    type = type,
                    url = url
                )
            } else {
                null
            }
        }
    }
}
