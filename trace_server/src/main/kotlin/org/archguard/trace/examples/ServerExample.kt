package org.archguard.trace.examples

import kotlinx.coroutines.runBlocking
import org.archguard.trace.model.*
import org.archguard.trace.receiver.*
import org.archguard.trace.server.TraceServer
import org.archguard.trace.storage.InMemoryTraceStorage

/**
 * Example usage of Agent Trace Server (Phase 2)
 */
object ServerExample {
    
    /**
     * Example: Start trace server and send OTLP traces
     */
    fun startServerExample() {
        println("=== Agent Trace Server Example ===\n")
        
        // Create storage
        val storage = InMemoryTraceStorage()
        
        // Create and start server
        val server = TraceServer(storage, port = 4318)
        
        println("Starting server on http://localhost:4318")
        println("Endpoints:")
        println("  POST   /v1/traces          - OTLP trace ingestion")
        println("  GET    /api/traces         - List all traces")
        println("  GET    /api/traces/{id}    - Get trace by ID")
        println("  GET    /api/traces/{id}/otel - Export as OTEL")
        println("  DELETE /api/traces/{id}    - Delete trace")
        println("  GET    /api/stats          - Statistics")
        println("  GET    /health             - Health check")
        println()
        
        // Start server (non-blocking)
        server.start(wait = false)
        
        println("Server started successfully!")
        println("Send OTLP traces to: http://localhost:4318/v1/traces")
        println()
        
        // Keep server running
        println("Press Enter to stop server...")
        readLine()
        
        server.stop()
        println("Server stopped.")
    }
    
    /**
     * Example: Programmatically send traces to receiver
     */
    fun sendTracesExample() = runBlocking {
        println("=== Sending OTLP Traces Example ===\n")
        
        // Create components
        val storage = InMemoryTraceStorage()
        val receiver = org.archguard.trace.receiver.OtelTraceReceiver(
            org.archguard.trace.converter.OtelToAgentTraceConverter(),
            storage
        )
        
        // Create OTLP request
        val traceId = "4bf92f3577b34da6a3ce929d0e0e4736"
        val span = OtelSpan(
            traceId = traceId,
            spanId = "00f067aa0ba902b7",
            name = "generate_code",
            startTimeNanos = System.nanoTime(),
            endTimeNanos = System.nanoTime(),
            attributes = mapOf(
                OtelSemanticConventions.GenAI.SYSTEM to "anthropic",
                OtelSemanticConventions.GenAI.REQUEST_MODEL to "anthropic/claude-opus-4-5-20251101",
                OtelSemanticConventions.Code.CONTRIBUTOR_TYPE to "ai",
                OtelSemanticConventions.Code.GENERATION_FILE to "src/example.ts",
                OtelSemanticConventions.Conversation.URL to "https://api.cursor.com/v1/conversations/12345",
                OtelSemanticConventions.Vcs.TYPE to "git",
                OtelSemanticConventions.Vcs.REVISION to "abc123def456"
            ),
            events = listOf(
                SpanEvent(
                    timeNanos = System.nanoTime(),
                    name = OtelSemanticConventions.SpanNames.CODE_RANGE_GENERATED,
                    attributes = mapOf(
                        OtelSemanticConventions.Code.RANGE_START to "10",
                        OtelSemanticConventions.Code.RANGE_END to "30",
                        OtelSemanticConventions.Code.RANGE_HASH to "murmur3:abc123"
                    )
                )
            )
        )
        
        val request = OtlpExportRequest(
            resourceSpans = listOf(
                ResourceSpans(
                    resource = Resource(
                        attributes = mapOf(
                            "service.name" to "cursor-ai-agent",
                            "service.version" to "2.4.0"
                        )
                    ),
                    scopeSpans = listOf(
                        ScopeSpans(
                            scope = InstrumentationScope(
                                name = "cursor.code_generation",
                                version = "1.0.0"
                            ),
                            spans = listOf(span)
                        )
                    )
                )
            )
        )
        
        println("Sending OTLP request...")
        println("  Trace ID: $traceId")
        println("  Spans: ${request.resourceSpans.sumOf { it.scopeSpans.sumOf { scope -> scope.spans.size } }}")
        println()
        
        // Send traces
        val response = receiver.receiveOtlpTraces(request)
        
        println("Response:")
        println("  Rejected spans: ${response.partialSuccess?.rejectedSpans ?: 0}")
        println("  Error: ${response.partialSuccess?.errorMessage ?: "None"}")
        println()
        
        // Get statistics
        val stats = receiver.getStatistics()
        println("Statistics:")
        println("  Total traces: ${stats.totalTraces}")
        println("  Storage type: ${stats.storageType}")
        println()
        
        // Retrieve stored trace
        val traces = storage.list()
        println("Stored traces: ${traces.size}")
        traces.forEach { trace ->
            println("  - ID: ${trace.id}")
            println("    VCS: ${trace.vcs.type} @ ${trace.vcs.revision}")
            println("    Files: ${trace.files.size}")
            trace.files.forEach { file ->
                println("      - ${file.path}: ${file.conversations.size} conversations")
            }
        }
    }
    
    /**
     * Example: Storage operations
     */
    fun storageExample() = runBlocking {
        println("=== Storage Operations Example ===\n")
        
        val storage = InMemoryTraceStorage()
        
        // Create and store traces
        println("Creating traces...")
        repeat(5) { i ->
            val trace = TraceRecord(
                version = "0.1.0",
                id = "trace-$i",
                timestamp = "2026-02-04T${14 + i}:00:00Z",
                vcs = VcsInfo(
                    type = "git",
                    revision = "commit-$i"
                ),
                tool = ToolInfo(
                    name = "cursor",
                    version = "2.4.0"
                ),
                files = listOf(
                    TraceFile(
                        path = "src/file-$i.ts",
                        conversations = listOf(
                            Conversation(
                                url = "https://example.com/conv/$i",
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
            storage.store(trace)
            println("  Stored: ${trace.id}")
        }
        println()
        
        // Count
        println("Total traces: ${storage.count()}")
        println()
        
        // List with pagination
        println("First page (limit 3):")
        val firstPage = storage.list(offset = 0, limit = 3)
        firstPage.forEach { trace ->
            println("  - ${trace.id} (${trace.timestamp})")
        }
        println()
        
        println("Second page (offset 3, limit 3):")
        val secondPage = storage.list(offset = 3, limit = 3)
        secondPage.forEach { trace ->
            println("  - ${trace.id} (${trace.timestamp})")
        }
        println()
        
        // Get by ID
        println("Get trace-2:")
        val trace = storage.get("trace-2")
        if (trace != null) {
            println("  ID: ${trace.id}")
            println("  VCS: ${trace.vcs.type} @ ${trace.vcs.revision}")
            println("  Files: ${trace.files.size}")
        }
        println()
        
        // Delete
        println("Deleting trace-2...")
        val deleted = storage.delete("trace-2")
        println("  Deleted: $deleted")
        println("  Remaining traces: ${storage.count()}")
        println()
    }
}

/**
 * Run server example
 */
fun main(args: Array<String>) {
    when (args.getOrNull(0)) {
        "server" -> ServerExample.startServerExample()
        "send" -> ServerExample.sendTracesExample()
        "storage" -> ServerExample.storageExample()
        else -> {
            println("Usage:")
            println("  server  - Start trace server")
            println("  send    - Send traces to receiver")
            println("  storage - Storage operations")
            println()
            println("Running all examples...")
            println()
            
            ServerExample.sendTracesExample()
            println("=" .repeat(60))
            println()
            
            ServerExample.storageExample()
        }
    }
}
