package org.archguard.trace.server

import io.grpc.Server
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.archguard.trace.converter.AgentTraceToOtelConverter
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.model.TelemetryListResponse
import org.archguard.trace.model.TraceRecord
import org.archguard.trace.receiver.TelemetryCounters
import org.archguard.trace.receiver.ReceiverStatistics
import org.archguard.trace.receiver.OtlpGrpcReceiver
import org.archguard.trace.receiver.OtlpGrpcLogsReceiver
import org.archguard.trace.receiver.OtlpGrpcMetricsReceiver
import org.archguard.trace.receiver.OtelTraceReceiver
import org.archguard.trace.receiver.handleOtlpRequest
import org.archguard.trace.storage.DatabaseTraceStorage
import org.archguard.trace.storage.InMemoryTelemetryStorage
import org.archguard.trace.storage.InMemoryTraceStorage
import org.archguard.trace.storage.TelemetryStorage
import org.archguard.trace.storage.TraceStorage
import java.io.IOException
import java.time.Instant
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

/**
 * Response models for API endpoints
 */
@Serializable
data class TracesListResponse(
    val traces: List<TraceRecord>,
    val offset: Int,
    val limit: Int,
    val total: Long,
    val filters: TraceFilters
)

@Serializable
data class TraceFilters(
    val revision: String? = null,
    val tool: String? = null,
    val startTime: String? = null,
    val endTime: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class HealthResponse(
    val status: String,
    val service: String
)

@Serializable
data class OtelExportResponse(
    val traceId: String?,
    val spans: List<org.archguard.trace.model.OtelSpan>
)

/**
 * Agent Trace Server
 * 
 * HTTP server that provides OTLP-compatible endpoints for receiving
 * OpenTelemetry traces and converting them to Agent Trace format.
 */
class TraceServer(
    private val storage: TraceStorage = InMemoryTraceStorage(),
    private val port: Int = 4318,
    private val grpcPort: Int = 4317,
    private val host: String = "0.0.0.0",
    private val telemetryStorage: TelemetryStorage = InMemoryTelemetryStorage()
) {
    
    private val otelToAgentConverter = OtelToAgentTraceConverter()
    private val agentToOtelConverter = AgentTraceToOtelConverter()
    private val receiver = OtelTraceReceiver(otelToAgentConverter, storage)
    private val counters = TelemetryCounters()
    private val grpcScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var server: NettyApplicationEngine
    private var grpcServer: Server? = null
    private var routing: Routing? = null
    
    /**
     * Start the server
     */
    fun start(wait: Boolean = false) {
        logger.info { "Starting Agent Trace Server on $host:$port" }

        // Start OTLP gRPC receiver (default OTEL SDKs: 4317)
        grpcServer = try {
            NettyServerBuilder
                .forPort(grpcPort)
                .addService(OtlpGrpcReceiver(otelToAgentConverter, storage, counters, grpcScope))
                .addService(OtlpGrpcMetricsReceiver(counters, telemetryStorage, grpcScope))
                .addService(OtlpGrpcLogsReceiver(counters, telemetryStorage, grpcScope))
                .build()
                .start()
        } catch (e: IOException) {
            logger.error(e) { "Failed to start OTLP gRPC server on port $grpcPort (port already in use?)" }
            throw e
        }
        logger.info { "OTLP gRPC endpoint: $host:$grpcPort (metrics/logs/traces)" }
        
        server = embeddedServer(Netty, port = port, host = host) {
            configureServer()
        }
        
        server.start(wait = wait)
        
        logger.info { "Agent Trace Server started successfully" }
        logger.info { "OTLP HTTP endpoint: http://$host:$port/v1/traces" }
    }
    
    /**
     * Get all registered HTTP routes
     */
    fun getRegisteredRoutes(): List<RegisteredRoute> {
        return routing?.getAllRoutes() ?: emptyList()
    }

    /**
     * Stop the server
     */
    fun stop() {
        logger.info { "Stopping Agent Trace Server" }
        grpcServer?.let { s ->
            s.shutdown()
            val ok = runCatching { s.awaitTermination(5, TimeUnit.SECONDS) }.getOrDefault(true)
            if (!ok) {
                logger.warn { "gRPC server did not stop in time; forcing shutdown" }
                s.shutdownNow()
                runCatching { s.awaitTermination(5, TimeUnit.SECONDS) }
            }
        }
        grpcServer = null
        grpcScope.cancel("TraceServer stopping")
        server.stop(1000, 5000)
    }
    
    /**
     * Configure Ktor application
     */
    private fun Application.configureServer() {
        // CORS - keep safe by default (no credentials + restricted origins).
        // If you really need wide-open CORS for local demos, set:
        //   ARCHGUARD_TRACE_CORS_ANY_HOST=true
        val corsAnyHost = System.getenv("ARCHGUARD_TRACE_CORS_ANY_HOST")
            ?.toBooleanStrictOrNull() == true
        install(CORS) {
            if (corsAnyHost) {
                anyHost()
            } else {
                allowHost("localhost", schemes = listOf("http", "https"))
                allowHost("127.0.0.1", schemes = listOf("http", "https"))
            }
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.Accept)
            allowHeader("X-Requested-With")
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Options)
            // No cookies/auth sessions for this service; keep CORS non-credentialed by default.
            allowCredentials = false
            maxAgeInSeconds = 3600
        }

        // Request logging (critical for diagnosing OTLP clients)
        install(CallLogging) {
            // keep default level; narrow to OTLP and API endpoints
            filter { call: ApplicationCall ->
                val p = call.request.path()
                p.startsWith("/v1/") || p.startsWith("/api/") || p == "/health"
            }
            format { call: ApplicationCall ->
                val ct = call.request.contentType().toString()
                val len = call.request.headers["Content-Length"] ?: "-"
                val ua = call.request.userAgent() ?: "-"
                val remote = call.request.local.remoteHost
                "${call.request.httpMethod.value} ${call.request.path()} ct=$ct len=$len remote=$remote ua=\"$ua\""
            }
        }

        // JSON serialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        
        routing {
            configureRoutes()
            // Save routing reference for introspection
            routing = this
        }
    }
    
    /**
     * Configure routes
     */
    private fun Routing.configureRoutes() {
        // Health check
        get("/health") {
            call.respond(
                HealthResponse(
                    status = "healthy",
                    service = "agent-trace-server"
                )
            )
        }
        
        // OTLP HTTP endpoint for traces (supports both JSON and Protobuf)
        post("/v1/traces") {
            counters.otlpHttpTraceRequests.incrementAndGet()
            handleOtlpRequest(otelToAgentConverter, storage)
        }

        // OTLP HTTP endpoints for Claude Code telemetry (JSON and protobuf)
        installOtlpHttpTelemetryRoutes(telemetryStorage, counters)
        
        // Get trace by ID
        get("/api/traces/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = "Missing trace ID")
            )
            
            val trace = storage.get(id)
            if (trace != null) {
                call.respond(trace)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(error = "Trace not found"))
            }
        }
        
        // List all traces with filtering
        get("/api/traces") {
            val paging = call.requireOffsetLimit(defaultLimit = 100) ?: return@get
            val offset = paging.first
            val limit = paging.second
            val revision = call.request.queryParameters["revision"]
            val tool = call.request.queryParameters["tool"]
            val startTime = call.request.queryParameters["start_time"]
            val endTime = call.request.queryParameters["end_time"]

            val startInstant = if (startTime != null) {
                runCatching { Instant.parse(startTime) }.getOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Invalid start_time"))
            } else null
            val endInstant = if (endTime != null) {
                runCatching { Instant.parse(endTime) }.getOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Invalid end_time"))
            } else null
            
            val traces = when {
                revision != null && storage is DatabaseTraceStorage -> {
                    storage.findByRevision(revision, offset, limit)
                }
                tool != null && storage is DatabaseTraceStorage -> {
                    storage.findByTool(tool, offset, limit)
                }
                startInstant != null && endInstant != null && storage is DatabaseTraceStorage -> {
                    storage.findByTimeRange(
                        startInstant,
                        endInstant,
                        offset,
                        limit
                    )
                }
                else -> storage.list(offset, limit)
            }
            
            call.respond(
                TracesListResponse(
                    traces = traces,
                    offset = offset,
                    limit = limit,
                    total = storage.count(),
                    filters = TraceFilters(
                        revision = revision,
                        tool = tool,
                        startTime = startTime,
                        endTime = endTime
                    )
                )
            )
        }
        
        // Get statistics
        get("/api/stats") {
            call.respond(
                ReceiverStatistics(
                    totalTraces = storage.count(),
                    storageType = storage.type(),
                    otlpGrpcMetricsRequests = counters.otlpGrpcMetricsRequests.get(),
                    otlpGrpcLogsRequests = counters.otlpGrpcLogsRequests.get(),
                    otlpGrpcTraceRequests = counters.otlpGrpcTraceRequests.get(),
                    otlpHttpTraceRequests = counters.otlpHttpTraceRequests.get(),
                    otlpHttpMetricsRequests = counters.otlpHttpMetricsRequests.get(),
                    otlpHttpLogsRequests = counters.otlpHttpLogsRequests.get(),
                    telemetryLogsStored = telemetryStorage.logsCount(),
                    telemetryMetricsStored = telemetryStorage.metricsCount()
                )
            )
        }

        // Claude Code telemetry APIs (OTLP metrics + logs/events)
        get("/api/telemetry/logs") {
            val paging = call.requireOffsetLimit(defaultLimit = 100) ?: return@get
            val offset = paging.first
            val limit = paging.second
            val eventName = call.request.queryParameters["event_name"]

            val items = telemetryStorage.listLogs(offset, limit, eventName)
            call.respond(
                TelemetryListResponse(
                    items = items,
                    offset = offset,
                    limit = limit,
                    total = telemetryStorage.logsCount()
                )
            )
        }

        get("/api/telemetry/metrics") {
            val paging = call.requireOffsetLimit(defaultLimit = 100) ?: return@get
            val offset = paging.first
            val limit = paging.second
            val metricName = call.request.queryParameters["metric_name"]

            val items = telemetryStorage.listMetrics(offset, limit, metricName)
            call.respond(
                TelemetryListResponse(
                    items = items,
                    offset = offset,
                    limit = limit,
                    total = telemetryStorage.metricsCount()
                )
            )
        }
        
        // Export trace as OTEL format
        get("/api/traces/{id}/otel") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = "Missing trace ID")
            )
            
            val trace = storage.get(id)
            if (trace != null) {
                val otelSpans = agentToOtelConverter.convert(trace)
                call.respond(
                    OtelExportResponse(
                        traceId = otelSpans.firstOrNull()?.traceId,
                        spans = otelSpans
                    )
                )
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(error = "Trace not found"))
            }
        }
        
        // Delete trace
        delete("/api/traces/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = "Missing trace ID")
            )
            
            val deleted = storage.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(error = "Trace not found"))
            }
        }
    }
}

private suspend fun ApplicationCall.requireOffsetLimit(
    defaultOffset: Int = 0,
    defaultLimit: Int = 100,
    maxLimit: Int = 5000
): Pair<Int, Int>? {
    val offsetRaw = request.queryParameters["offset"]
    val limitRaw = request.queryParameters["limit"]

    val offset = when {
        offsetRaw == null -> defaultOffset
        offsetRaw.toIntOrNull() == null -> {
            respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Invalid offset"))
            return null
        }
        else -> offsetRaw.toInt()
    }
    if (offset < 0) {
        respond(HttpStatusCode.BadRequest, ErrorResponse(error = "offset must be >= 0"))
        return null
    }

    val limit = when {
        limitRaw == null -> defaultLimit
        limitRaw.toIntOrNull() == null -> {
            respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Invalid limit"))
            return null
        }
        else -> limitRaw.toInt()
    }
    if (limit <= 0 || limit > maxLimit) {
        respond(HttpStatusCode.BadRequest, ErrorResponse(error = "limit must be in 1..$maxLimit"))
        return null
    }

    return offset to limit
}

/**
 * Start server from command line
 */
fun main(args: Array<String>) {
    val httpPort = args.getOrNull(0)?.toIntOrNull() ?: 4318
    val grpcPort = args.getOrNull(1)?.toIntOrNull() ?: 4317
    val storage = InMemoryTraceStorage()
    
    val server = TraceServer(storage, httpPort, grpcPort)
    server.start(wait = true)
}
