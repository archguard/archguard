package org.archguard.trace.server

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.archguard.trace.converter.AgentTraceToOtelConverter
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.receiver.OtelTraceReceiver
import org.archguard.trace.receiver.OtlpExportRequest
import org.archguard.trace.storage.InMemoryTraceStorage
import org.archguard.trace.storage.TraceStorage

private val logger = KotlinLogging.logger {}

/**
 * Agent Trace Server
 * 
 * HTTP server that provides OTLP-compatible endpoints for receiving
 * OpenTelemetry traces and converting them to Agent Trace format.
 */
class TraceServer(
    private val storage: TraceStorage = InMemoryTraceStorage(),
    private val port: Int = 4318,
    private val host: String = "0.0.0.0"
) {
    
    private val otelToAgentConverter = OtelToAgentTraceConverter()
    private val agentToOtelConverter = AgentTraceToOtelConverter()
    private val receiver = OtelTraceReceiver(otelToAgentConverter, storage)
    
    private lateinit var server: NettyApplicationEngine
    
    /**
     * Start the server
     */
    fun start(wait: Boolean = false) {
        logger.info { "Starting Agent Trace Server on $host:$port" }
        
        server = embeddedServer(Netty, port = port, host = host) {
            configureServer()
        }
        
        server.start(wait = wait)
        
        logger.info { "Agent Trace Server started successfully" }
        logger.info { "OTLP HTTP endpoint: http://$host:$port/v1/traces" }
    }
    
    /**
     * Stop the server
     */
    fun stop() {
        logger.info { "Stopping Agent Trace Server" }
        server.stop(1000, 5000)
    }
    
    /**
     * Configure Ktor application
     */
    private fun Application.configureServer() {
        // JSON serialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        routing {
            configureRoutes()
        }
    }
    
    /**
     * Configure routes
     */
    private fun Routing.configureRoutes() {
        // Health check
        get("/health") {
            call.respond(
                mapOf(
                    "status" to "healthy",
                    "service" to "agent-trace-server"
                )
            )
        }
        
        // OTLP HTTP endpoint for traces
        post("/v1/traces") {
            try {
                val request = call.receive<OtlpExportRequest>()
                val response = receiver.receiveOtlpTraces(request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                logger.error(e) { "Failed to process OTLP request: ${e.message}" }
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error"))
                )
            }
        }
        
        // Get trace by ID
        get("/api/traces/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing trace ID")
            )
            
            val trace = storage.get(id)
            if (trace != null) {
                call.respond(trace)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Trace not found"))
            }
        }
        
        // List all traces
        get("/api/traces") {
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
            
            val traces = storage.list(offset, limit)
            call.respond(
                mapOf(
                    "traces" to traces,
                    "offset" to offset,
                    "limit" to limit,
                    "total" to storage.count()
                )
            )
        }
        
        // Get statistics
        get("/api/stats") {
            val stats = receiver.getStatistics()
            call.respond(stats)
        }
        
        // Export trace as OTEL format
        get("/api/traces/{id}/otel") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing trace ID")
            )
            
            val trace = storage.get(id)
            if (trace != null) {
                val otelSpans = agentToOtelConverter.convert(trace)
                call.respond(
                    mapOf(
                        "traceId" to otelSpans.firstOrNull()?.traceId,
                        "spans" to otelSpans
                    )
                )
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Trace not found"))
            }
        }
        
        // Delete trace
        delete("/api/traces/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing trace ID")
            )
            
            val deleted = storage.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Trace not found"))
            }
        }
    }
}

/**
 * Start server from command line
 */
fun main(args: Array<String>) {
    val port = args.getOrNull(0)?.toIntOrNull() ?: 4318
    val storage = InMemoryTraceStorage()
    
    val server = TraceServer(storage, port)
    server.start(wait = true)
}
