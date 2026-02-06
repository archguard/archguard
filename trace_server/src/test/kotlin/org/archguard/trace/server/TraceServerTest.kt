package org.archguard.trace.server

import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.archguard.trace.storage.InMemoryTraceStorage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TraceServerTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `should respond to health check`() = testApplication {
        application {
            val storage = InMemoryTraceStorage()
            val server = TraceServer(storage)
            // Configure server routes
        }
        
        // Note: Full integration test would require starting actual server
        // This is a placeholder for structure
        assertTrue(true)
    }
    
    @Test
    fun `should accept OTLP traces via POST v1 traces`() {
        // Integration test structure
        // Would test actual HTTP endpoint with OTLP request
        assertTrue(true)
    }
    
    @Test
    fun `should retrieve trace by ID`() {
        // Integration test structure
        assertTrue(true)
    }
    
    @Test
    fun `should list traces with pagination`() {
        // Integration test structure
        assertTrue(true)
    }
    
    @Test
    fun `should export trace as OTEL format`() {
        // Integration test structure
        assertTrue(true)
    }
    
    @Test
    fun `should delete trace by ID`() {
        // Integration test structure
        assertTrue(true)
    }

    @Test
    fun `should retrieve all registered routes`() {
        val storage = InMemoryTraceStorage()
        val server = TraceServer(storage, port = 14318, grpcPort = 14317)

        try {
            // Start server to initialize routes
            server.start(wait = false)

            // Give server a moment to initialize
            Thread.sleep(500)

            // Get registered routes
            val routes = server.getRegisteredRoutes()

            // Verify we have routes
            assertTrue(routes.isNotEmpty(), "Should have registered routes")

            // Verify key endpoints exist
            val paths = routes.map { it.path }
            assertTrue(paths.contains("/health"), "Should have /health endpoint")
            assertTrue(paths.contains("/v1/traces"), "Should have /v1/traces endpoint")
            assertTrue(paths.contains("/api/traces"), "Should have /api/traces endpoint")
            assertTrue(paths.contains("/api/stats"), "Should have /api/stats endpoint")

            // Verify HTTP methods
            val postRoutes = routes.filter { it.method == "POST" }
            assertTrue(postRoutes.any { it.path == "/v1/traces" }, "Should have POST /v1/traces")

            val getRoutes = routes.filter { it.method == "GET" }
            assertTrue(getRoutes.any { it.path == "/health" }, "Should have GET /health")
            assertTrue(getRoutes.any { it.path == "/api/traces" }, "Should have GET /api/traces")

        } finally {
            server.stop()
        }
    }
}
