package org.archguard.mcp.server

import org.archguard.mcp.prompts.PromptRegistry
import org.archguard.mcp.protocol.McpProtocolHandler
import org.archguard.mcp.resources.ResourceRegistry
import org.archguard.mcp.tools.ToolRegistry

/**
 * ArchGuard MCP Server
 *
 * A lightweight Model Context Protocol server that exposes ArchGuard's
 * architecture linter capabilities to AI assistants and development tools.
 */
object ArchGuardMcpServer {
    const val SERVER_NAME = "archguard-mcp-server"
    const val SERVER_VERSION = "1.0.0"
    const val PROTOCOL_VERSION = "2024-11-05"

    /**
     * Creates and configures the MCP protocol handler with all ArchGuard capabilities.
     */
    fun createHandler(config: ServerConfig = ServerConfig()): McpProtocolHandler {
        val resourceRegistry = ResourceRegistry()
        val toolRegistry = ToolRegistry(config)
        val promptRegistry = PromptRegistry()

        return McpProtocolHandler(
            resourceRegistry = resourceRegistry,
            toolRegistry = toolRegistry,
            promptRegistry = promptRegistry
        )
    }
}
