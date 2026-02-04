package org.archguard.trace.receiver

import kotlinx.coroutines.runBlocking
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.model.*
import org.archguard.trace.storage.InMemoryTraceStorage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OtelTraceReceiverTest {
    
    private lateinit var storage: InMemoryTraceStorage
    private lateinit var converter: OtelToAgentTraceConverter
    private lateinit var receiver: OtelTraceReceiver
    
    @BeforeEach
    fun setup() {
        storage = InMemoryTraceStorage()
        converter = OtelToAgentTraceConverter()
        receiver = OtelTraceReceiver(converter, storage)
    }
    
    @Test
    fun `should receive and store OTLP traces`() = runBlocking {
        // Given: OTLP export request with valid traces
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val spanId = "00f067aa0ba902b7"
        
        val span = OtelSpan(
            traceId = traceId,
            spanId = spanId,
            name = "generate_code",
            startTimeNanos = 1706021400000000000L,
            endTimeNanos = 1706021405000000000L,
            attributes = mapOf(
                OtelSemanticConventions.Code.GENERATION_FILE to "src/test.ts",
                OtelSemanticConventions.Code.CONTRIBUTOR_TYPE to "ai",
                OtelSemanticConventions.Conversation.URL to "https://example.com/conv/123",
                OtelSemanticConventions.Vcs.TYPE to "git",
                OtelSemanticConventions.Vcs.REVISION to "abc123"
            )
        )
        
        val request = OtlpExportRequest(
            resourceSpans = listOf(
                ResourceSpans(
                    resource = Resource(
                        attributes = mapOf("service.name" to "cursor-ai-agent")
                    ),
                    scopeSpans = listOf(
                        ScopeSpans(
                            scope = InstrumentationScope(
                                name = "cursor.code_generation",
                                version = "1.0.0"
                            ),
                            spans = listOf(span)
                        )
                    )
                )
            )
        )
        
        // When: Receive traces
        val response = receiver.receiveOtlpTraces(request)
        
        // Then: Should accept traces
        assertNotNull(response.partialSuccess)
        assertEquals(0, response.partialSuccess?.rejectedSpans)
        assertNull(response.partialSuccess?.errorMessage)
        
        // And: Should store in storage
        assertEquals(1, storage.count())
        
        val storedTraces = storage.list()
        assertEquals(1, storedTraces.size)
        
        val storedTrace = storedTraces[0]
        assertEquals("0.1.0", storedTrace.version)
        assertEquals("git", storedTrace.vcs.type)
        assertEquals("abc123", storedTrace.vcs.revision)
        assertEquals(1, storedTrace.files.size)
        assertEquals("src/test.ts", storedTrace.files[0].path)
    }
    
    @Test
    fun `should handle multiple traces in one request`() = runBlocking {
        // Given: Multiple traces
        val trace1 = "trace-id-1"
        val trace2 = "trace-id-2"
        
        val span1 = createTestSpan(trace1, "span1", "file1.ts")
        val span2 = createTestSpan(trace2, "span2", "file2.ts")
        
        val request = OtlpExportRequest(
            resourceSpans = listOf(
                ResourceSpans(
                    resource = Resource(attributes = emptyMap()),
                    scopeSpans = listOf(
                        ScopeSpans(
                            scope = InstrumentationScope(name = "test"),
                            spans = listOf(span1, span2)
                        )
                    )
                )
            )
        )
        
        // When: Receive traces
        val response = receiver.receiveOtlpTraces(request)
        
        // Then: Should accept all traces
        assertEquals(0, response.partialSuccess?.rejectedSpans)
        assertEquals(2, storage.count())
    }
    
    @Test
    fun `should return partial success on error`() = runBlocking {
        // Given: Invalid span (missing required attributes)
        val invalidSpan = OtelSpan(
            traceId = "invalid-trace",
            spanId = "invalid-span",
            name = "test",
            startTimeNanos = 0L,
            endTimeNanos = 0L,
            attributes = emptyMap() // Missing required attributes
        )
        
        val request = OtlpExportRequest(
            resourceSpans = listOf(
                ResourceSpans(
                    resource = Resource(attributes = emptyMap()),
                    scopeSpans = listOf(
                        ScopeSpans(
                            scope = InstrumentationScope(name = "test"),
                            spans = listOf(invalidSpan)
                        )
                    )
                )
            )
        )
        
        // When: Receive traces
        val response = receiver.receiveOtlpTraces(request)
        
        // Then: Should still succeed but may have rejections
        assertNotNull(response.partialSuccess)
    }
    
    @Test
    fun `should get receiver statistics`() = runBlocking {
        // Given: Some stored traces
        val span = createTestSpan("trace-1", "span-1", "file1.ts")
        val request = OtlpExportRequest(
            resourceSpans = listOf(
                ResourceSpans(
                    resource = Resource(attributes = emptyMap()),
                    scopeSpans = listOf(
                        ScopeSpans(
                            scope = InstrumentationScope(name = "test"),
                            spans = listOf(span)
                        )
                    )
                )
            )
        )
        receiver.receiveOtlpTraces(request)
        
        // When: Get statistics
        val stats = receiver.getStatistics()
        
        // Then: Should return correct stats
        assertEquals(1, stats.totalTraces)
        assertEquals("in-memory", stats.storageType)
    }
    
    private fun createTestSpan(traceId: String, spanId: String, file: String): OtelSpan {
        return OtelSpan(
            traceId = traceId,
            spanId = spanId,
            name = "generate_code",
            startTimeNanos = System.nanoTime(),
            endTimeNanos = System.nanoTime(),
            attributes = mapOf(
                OtelSemanticConventions.Code.GENERATION_FILE to file,
                OtelSemanticConventions.Code.CONTRIBUTOR_TYPE to "ai",
                OtelSemanticConventions.Conversation.URL to "https://example.com/conv/$spanId",
                OtelSemanticConventions.Vcs.TYPE to "git",
                OtelSemanticConventions.Vcs.REVISION to "abc123"
            )
        )
    }
}
