package org.archguard.trace.converter

import org.archguard.trace.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AgentTraceToOtelConverterTest {
    
    private val converter = AgentTraceToOtelConverter()
    
    @Test
    fun `should convert Agent Trace record to OTEL spans`() {
        // Given: Agent Trace record
        val record = TraceRecord(
            version = "0.1.0",
            id = "550e8400-e29b-41d4-a716-446655440000",
            timestamp = "2026-01-23T14:30:00Z",
            vcs = VcsInfo(
                type = "git",
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
                                type = "ai",
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
                                    type = "session",
                                    url = "https://api.cursor.com/v1/sessions/67890"
                                )
                            )
                        )
                    )
                )
            )
        )
        
        // When: Convert to OTEL
        val spans = converter.convert(record)
        
        // Then: Should create root span + conversation span
        assertEquals(2, spans.size)
        
        // Verify root span
        val rootSpan = spans[0]
        assertNull(rootSpan.parentSpanId)
        assertEquals(OtelSemanticConventions.SpanNames.TRACE_RECORD, rootSpan.name)
        assertEquals("git", rootSpan.attributes[OtelSemanticConventions.Vcs.TYPE])
        assertEquals("a1b2c3d4e5f6", rootSpan.attributes[OtelSemanticConventions.Vcs.REVISION])
        assertEquals("cursor", rootSpan.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TOOL])
        
        // Verify conversation span
        val conversationSpan = spans[1]
        assertEquals(rootSpan.spanId, conversationSpan.parentSpanId)
        assertEquals(OtelSemanticConventions.SpanNames.GENERATE_CODE, conversationSpan.name)
        assertEquals(SpanKind.CLIENT, conversationSpan.kind)
        assertEquals("ai", conversationSpan.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TYPE])
        assertEquals("anthropic/claude-opus-4-5-20251101", conversationSpan.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_MODEL])
        assertEquals("anthropic", conversationSpan.attributes[OtelSemanticConventions.GenAI.SYSTEM])
        assertEquals("src/utils/parser.ts", conversationSpan.attributes[OtelSemanticConventions.Code.GENERATION_FILE])
        assertEquals("https://api.cursor.com/v1/conversations/12345", conversationSpan.attributes[OtelSemanticConventions.Conversation.URL])
        
        // Verify events
        assertEquals(1, conversationSpan.events.size)
        val event = conversationSpan.events[0]
        assertEquals(OtelSemanticConventions.SpanNames.CODE_RANGE_GENERATED, event.name)
        assertEquals("42", event.attributes[OtelSemanticConventions.Code.RANGE_START])
        assertEquals("67", event.attributes[OtelSemanticConventions.Code.RANGE_END])
        assertEquals("murmur3:9f2e8a1b", event.attributes[OtelSemanticConventions.Code.RANGE_HASH])
        assertEquals("26", event.attributes[OtelSemanticConventions.Code.RANGE_LINES])
        
        // Verify links
        assertEquals(1, conversationSpan.links.size)
        val link = conversationSpan.links[0]
        assertEquals("session", link.attributes[OtelSemanticConventions.LinkTypes.TYPE])
        assertEquals("https://api.cursor.com/v1/sessions/67890", link.attributes[OtelSemanticConventions.LinkTypes.URL])
    }
    
    @Test
    fun `should create multiple conversation spans for multiple files`() {
        // Given: Agent Trace with multiple files
        val record = TraceRecord(
            version = "0.1.0",
            id = "550e8400-e29b-41d4-a716-446655440000",
            timestamp = "2026-01-23T14:30:00Z",
            vcs = VcsInfo(type = "git", revision = "abc123"),
            tool = ToolInfo(name = "cursor"),
            files = listOf(
                TraceFile(
                    path = "file1.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://example.com/conv1",
                            contributor = Contributor(type = "ai"),
                            ranges = emptyList()
                        )
                    )
                ),
                TraceFile(
                    path = "file2.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://example.com/conv2",
                            contributor = Contributor(type = "ai"),
                            ranges = emptyList()
                        ),
                        Conversation(
                            url = "https://example.com/conv3",
                            contributor = Contributor(type = "human"),
                            ranges = emptyList()
                        )
                    )
                )
            )
        )
        
        // When: Convert to OTEL
        val spans = converter.convert(record)
        
        // Then: Should create 1 root + 3 conversation spans
        assertEquals(4, spans.size)
        
        val rootSpan = spans[0]
        assertNull(rootSpan.parentSpanId)
        
        val conversationSpans = spans.drop(1)
        assertEquals(3, conversationSpans.size)
        
        // All conversation spans should have root as parent
        conversationSpans.forEach { span ->
            assertEquals(rootSpan.spanId, span.parentSpanId)
        }
    }
    
    @Test
    fun `should handle empty ranges and related resources`() {
        // Given: Conversation with no ranges or related resources
        val record = TraceRecord(
            version = "0.1.0",
            id = "550e8400-e29b-41d4-a716-446655440000",
            timestamp = "2026-01-23T14:30:00Z",
            vcs = VcsInfo(type = "git", revision = "abc123"),
            tool = ToolInfo(name = "cursor"),
            files = listOf(
                TraceFile(
                    path = "file.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://example.com/conv",
                            contributor = Contributor(type = "ai"),
                            ranges = emptyList(),
                            related = emptyList()
                        )
                    )
                )
            )
        )
        
        // When: Convert to OTEL
        val spans = converter.convert(record)
        
        // Then: Should handle empty collections
        assertEquals(2, spans.size)
        
        val conversationSpan = spans[1]
        assertTrue(conversationSpan.events.isEmpty())
        assertTrue(conversationSpan.links.isEmpty())
    }
    
    @Test
    fun `should use existing OTEL trace ID from metadata`() {
        // Given: Agent Trace with existing OTEL metadata
        val existingTraceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val existingSpanId = "00f067aa0ba902b7"
        
        val record = TraceRecord(
            version = "0.1.0",
            id = "550e8400-e29b-41d4-a716-446655440000",
            timestamp = "2026-01-23T14:30:00Z",
            vcs = VcsInfo(type = "git", revision = "abc123"),
            tool = ToolInfo(name = "cursor"),
            files = emptyList(),
            metadata = mapOf(
                "otel_trace_id" to existingTraceId,
                "otel_root_span_id" to existingSpanId
            )
        )
        
        // When: Convert to OTEL
        val spans = converter.convert(record)
        
        // Then: Should use existing IDs
        assertEquals(1, spans.size)
        val rootSpan = spans[0]
        assertEquals(existingTraceId, rootSpan.traceId)
        assertEquals(existingSpanId, rootSpan.spanId)
    }
}
