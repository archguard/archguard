package org.archguard.trace

import mu.KotlinLogging
import org.archguard.trace.converter.AgentTraceToOtelConverter
import org.archguard.trace.converter.OtelToAgentTraceConverter

private val logger = KotlinLogging.logger {}

/**
 * Main entry point for Agent Trace Server
 * 
 * This is a placeholder for future HTTP server implementation
 * that will accept OTEL traces and convert them to Agent Trace format.
 */
fun main(args: Array<String>) {
    logger.info { "Agent Trace Server starting..." }
    logger.info { "OpenTelemetry to Agent Trace converter initialized" }
    
    // Initialize converters
    val otelToAgent = OtelToAgentTraceConverter()
    val agentToOtel = AgentTraceToOtelConverter()
    
    logger.info { "Converters initialized successfully" }
    logger.info { "Phase 1 implementation complete: Models, Converters, and Tests" }
    logger.info { "Next phase: Implement HTTP endpoints to receive OTEL traces" }
    
    // Future: Start HTTP server for OTLP ingestion
    // Future: Implement REST API endpoints
    // Future: Integrate with ArchGuard backend
}
