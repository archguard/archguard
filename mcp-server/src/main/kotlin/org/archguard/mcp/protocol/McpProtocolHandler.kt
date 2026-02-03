package org.archguard.mcp.protocol

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import mu.KotlinLogging
import org.archguard.mcp.prompts.PromptRegistry
import org.archguard.mcp.resources.ResourceRegistry
import org.archguard.mcp.server.ArchGuardMcpServer
import org.archguard.mcp.tools.ToolRegistry

private val logger = KotlinLogging.logger {}

/**
 * MCP Protocol Handler implementing JSON-RPC 2.0
 */
class McpProtocolHandler(
    private val resourceRegistry: ResourceRegistry,
    private val toolRegistry: ToolRegistry,
    private val promptRegistry: PromptRegistry
) {
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun handleRequest(requestJson: String): String {
        return try {
            val request = json.decodeFromString<McpRequest>(requestJson)
            val response = processRequest(request)
            json.encodeToString(response)
        } catch (e: Exception) {
            logger.error(e) { "Failed to process request: $requestJson" }
            json.encodeToString(
                McpResponse(
                    error = McpError(
                        code = McpErrorCodes.PARSE_ERROR,
                        message = e.message ?: "Unknown error"
                    )
                )
            )
        }
    }

    private suspend fun processRequest(request: McpRequest): McpResponse {
        return when (request.method) {
            "initialize" -> handleInitialize(request)
            "initialized" -> McpResponse(result = JsonObject(emptyMap()), id = request.id)
            "resources/list" -> handleResourcesList(request)
            "resources/read" -> handleResourcesRead(request)
            "tools/list" -> handleToolsList(request)
            "tools/call" -> handleToolsCall(request)
            "prompts/list" -> handlePromptsList(request)
            "prompts/get" -> handlePromptsGet(request)
            "ping" -> McpResponse(result = JsonObject(emptyMap()), id = request.id)
            else -> McpResponse(
                error = McpError(
                    code = McpErrorCodes.METHOD_NOT_FOUND,
                    message = "Method not found: ${request.method}"
                ),
                id = request.id
            )
        }
    }

    private fun handleInitialize(request: McpRequest): McpResponse {
        val result = InitializeResult(
            protocolVersion = ArchGuardMcpServer.PROTOCOL_VERSION,
            serverInfo = ServerInfo(
                name = ArchGuardMcpServer.SERVER_NAME,
                version = ArchGuardMcpServer.SERVER_VERSION
            ),
            capabilities = ServerCapabilities(
                resources = ResourceCapabilities(subscribe = false, listChanged = false),
                tools = ToolCapabilities(listChanged = false),
                prompts = PromptCapabilities(listChanged = false)
            ),
            instructions = """
                ArchGuard MCP Server provides architectural linting capabilities for AI assistants.
                
                Available capabilities:
                - Resources: Access linting rules and their configurations
                - Tools: Execute code analysis (lint_code, lint_webapi, lint_sql, lint_test, lint_layer, lint_all)
                - Prompts: Pre-defined templates for architectural analysis
            """.trimIndent()
        )
        return McpResponse(
            result = json.encodeToJsonElement(result),
            id = request.id
        )
    }

    private fun handleResourcesList(request: McpRequest): McpResponse {
        val resources = resourceRegistry.listResources()
        val result = ListResourcesResult(resources = resources)
        return McpResponse(
            result = json.encodeToJsonElement(result),
            id = request.id
        )
    }

    private fun handleResourcesRead(request: McpRequest): McpResponse {
        val uri = request.params?.get("uri")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing uri parameter"),
                id = request.id
            )

        return try {
            val contents = resourceRegistry.readResource(uri)
            val result = ReadResourceResult(contents = contents)
            McpResponse(
                result = json.encodeToJsonElement(result),
                id = request.id
            )
        } catch (e: Exception) {
            McpResponse(
                error = McpError(
                    McpErrorCodes.INTERNAL_ERROR,
                    "Failed to read resource: ${e.message}"
                ),
                id = request.id
            )
        }
    }

    private fun handleToolsList(request: McpRequest): McpResponse {
        val tools = toolRegistry.listTools()
        val result = ListToolsResult(tools = tools)
        return McpResponse(
            result = json.encodeToJsonElement(result),
            id = request.id
        )
    }

    private suspend fun handleToolsCall(request: McpRequest): McpResponse {
        val toolName = request.params?.get("name")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing name parameter"),
                id = request.id
            )

        val arguments = request.params["arguments"]?.jsonObject ?: JsonObject(emptyMap())

        return try {
            val result = toolRegistry.executeTool(toolName, arguments)
            McpResponse(
                result = json.encodeToJsonElement(result),
                id = request.id
            )
        } catch (e: Exception) {
            logger.error(e) { "Tool execution failed: $toolName" }
            McpResponse(
                error = McpError(
                    McpErrorCodes.INTERNAL_ERROR,
                    "Tool execution failed: ${e.message}"
                ),
                id = request.id
            )
        }
    }

    private fun handlePromptsList(request: McpRequest): McpResponse {
        val prompts = promptRegistry.listPrompts()
        val result = ListPromptsResult(prompts = prompts)
        return McpResponse(
            result = json.encodeToJsonElement(result),
            id = request.id
        )
    }

    private fun handlePromptsGet(request: McpRequest): McpResponse {
        val name = request.params?.get("name")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing name parameter"),
                id = request.id
            )

        val arguments = request.params["arguments"]?.jsonObject?.mapValues { 
            it.value.jsonPrimitive.contentOrNull ?: "" 
        } ?: emptyMap()

        return try {
            val result = promptRegistry.getPrompt(name, arguments)
            McpResponse(
                result = json.encodeToJsonElement(result),
                id = request.id
            )
        } catch (e: Exception) {
            McpResponse(
                error = McpError(
                    McpErrorCodes.INTERNAL_ERROR,
                    "Failed to get prompt: ${e.message}"
                ),
                id = request.id
            )
        }
    }
}
