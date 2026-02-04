package org.archguard.trace.storage

import kotlinx.coroutines.runBlocking
import org.archguard.trace.model.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class DatabaseTraceStorageTest {
    
    private lateinit var database: org.jetbrains.exposed.sql.Database
    private lateinit var storage: DatabaseTraceStorage
    
    @BeforeEach
    fun setup() {
        // Use in-memory H2 database for testing
        database = createDatabase(DatabaseConfig(
            url = "jdbc:h2:mem:test_${System.currentTimeMillis()};DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        ))
        storage = DatabaseTraceStorage(database)
    }
    
    @AfterEach
    fun cleanup() {
        storage.clear()
    }
    
    @Test
    fun `should store and retrieve trace from database`() = runBlocking {
        // Given: A trace record
        val trace = createTestTrace("trace-1")
        
        // When: Store the trace
        storage.store(trace)
        
        // Then: Should be able to retrieve it
        val retrieved = storage.get("trace-1")
        assertNotNull(retrieved)
        assertEquals("trace-1", retrieved?.id)
        assertEquals("0.1.0", retrieved?.version)
        assertEquals("git", retrieved?.vcs?.type)
        assertEquals("abc123", retrieved?.vcs?.revision)
        assertEquals("cursor", retrieved?.tool?.name)
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
            storage.store(createTestTrace("trace-$i", "2026-02-0${i % 9 + 1}T10:00:00Z"))
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
    fun `should count traces correctly`() = runBlocking {
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
    fun `should delete trace from database`() = runBlocking {
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
    fun `should find traces by VCS revision`() = runBlocking {
        // Given: Traces with different revisions
        storage.store(createTestTrace("trace-1", revision = "rev-a"))
        storage.store(createTestTrace("trace-2", revision = "rev-b"))
        storage.store(createTestTrace("trace-3", revision = "rev-a"))
        
        // When: Find by revision
        val found = storage.findByRevision("rev-a")
        
        // Then: Should find matching traces
        assertEquals(2, found.size)
        assertTrue(found.all { it.vcs.revision == "rev-a" })
    }
    
    @Test
    fun `should find traces by tool name`() = runBlocking {
        // Given: Traces with different tools
        storage.store(createTestTrace("trace-1", toolName = "cursor"))
        storage.store(createTestTrace("trace-2", toolName = "windsurf"))
        storage.store(createTestTrace("trace-3", toolName = "cursor"))
        
        // When: Find by tool
        val found = storage.findByTool("cursor")
        
        // Then: Should find matching traces
        assertEquals(2, found.size)
        assertTrue(found.all { it.tool.name == "cursor" })
    }
    
    @Test
    fun `should find traces by time range`() = runBlocking {
        // Given: Traces with different timestamps
        storage.store(createTestTrace("trace-1", "2026-01-01T10:00:00Z"))
        storage.store(createTestTrace("trace-2", "2026-02-01T10:00:00Z"))
        storage.store(createTestTrace("trace-3", "2026-03-01T10:00:00Z"))
        
        // When: Find by time range
        val found = storage.findByTimeRange(
            startTime = Instant.parse("2026-01-15T00:00:00Z"),
            endTime = Instant.parse("2026-02-15T00:00:00Z")
        )
        
        // Then: Should find traces within range
        assertEquals(1, found.size)
        assertEquals("trace-2", found[0].id)
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
    fun `should persist data across storage instances`() = runBlocking {
        // Given: Store trace in first instance
        storage.store(createTestTrace("trace-1"))
        
        // When: Create new storage instance with same database
        val newStorage = DatabaseTraceStorage(database)
        
        // Then: Should still be able to retrieve the trace
        val retrieved = newStorage.get("trace-1")
        assertNotNull(retrieved)
        assertEquals("trace-1", retrieved?.id)
    }
    
    @Test
    fun `should handle complex trace records`() = runBlocking {
        // Given: Complex trace with multiple files and conversations
        val trace = TraceRecord(
            version = "0.1.0",
            id = "complex-trace",
            timestamp = "2026-02-04T14:30:00Z",
            vcs = VcsInfo(
                type = "git",
                revision = "abc123",
                repository = "github.com/test/repo",
                branch = "main"
            ),
            tool = ToolInfo(
                name = "cursor",
                version = "2.4.0",
                metadata = mapOf("extra" to "data")
            ),
            files = listOf(
                TraceFile(
                    path = "file1.ts",
                    conversations = listOf(
                        Conversation(
                            url = "https://example.com/conv/1",
                            contributor = Contributor(
                                type = "ai",
                                modelId = "anthropic/claude-opus",
                                confidence = 0.95
                            ),
                            ranges = listOf(
                                Range(
                                    startLine = 1,
                                    endLine = 10,
                                    contentHash = "hash1"
                                ),
                                Range(
                                    startLine = 20,
                                    endLine = 30,
                                    contentHash = "hash2"
                                )
                            ),
                            related = listOf(
                                RelatedResource(
                                    type = "session",
                                    url = "https://example.com/session/1"
                                )
                            ),
                            metadata = mapOf("custom" to "value")
                        )
                    )
                )
            ),
            metadata = mapOf("project" to "test")
        )
        
        // When: Store and retrieve
        storage.store(trace)
        val retrieved = storage.get("complex-trace")
        
        // Then: Should preserve all data
        assertNotNull(retrieved)
        assertEquals(trace.files.size, retrieved?.files?.size)
        assertEquals(trace.files[0].conversations.size, retrieved?.files?.get(0)?.conversations?.size)
        assertEquals(trace.files[0].conversations[0].ranges.size, 
                     retrieved?.files?.get(0)?.conversations?.get(0)?.ranges?.size)
        assertEquals(trace.metadata["project"], retrieved?.metadata?.get("project"))
    }
    
    @Test
    fun `should return database as storage type`() {
        // When: Get storage type
        val type = storage.type()
        
        // Then: Should be database
        assertEquals("database", type)
    }
    
    private fun createTestTrace(
        id: String,
        timestamp: String = "2026-02-04T14:30:00Z",
        revision: String = "abc123",
        toolName: String = "cursor"
    ): TraceRecord {
        return TraceRecord(
            version = "0.1.0",
            id = id,
            timestamp = timestamp,
            vcs = VcsInfo(
                type = "git",
                revision = revision
            ),
            tool = ToolInfo(
                name = toolName,
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
