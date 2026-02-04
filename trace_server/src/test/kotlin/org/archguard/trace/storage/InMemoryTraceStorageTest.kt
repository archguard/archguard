package org.archguard.trace.storage

import kotlinx.coroutines.runBlocking
import org.archguard.trace.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryTraceStorageTest {
    
    private lateinit var storage: InMemoryTraceStorage
    
    @BeforeEach
    fun setup() {
        storage = InMemoryTraceStorage()
    }
    
    @Test
    fun `should store and retrieve trace`() = runBlocking {
        // Given: A trace record
        val trace = createTestTrace("trace-1")
        
        // When: Store the trace
        storage.store(trace)
        
        // Then: Should be able to retrieve it
        val retrieved = storage.get("trace-1")
        assertNotNull(retrieved)
        assertEquals("trace-1", retrieved?.id)
        assertEquals("0.1.0", retrieved?.version)
    }
    
    @Test
    fun `should return null for non-existent trace`() = runBlocking {
        // When: Get non-existent trace
        val retrieved = storage.get("non-existent")
        
        // Then: Should return null
        assertNull(retrieved)
    }
    
    @Test
    fun `should list traces with pagination`() = runBlocking {
        // Given: Multiple traces
        repeat(10) { i ->
            storage.store(createTestTrace("trace-$i"))
        }
        
        // When: List with pagination
        val firstPage = storage.list(offset = 0, limit = 5)
        val secondPage = storage.list(offset = 5, limit = 5)
        
        // Then: Should return correct pages
        assertEquals(5, firstPage.size)
        assertEquals(5, secondPage.size)
        
        // And: Should not overlap
        val firstIds = firstPage.map { it.id }.toSet()
        val secondIds = secondPage.map { it.id }.toSet()
        assertTrue(firstIds.intersect(secondIds).isEmpty())
    }
    
    @Test
    fun `should count traces`() = runBlocking {
        // Given: Some traces
        storage.store(createTestTrace("trace-1"))
        storage.store(createTestTrace("trace-2"))
        storage.store(createTestTrace("trace-3"))
        
        // When: Count traces
        val count = storage.count()
        
        // Then: Should return correct count
        assertEquals(3, count)
    }
    
    @Test
    fun `should delete trace`() = runBlocking {
        // Given: A stored trace
        storage.store(createTestTrace("trace-1"))
        assertEquals(1, storage.count())
        
        // When: Delete the trace
        val deleted = storage.delete("trace-1")
        
        // Then: Should be deleted
        assertTrue(deleted)
        assertEquals(0, storage.count())
        assertNull(storage.get("trace-1"))
    }
    
    @Test
    fun `should return false when deleting non-existent trace`() = runBlocking {
        // When: Delete non-existent trace
        val deleted = storage.delete("non-existent")
        
        // Then: Should return false
        assertFalse(deleted)
    }
    
    @Test
    fun `should clear all traces`() = runBlocking {
        // Given: Some traces
        storage.store(createTestTrace("trace-1"))
        storage.store(createTestTrace("trace-2"))
        assertEquals(2, storage.count())
        
        // When: Clear storage
        storage.clear()
        
        // Then: Should be empty
        assertEquals(0, storage.count())
    }
    
    @Test
    fun `should update existing trace`() = runBlocking {
        // Given: A stored trace
        val trace1 = createTestTrace("trace-1")
        storage.store(trace1)
        
        // When: Store another trace with same ID but different data
        val trace2 = trace1.copy(
            vcs = trace1.vcs.copy(revision = "updated-revision")
        )
        storage.store(trace2)
        
        // Then: Should update the trace
        assertEquals(1, storage.count())
        val retrieved = storage.get("trace-1")
        assertEquals("updated-revision", retrieved?.vcs?.revision)
    }
    
    @Test
    fun `should maintain order by timestamp desc`() = runBlocking {
        // Given: Traces with different timestamps
        storage.store(createTestTrace("trace-1", "2026-01-01T10:00:00Z"))
        storage.store(createTestTrace("trace-2", "2026-01-03T10:00:00Z"))
        storage.store(createTestTrace("trace-3", "2026-01-02T10:00:00Z"))
        
        // When: List traces
        val traces = storage.list()
        
        // Then: Should be ordered by timestamp descending
        assertEquals("trace-2", traces[0].id) // 2026-01-03 (newest)
        assertEquals("trace-3", traces[1].id) // 2026-01-02
        assertEquals("trace-1", traces[2].id) // 2026-01-01 (oldest)
    }
    
    private fun createTestTrace(
        id: String, 
        timestamp: String = "2026-02-04T14:30:00Z"
    ): TraceRecord {
        return TraceRecord(
            version = "0.1.0",
            id = id,
            timestamp = timestamp,
            vcs = VcsInfo(
                type = "git",
                revision = "abc123"
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
                            url = "https://example.com/conv/123",
                            contributor = Contributor(
                                type = "ai",
                                modelId = "anthropic/claude-opus"
                            ),
                            ranges = emptyList()
                        )
                    )
                )
            )
        )
    }
}
