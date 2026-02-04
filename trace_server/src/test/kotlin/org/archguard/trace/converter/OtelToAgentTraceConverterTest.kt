package org.archguard.trace.converter

import org.archguard.trace.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OtelToAgentTraceConverterTest {
    
    private val converter = OtelToAgentTraceConverter()
    
    @Test
    fun `should convert OTEL spans to Agent Trace record`() {
        // Given: OTEL spans with code attribution
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val rootSpanId = "00f067aa0ba902b6"
        val conversationSpanId = "00f067aa0ba902b7"
        
        val rootSpan = OtelSpan(
            traceId = traceId,
            spanId = rootSpanId,
            parentSpanId = null,
            name = "agent_trace_record",
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.Vcs.TYPE to "git",
                OtelSemanticConventions.Vcs.REVISION to "a1b2c3d4e5f6",
                OtelSemanticConventions.Code.CONTRIBUTOR_TOOL to "cursor",
                OtelSemanticConventions.Code.CONTRIBUTOR_TOOL_VERSION to "2.4.0"
            )
        )
        
        val conversationSpan = OtelSpan(
            traceId = traceId,
            spanId = conversationSpanId,
            parentSpanId = rootSpanId,
            name = "generate_code",
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
        
        val spans = listOf(rootSpan, conversationSpan)
        
        // When: Convert to Agent Trace
        val record = converter.convert(traceId, spans)
        
        // Then: Verify conversion
        assertEquals("0.1.0", record.version)
        assertEquals("git", record.vcs.type)
        assertEquals("a1b2c3d4e5f6", record.vcs.revision)
        assertEquals("cursor", record.tool.name)
        assertEquals("2.4.0", record.tool.version)
        
        assertEquals(1, record.files.size)
        val file = record.files[0]
        assertEquals("src/utils/parser.ts", file.path)
        
        assertEquals(1, file.conversations.size)
        val conversation = file.conversations[0]
        assertEquals("https://api.cursor.com/v1/conversations/12345", conversation.url)
        assertEquals("ai", conversation.contributor.type)
        assertEquals("anthropic/claude-opus-4-5-20251101", conversation.contributor.modelId)
        
        assertEquals(1, conversation.ranges.size)
        val range = conversation.ranges[0]
        assertEquals(42, range.startLine)
        assertEquals(67, range.endLine)
        assertEquals("murmur3:9f2e8a1b", range.contentHash)
        
        assertEquals(1, conversation.related.size)
        val related = conversation.related[0]
        assertEquals("session", related.type)
        assertEquals("https://api.cursor.com/v1/sessions/67890", related.url)
        
        assertTrue(record.metadata.containsKey("otel_trace_id"))
        assertEquals(traceId, record.metadata["otel_trace_id"])
    }
    
    @Test
    fun `should handle missing optional attributes`() {
        // Given: OTEL span with minimal attributes
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val spanId = "00f067aa0ba902b6"
        
        val span = OtelSpan(
            traceId = traceId,
            spanId = spanId,
            name = "generate_code",
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.Code.GENERATION_FILE to "src/test.ts"
            )
        )
        
        // When: Convert to Agent Trace
        val record = converter.convert(traceId, listOf(span))
        
        // Then: Verify defaults
        assertEquals("git", record.vcs.type) // Default VCS type
        assertEquals("unknown", record.vcs.revision)
        assertEquals("unknown", record.tool.name)
        assertNull(record.tool.version)
    }
    
    @Test
    fun `should group spans by file path`() {
        // Given: Multiple spans for different files
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        
        val span1 = createTestSpan(traceId, "span1", "file1.ts")
        val span2 = createTestSpan(traceId, "span2", "file2.ts")
        val span3 = createTestSpan(traceId, "span3", "file1.ts")
        
        // When: Convert to Agent Trace
        val record = converter.convert(traceId, listOf(span1, span2, span3))
        
        // Then: Should group by file
        assertEquals(2, record.files.size)
        
        val file1 = record.files.find { it.path == "file1.ts" }
        assertNotNull(file1)
        assertEquals(2, file1!!.conversations.size)
        
        val file2 = record.files.find { it.path == "file2.ts" }
        assertNotNull(file2)
        assertEquals(1, file2!!.conversations.size)
    }
    
    private fun createTestSpan(traceId: String, spanId: String, filePath: String): OtelSpan {
        return OtelSpan(
            traceId = traceId,
            spanId = spanId,
            name = "generate_code",
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.Code.GENERATION_FILE to filePath,
                OtelSemanticConventions.Conversation.URL to "https://example.com/$spanId"
            )
        )
    }
}
