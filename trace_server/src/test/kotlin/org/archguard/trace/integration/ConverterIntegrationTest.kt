package org.archguard.trace.integration

import org.archguard.trace.converter.AgentTraceToOtelConverter
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Integration tests for bidirectional conversion
 * 
 * Converted from ConverterExample.kt
 */
class ConverterIntegrationTest {
    
    @Test
    fun `should perform Agent Trace to OTEL conversion end-to-end`() {
        // Given: Agent Trace record
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
        
        // When: Convert to OTEL
        val converter = AgentTraceToOtelConverter()
        val otelSpans = converter.convert(traceRecord)
        
        // Then: Verify conversion
        assertNotNull(otelSpans)
        assertEquals(2, otelSpans.size, "Should have root span + conversation span")
        
        val rootSpan = otelSpans[0]
        assertNull(rootSpan.parentSpanId, "Root span should have no parent")
        assertEquals(OtelSemanticConventions.SpanNames.TRACE_RECORD, rootSpan.name)
        assertEquals("git", rootSpan.attributes[OtelSemanticConventions.Vcs.TYPE])
        assertEquals("a1b2c3d4e5f6", rootSpan.attributes[OtelSemanticConventions.Vcs.REVISION])
        
        val conversationSpan = otelSpans[1]
        assertEquals(rootSpan.spanId, conversationSpan.parentSpanId)
        assertEquals(OtelSemanticConventions.SpanNames.GENERATE_CODE, conversationSpan.name)
        assertEquals("ai", conversationSpan.attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TYPE])
        assertEquals(1, conversationSpan.events.size)
        assertEquals(1, conversationSpan.links.size)
    }
    
    @Test
    fun `should perform OTEL to Agent Trace conversion end-to-end`() {
        // Given: OTEL spans
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
        
        // When: Convert to Agent Trace
        val converter = OtelToAgentTraceConverter()
        val traceRecord = converter.convert(traceId, otelSpans)
        
        // Then: Verify conversion
        assertNotNull(traceRecord)
        assertEquals("0.1.0", traceRecord.version)
        assertEquals("git", traceRecord.vcs.type)
        assertEquals("a1b2c3d4e5f6", traceRecord.vcs.revision)
        assertEquals("github.com/archguard/archguard", traceRecord.vcs.repository)
        assertEquals("main", traceRecord.vcs.branch)
        assertEquals("cursor", traceRecord.tool.name)
        assertEquals("2.4.0", traceRecord.tool.version)
        
        assertEquals(1, traceRecord.files.size)
        val file = traceRecord.files[0]
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
    }
    
    @Test
    fun `should perform round-trip conversion without data loss`() {
        // Given: Original trace record
        val original = createCompleteTraceRecord()
        
        // When: Agent Trace → OTEL → Agent Trace
        val agentToOtel = AgentTraceToOtelConverter()
        val otelSpans = agentToOtel.convert(original)
        
        val otelToAgent = OtelToAgentTraceConverter()
        val roundTrip = otelToAgent.convert(otelSpans.first().traceId, otelSpans)
        
        // Then: Should preserve key data
        assertEquals(original.version, roundTrip.version)
        assertEquals(original.vcs.type, roundTrip.vcs.type)
        assertEquals(original.vcs.revision, roundTrip.vcs.revision)
        assertEquals(original.tool.name, roundTrip.tool.name)
        assertEquals(original.files.size, roundTrip.files.size)
        assertEquals(original.files[0].path, roundTrip.files[0].path)
        assertEquals(original.files[0].conversations.size, roundTrip.files[0].conversations.size)
    }
    
    @Test
    fun `should handle multiple files and conversations`() {
        // Given: Trace with multiple files
        val traceRecord = TraceRecord(
            version = "0.1.0",
            id = "test-multi",
            timestamp = "2026-02-04T14:30:00Z",
            vcs = VcsInfo(type = "git", revision = "abc123"),
            tool = ToolInfo(name = "cursor"),
            files = listOf(
                TraceFile(
                    path = "file1.ts",
                    conversations = listOf(
                        createTestConversation("conv1"),
                        createTestConversation("conv2")
                    )
                ),
                TraceFile(
                    path = "file2.ts",
                    conversations = listOf(
                        createTestConversation("conv3")
                    )
                )
            )
        )
        
        // When: Convert to OTEL
        val converter = AgentTraceToOtelConverter()
        val spans = converter.convert(traceRecord)
        
        // Then: Should create 1 root + 3 conversation spans
        assertEquals(4, spans.size)
        
        val rootSpan = spans[0]
        assertNull(rootSpan.parentSpanId)
        
        val conversationSpans = spans.drop(1)
        assertEquals(3, conversationSpans.size)
        conversationSpans.forEach { span ->
            assertEquals(rootSpan.spanId, span.parentSpanId)
        }
    }
    
    private fun createCompleteTraceRecord(): TraceRecord {
        return TraceRecord(
            version = "0.1.0",
            id = "complete-test",
            timestamp = "2026-02-04T14:30:00Z",
            vcs = VcsInfo(
                type = "git",
                revision = "abc123",
                repository = "github.com/test/repo",
                branch = "main"
            ),
            tool = ToolInfo(
                name = "cursor",
                version = "2.4.0"
            ),
            files = listOf(
                TraceFile(
                    path = "src/test.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://example.com/conv/1",
                            contributor = Contributor(
                                type = "ai",
                                modelId = "anthropic/claude-opus"
                            ),
                            ranges = listOf(
                                Range(
                                    startLine = 1,
                                    endLine = 10,
                                    contentHash = "hash1"
                                )
                            )
                        )
                    )
                )
            )
        )
    }
    
    private fun createTestConversation(id: String): Conversation {
        return Conversation(
            url = "https://example.com/conv/$id",
            contributor = Contributor(type = "ai"),
            ranges = emptyList()
        )
    }
}
