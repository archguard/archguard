package org.archguard.mcp.server

/**
 * Configuration for the ArchGuard MCP Server.
 *
 * @property mode Server mode: "stdio" for standard input/output or "http" for HTTP server
 * @property port HTTP port (only used in HTTP mode)
 * @property host HTTP host binding (defaults to localhost for security)
 * @property workspacePath Default workspace path for linting operations
 */
data class ServerConfig(
    val mode: ServerMode = ServerMode.STDIO,
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val workspacePath: String = System.getProperty("user.dir")
) {
    companion object {
        fun fromEnvironment(): ServerConfig {
            val mode = when (System.getenv("MCP_MODE")?.lowercase()) {
                "http" -> ServerMode.HTTP
                "sse" -> ServerMode.SSE
                else -> ServerMode.STDIO
            }
            val port = System.getenv("MCP_PORT")?.toIntOrNull() ?: 8080
            val host = System.getenv("MCP_HOST") ?: "127.0.0.1"
            val workspacePath = System.getenv("MCP_WORKSPACE") ?: System.getProperty("user.dir")

            return ServerConfig(
                mode = mode,
                port = port,
                host = host,
                workspacePath = workspacePath
            )
        }
    }
}

enum class ServerMode {
    STDIO,  // Standard input/output (default, most secure)
    HTTP,   // HTTP server (localhost only by default)
    SSE     // Server-Sent Events
}
