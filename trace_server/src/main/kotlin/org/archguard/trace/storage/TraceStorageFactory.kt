package org.archguard.trace.storage

/**
 * Storage factory
 */
object TraceStorageFactory {
    /**
     * Create storage based on type
     */
    fun create(type: String = "memory"): TraceStorage {
        return when (type.lowercase()) {
            "memory", "in-memory" -> InMemoryTraceStorage()
            else -> InMemoryTraceStorage()
        }
    }
}