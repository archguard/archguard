package org.archguard.mcp

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import org.archguard.mcp.server.ArchGuardMcpServer
import org.archguard.mcp.server.ServerConfig
import org.archguard.mcp.server.ServerMode
import java.io.File

private val logger = KotlinLogging.logger {}
private val prettyJson = Json { prettyPrint = true }

/**
 * Main entry point for the ArchGuard MCP Server.
 *
 * Supports multiple modes:
 * - stdio: Standard input/output (default, for local tools like Claude Desktop)
 * - http: HTTP server with JSON-RPC endpoint
 *
 * Configuration via environment variables:
 * - MCP_MODE: Server mode (stdio, http)
 * - MCP_PORT: HTTP port (default: 8080)
 * - MCP_HOST: HTTP host binding (default: 127.0.0.1)
 * - MCP_WORKSPACE: Default workspace path
 */
fun main(args: Array<String>) {
    val config = ServerConfig.fromEnvironment()

    // Override from command line arguments
    val mode = args.firstOrNull()?.let {
        when (it.lowercase()) {
            "stdio" -> ServerMode.STDIO
            "http" -> ServerMode.HTTP
            "sse" -> ServerMode.SSE
            else -> config.mode
        }
    } ?: config.mode

    val effectiveConfig = config.copy(mode = mode)

    when (effectiveConfig.mode) {
        ServerMode.STDIO -> runStdioServer(effectiveConfig)
        ServerMode.HTTP, ServerMode.SSE -> runHttpServer(effectiveConfig)
    }
}

/**
 * Runs the MCP server in stdio mode.
 * This is the recommended mode for local integrations like Claude Desktop.
 */
private fun runStdioServer(config: ServerConfig) {
    // Write server info to stderr (stdout is reserved for MCP protocol)
    val jarPath = resolveRunningJarPath()
    val jarPathHint = jarPath ?: "/path/to/archguard-mcp-server.jar"

    System.err.println("Starting ArchGuard MCP Server in stdio mode")
    System.err.println("Server: ${ArchGuardMcpServer.SERVER_NAME} v${ArchGuardMcpServer.SERVER_VERSION}")
    System.err.println("Workspace: ${config.workspacePath}")
    System.err.println()
    System.err.println("MCP (Claude Desktop) config snippet (copy/paste):")
    System.err.println(prettyJson.encodeToString(buildClaudeDesktopConfig(jarPathHint, config)))
    System.err.println()
    System.err.println("Quick self-check (stdio):")
    System.err.println("  echo '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}' | java -jar \"$jarPathHint\" stdio")
    System.err.println()

    val handler = ArchGuardMcpServer.createHandler(config)

    runBlocking {
        // Read from stdin, write to stdout (MCP protocol)
        val reader = System.`in`.bufferedReader()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            val requestLine = line ?: break

            if (requestLine.isBlank()) continue

            try {
                val response = handler.handleRequest(requestLine)
                println(response)
                System.out.flush()
            } catch (e: Exception) {
                System.err.println("Error processing request: ${e.message}")
            }
        }
    }
}

/**
 * Runs the MCP server in HTTP mode.
 * Binds to localhost by default for security.
 */
private fun runHttpServer(config: ServerConfig) {
    logger.info { "Starting ArchGuard MCP Server in HTTP mode" }
    logger.info { "Server: ${ArchGuardMcpServer.SERVER_NAME} v${ArchGuardMcpServer.SERVER_VERSION}" }
    logger.info { "Listening on http://${config.host}:${config.port}" }
    logger.info { "Workspace: ${config.workspacePath}" }
    logger.info { "MCP endpoint: POST http://${config.host}:${config.port}/mcp" }
    logger.info { "Health check: GET  http://${config.host}:${config.port}/health" }
    logger.info { "Quick self-check: curl -sS -X POST http://${config.host}:${config.port}/mcp -H 'Content-Type: application/json' -d '{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}'" }

    if (config.host != "127.0.0.1" && config.host != "localhost") {
        logger.warn { "WARNING: Server is bound to ${config.host}. For security, consider using localhost or placing behind an authenticated reverse proxy." }
    }

    val handler = ArchGuardMcpServer.createHandler(config)

    embeddedServer(Netty, port = config.port, host = config.host) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            // Health check endpoint
            get("/health") {
                call.respond(mapOf(
                    "status" to "ok",
                    "server" to ArchGuardMcpServer.SERVER_NAME,
                    "version" to ArchGuardMcpServer.SERVER_VERSION
                ))
            }

            // MCP JSON-RPC endpoint
            post("/mcp") {
                val request = call.receiveText()
                val response = handler.handleRequest(request)
                call.respondText(response, ContentType.Application.Json)
            }

            // SSE endpoint for streaming (simplified implementation)
            get("/mcp/sse") {
                call.respondText(
                    "SSE endpoint. Use POST /mcp for JSON-RPC requests.",
                    ContentType.Text.Plain
                )
            }
        }
    }.start(wait = true)
}

private fun resolveRunningJarPath(): String? {
    return runCatching {
        val uri = object {}.javaClass.protectionDomain.codeSource.location.toURI()
        val file = File(uri)
        file.takeIf { it.isFile && it.extension.lowercase() == "jar" }?.absolutePath
    }.getOrNull()
}

private fun buildClaudeDesktopConfig(jarPath: String, config: ServerConfig): JsonObject {
    return buildJsonObject {
        put("mcpServers", buildJsonObject {
            put("archguard", buildJsonObject {
                put("command", JsonPrimitive("java"))
                put("args", buildJsonArray {
                    add(JsonPrimitive("-jar"))
                    add(JsonPrimitive(jarPath))
                    add(JsonPrimitive("stdio"))
                })
                put("env", buildJsonObject {
                    put("MCP_MODE", JsonPrimitive("stdio"))
                    put("MCP_WORKSPACE", JsonPrimitive(config.workspacePath))
                })
            })
        })
    }
}
