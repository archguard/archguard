package org.archguard.trace

import mu.KotlinLogging
import org.archguard.trace.server.TraceServer
import org.archguard.trace.storage.InMemoryTraceStorage

private val logger = KotlinLogging.logger {}

/**
 * Main entry point for Agent Trace Server
 * 
 * Starts an HTTP server that accepts OTEL traces via OTLP protocol
 * and stores them in Agent Trace format.
 */
fun main(args: Array<String>) {
    logger.info { "Agent Trace Server starting..." }
    
    val port = args.getOrNull(0)?.toIntOrNull() ?: 4318
    val host = args.getOrNull(1) ?: "0.0.0.0"
    
    val storage = InMemoryTraceStorage()
    val server = TraceServer(storage, port, host)
    
    logger.info { "Starting server on $host:$port" }
    logger.info { "Phase 1 ✅: Models, Converters, and Tests" }
    logger.info { "Phase 2 ✅: OTEL Receiver, Storage, and HTTP endpoints" }
    logger.info { "" }
    logger.info { "Endpoints:" }
    logger.info { "  POST   /v1/traces          - OTLP trace ingestion (OTEL compatible)" }
    logger.info { "  GET    /api/traces         - List all traces" }
    logger.info { "  GET    /api/traces/{id}    - Get trace by ID" }
    logger.info { "  GET    /api/traces/{id}/otel - Export trace as OTEL format" }
    logger.info { "  DELETE /api/traces/{id}    - Delete trace" }
    logger.info { "  GET    /api/stats          - Get statistics" }
    logger.info { "  GET    /health             - Health check" }
    
    server.start(wait = true)
}
