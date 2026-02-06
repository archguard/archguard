package org.archguard.trace

import mu.KotlinLogging
import org.archguard.trace.server.TraceServer
import org.archguard.trace.server.formatForLogging
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
    
    val httpPort = args.getOrNull(0)?.toIntOrNull() ?: 4318
    val grpcPort = args.getOrNull(1)?.toIntOrNull() ?: 4317
    val host = args.getOrNull(2) ?: "0.0.0.0"
    
    val storage = InMemoryTraceStorage()
    val server = TraceServer(storage, httpPort, grpcPort, host)

    logger.info { "Starting server on $host:$httpPort (HTTP), $host:$grpcPort (gRPC)" }
    logger.info { "Phase 1 ✅: Models, Converters, and Tests" }
    logger.info { "Phase 2 ✅: OTEL Receiver, Storage, and HTTP endpoints" }
    logger.info { "" }

    // Start server (non-blocking to allow route introspection)
    server.start(wait = false)

    // Log all registered endpoints dynamically
    logger.info { "Registered HTTP Endpoints:" }
    val routes = server.getRegisteredRoutes()
    routes.formatForLogging().forEach { route ->
        logger.info { route }
    }
    logger.info { "" }

    // Wait for server to complete
    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting down Agent Trace Server..." }
        server.stop()
    })

    // Keep the main thread alive
    Thread.currentThread().join()
}
