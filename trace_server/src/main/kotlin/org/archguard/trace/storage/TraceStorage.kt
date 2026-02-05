package org.archguard.trace.storage

import org.archguard.trace.model.TraceRecord

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

