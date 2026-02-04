package org.archguard.trace.examples

import org.archguard.trace.converter.AgentTraceToOtelConverter
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.model.*

/**
 * Example usage of Agent Trace converters
 */
object ConverterExample {
    
    /**
     * Example: Convert Agent Trace record to OTEL spans
     */
    fun agentTraceToOtelExample() {
        println("=== Agent Trace → OTEL Conversion Example ===\n")
        
        // Create Agent Trace record
        val traceRecord = TraceRecord(
            version = "0.1.0",
            id = "550e8400-e29b-41d4-a716-446655440000",
            timestamp = "2026-02-04T14:30:00Z",
            vcs = VcsInfo(
                type = VcsType.GIT,
                revision = "a1b2c3d4e5f6",
                repository = "github.com/archguard/archguard",
                branch = "main"
            ),
            tool = ToolInfo(
                name = "cursor",
                version = "2.4.0"
            ),
            files = listOf(
                TraceFile(
                    path = "src/utils/parser.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://api.cursor.com/v1/conversations/12345",
                            contributor = Contributor(
                                type = ContributorType.AI,
                                modelId = "anthropic/claude-opus-4-5-20251101"
                            ),
                            ranges = listOf(
                                Range(
                                    startLine = 42,
                                    endLine = 67,
                                    contentHash = "murmur3:9f2e8a1b"
                                )
                            ),
                            related = listOf(
                                RelatedResource(
                                    type = RelationType.SESSION,
                                    url = "https://api.cursor.com/v1/sessions/67890"
                                )
                            )
                        )
                    )
                )
            )
        )
        
        // Convert to OTEL
        val converter = AgentTraceToOtelConverter()
        val otelSpans = converter.convert(traceRecord)
        
        println("Input: Agent Trace Record")
        println("  Version: ${traceRecord.version}")
        println("  VCS: ${traceRecord.vcs.type} @ ${traceRecord.vcs.revision}")
        println("  Tool: ${traceRecord.tool.name} ${traceRecord.tool.version}")
        println("  Files: ${traceRecord.files.size}")
        println()
        
        println("Output: OTEL Spans")
        println("  Total spans: ${otelSpans.size}")
        otelSpans.forEachIndexed { index, span ->
            println("  Span ${index + 1}:")
            println("    Name: ${span.name}")
            println("    Trace ID: ${span.traceId}")
            println("    Span ID: ${span.spanId}")
            println("    Parent: ${span.parentSpanId ?: "None (root)"}")
            println("    Kind: ${span.kind}")
            println("    Attributes: ${span.attributes.size} attributes")
            if (span.events.isNotEmpty()) {
                println("    Events: ${span.events.size} events")
            }
            if (span.links.isNotEmpty()) {
                println("    Links: ${span.links.size} links")
            }
            println()
        }
    }
    
    /**
     * Example: Convert OTEL spans to Agent Trace record
     */
    fun otelToAgentTraceExample() {
        println("=== OTEL → Agent Trace Conversion Example ===\n")
        
        // Create OTEL spans
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val rootSpanId = "00f067aa0ba902b6"
        val conversationSpanId = "00f067aa0ba902b7"
        
        val rootSpan = OtelSpan(
            traceId = traceId,
            spanId = rootSpanId,
            parentSpanId = null,
            name = OtelSemanticConventions.SpanNames.TRACE_RECORD,
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.Vcs.TYPE to "git",
                OtelSemanticConventions.Vcs.REVISION to "a1b2c3d4e5f6",
                OtelSemanticConventions.Vcs.REPOSITORY to "github.com/archguard/archguard",
                OtelSemanticConventions.Vcs.BRANCH to "main",
                OtelSemanticConventions.Code.CONTRIBUTOR_TOOL to "cursor",
                OtelSemanticConventions.Code.CONTRIBUTOR_TOOL_VERSION to "2.4.0"
            )
        )
        
        val conversationSpan = OtelSpan(
            traceId = traceId,
            spanId = conversationSpanId,
            parentSpanId = rootSpanId,
            name = OtelSemanticConventions.SpanNames.GENERATE_CODE,
            kind = SpanKind.CLIENT,
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.GenAI.SYSTEM to "anthropic",
                OtelSemanticConventions.GenAI.REQUEST_MODEL to "anthropic/claude-opus-4-5-20251101",
                OtelSemanticConventions.Code.CONTRIBUTOR_TYPE to "ai",
                OtelSemanticConventions.Code.GENERATION_FILE to "src/utils/parser.ts",
                OtelSemanticConventions.Conversation.URL to "https://api.cursor.com/v1/conversations/12345"
            ),
            events = listOf(
                SpanEvent(
                    timeNanos = 1706021405000000000L,
                    name = OtelSemanticConventions.SpanNames.CODE_RANGE_GENERATED,
                    attributes = mapOf(
                        OtelSemanticConventions.Code.RANGE_START to "42",
                        OtelSemanticConventions.Code.RANGE_END to "67",
                        OtelSemanticConventions.Code.RANGE_HASH to "murmur3:9f2e8a1b"
                    )
                )
            ),
            links = listOf(
                SpanLink(
                    traceId = "5bf92f3577b34da6a3ce929d0e0e4737",
                    spanId = "00f067aa0ba902b8",
                    attributes = mapOf(
                        OtelSemanticConventions.LinkTypes.TYPE to "session",
                        OtelSemanticConventions.LinkTypes.URL to "https://api.cursor.com/v1/sessions/67890"
                    )
                )
            )
        )
        
        val otelSpans = listOf(rootSpan, conversationSpan)
        
        println("Input: OTEL Spans")
        println("  Trace ID: $traceId")
        println("  Total spans: ${otelSpans.size}")
        println()
        
        // Convert to Agent Trace
        val converter = OtelToAgentTraceConverter()
        val traceRecord = converter.convert(traceId, otelSpans)
        
        println("Output: Agent Trace Record")
        println("  Version: ${traceRecord.version}")
        println("  ID: ${traceRecord.id}")
        println("  Timestamp: ${traceRecord.timestamp}")
        println("  VCS: ${traceRecord.vcs.type} @ ${traceRecord.vcs.revision}")
        println("  Repository: ${traceRecord.vcs.repository}")
        println("  Branch: ${traceRecord.vcs.branch}")
        println("  Tool: ${traceRecord.tool.name} ${traceRecord.tool.version}")
        println()
        
        println("  Files: ${traceRecord.files.size}")
        traceRecord.files.forEach { file ->
            println("    File: ${file.path}")
            println("      Conversations: ${file.conversations.size}")
            file.conversations.forEach { conv ->
                println("        - URL: ${conv.url}")
                println("          Contributor: ${conv.contributor.type}")
                println("          Model: ${conv.contributor.modelId}")
                println("          Ranges: ${conv.ranges.size}")
                conv.ranges.forEach { range ->
                    println("            Lines ${range.startLine}-${range.endLine} (${range.contentHash})")
                }
                if (conv.related.isNotEmpty()) {
                    println("          Related: ${conv.related.size} resources")
                }
            }
        }
        println()
    }
    
    /**
     * Example: Using OtelAttributeBuilder
     */
    fun attributeBuilderExample() {
        println("=== OtelAttributeBuilder Example ===\n")
        
        val attributes = OtelAttributeBuilder()
            .addContributor(Contributor(
                type = ContributorType.AI,
                modelId = "anthropic/claude-opus-4-5-20251101"
            ))
            .addVcsInfo(VcsInfo(
                type = VcsType.GIT,
                revision = "a1b2c3d4e5f6",
                repository = "github.com/archguard/archguard",
                branch = "main"
            ))
            .addToolInfo(ToolInfo(
                name = "cursor",
                version = "2.4.0"
            ))
            .addFile("src/utils/parser.ts")
            .addConversation("https://api.cursor.com/v1/conversations/12345")
            .addRange(Range(
                startLine = 42,
                endLine = 67,
                contentHash = "murmur3:9f2e8a1b"
            ))
            .build()
        
        println("Generated OTEL Attributes:")
        attributes.forEach { (key, value) ->
            println("  $key = $value")
        }
        println()
    }
}

/**
 * Run all examples
 */
fun main() {
    ConverterExample.agentTraceToOtelExample()
    println("=" .repeat(60))
    println()
    
    ConverterExample.otelToAgentTraceExample()
    println("=" .repeat(60))
    println()
    
    ConverterExample.attributeBuilderExample()
}
