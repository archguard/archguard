package org.archguard.trace.storage

import org.archguard.trace.model.TraceRecord
import java.util.concurrent.ConcurrentHashMap

/**
 * Trace Storage Interface
 * 
 * Abstraction for storing and retrieving Agent Trace records.
 */
interface TraceStorage {
    /**
     * Store a trace record
     */
    suspend fun store(record: TraceRecord)
    
    /**
     * Retrieve a trace record by ID
     */
    suspend fun get(id: String): TraceRecord?
    
    /**
     * List all trace records (with pagination)
     */
    suspend fun list(offset: Int = 0, limit: Int = 100): List<TraceRecord>
    
    /**
     * Count total traces
     */
    fun count(): Long
    
    /**
     * Delete a trace record
     */
    suspend fun delete(id: String): Boolean
    
    /**
     * Storage type identifier
     */
    fun type(): String
}

/**
 * In-Memory Trace Storage
 * 
 * Simple concurrent hash map storage for development and testing.
 * Not suitable for production use.
 */
class InMemoryTraceStorage : TraceStorage {
    private val traces = ConcurrentHashMap<String, TraceRecord>()
    
    override suspend fun store(record: TraceRecord) {
        traces[record.id] = record
    }
    
    override suspend fun get(id: String): TraceRecord? {
        return traces[id]
    }
    
    override suspend fun list(offset: Int, limit: Int): List<TraceRecord> {
        return traces.values
            .sortedByDescending { it.timestamp }
            .drop(offset)
            .take(limit)
    }
    
    override fun count(): Long {
        return traces.size.toLong()
    }
    
    override suspend fun delete(id: String): Boolean {
        return traces.remove(id) != null
    }
    
    override fun type(): String = "in-memory"
    
    /**
     * Clear all traces (for testing)
     */
    fun clear() {
        traces.clear()
    }
}

/**
 * File System Trace Storage
 * 
 * Stores traces as JSON files in a directory structure.
 */
class FileSystemTraceStorage(
    private val baseDir: String = ".agent-trace"
) : TraceStorage {
    
    override suspend fun store(record: TraceRecord) {
        TODO("Implement file system storage")
    }
    
    override suspend fun get(id: String): TraceRecord? {
        TODO("Implement file system retrieval")
    }
    
    override suspend fun list(offset: Int, limit: Int): List<TraceRecord> {
        TODO("Implement file system listing")
    }
    
    override fun count(): Long {
        TODO("Implement file system counting")
    }
    
    override suspend fun delete(id: String): Boolean {
        TODO("Implement file system deletion")
    }
    
    override fun type(): String = "filesystem"
}
