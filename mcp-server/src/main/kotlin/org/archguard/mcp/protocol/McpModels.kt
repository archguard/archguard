package org.archguard.mcp.protocol

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * MCP Protocol Models - JSON-RPC 2.0 based
 */

@Serializable
data class McpRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: JsonObject? = null,
    val id: JsonElement? = null
)

@Serializable
data class McpResponse(
    val jsonrpc: String = "2.0",
    val result: JsonElement? = null,
    val error: McpError? = null,
    val id: JsonElement? = null
)

@Serializable
data class McpError(
    val code: Int,
    val message: String,
    val data: JsonElement? = null
)

object McpErrorCodes {
    const val PARSE_ERROR = -32700
    const val INVALID_REQUEST = -32600
    const val METHOD_NOT_FOUND = -32601
    const val INVALID_PARAMS = -32602
    const val INTERNAL_ERROR = -32603
}

// MCP Capability Models
@Serializable
data class ServerInfo(
    val name: String,
    val version: String
)

@Serializable
data class ServerCapabilities(
    val resources: ResourceCapabilities? = null,
    val tools: ToolCapabilities? = null,
    val prompts: PromptCapabilities? = null
)

@Serializable
data class ResourceCapabilities(
    val subscribe: Boolean = false,
    val listChanged: Boolean = false
)

@Serializable
data class ToolCapabilities(
    val listChanged: Boolean = false
)

@Serializable
data class PromptCapabilities(
    val listChanged: Boolean = false
)

@Serializable
data class InitializeResult(
    val protocolVersion: String,
    val serverInfo: ServerInfo,
    val capabilities: ServerCapabilities,
    val instructions: String? = null
)

// Resource Models
@Serializable
data class Resource(
    val uri: String,
    val name: String,
    val description: String? = null,
    val mimeType: String? = null
)

@Serializable
data class ResourceContent(
    val uri: String,
    val mimeType: String? = null,
    val text: String? = null,
    val blob: String? = null
)

@Serializable
data class ListResourcesResult(
    val resources: List<Resource>
)

@Serializable
data class ReadResourceResult(
    val contents: List<ResourceContent>
)

// Tool Models
@Serializable
data class Tool(
    val name: String,
    val description: String? = null,
    val inputSchema: JsonObject
)

@Serializable
data class ListToolsResult(
    val tools: List<Tool>
)

@Serializable
data class ToolContent(
    val type: String = "text",
    val text: String? = null
)

@Serializable
data class CallToolResult(
    val content: List<ToolContent>,
    val isError: Boolean = false
)

// Prompt Models
@Serializable
data class Prompt(
    val name: String,
    val description: String? = null,
    val arguments: List<PromptArgument>? = null
)

@Serializable
data class PromptArgument(
    val name: String,
    val description: String? = null,
    val required: Boolean = false
)

@Serializable
data class ListPromptsResult(
    val prompts: List<Prompt>
)

@Serializable
data class PromptMessage(
    val role: String,
    val content: PromptContent
)

@Serializable
data class PromptContent(
    val type: String = "text",
    val text: String
)

@Serializable
data class GetPromptResult(
    val description: String? = null,
    val messages: List<PromptMessage>
)
