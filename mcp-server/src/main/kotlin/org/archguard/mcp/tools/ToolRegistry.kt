package org.archguard.mcp.tools

import kotlinx.serialization.json.*
import org.archguard.mcp.protocol.CallToolResult
import org.archguard.mcp.protocol.Tool
import org.archguard.mcp.protocol.ToolContent
import org.archguard.mcp.server.ServerConfig

/**
 * Registry for MCP Tools providing linting operations.
 */
class ToolRegistry(config: ServerConfig) {
    private val lintEngine = LintEngine(config)
    private val tools = mutableMapOf<String, ToolDefinition>()

    init {
        registerTools()
    }

    private fun registerTools() {
        // lint_code tool
        tools["lint_code"] = ToolDefinition(
            tool = Tool(
                name = "lint_code",
                description = "Analyze code quality issues using ArchGuard rules. Detects code smells, anti-patterns, and architectural violations.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to the project directory or file to analyze")
                        })
                        put("language", buildJsonObject {
                            put("type", "string")
                            put("description", "Programming language (java, kotlin, typescript, etc.)")
                        })
                        put("rules", buildJsonObject {
                            put("type", "array")
                            put("description", "List of rule IDs to apply, or ['all'] for all rules")
                            put("items", buildJsonObject { put("type", "string") })
                        })
                    })
                    put("required", buildJsonArray { add("path") })
                }
            ),
            handler = { args ->
                val path = args["path"]?.jsonPrimitive?.content ?: ""
                val language = args["language"]?.jsonPrimitive?.content
                val rules = args["rules"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull } ?: listOf("all")
                lintEngine.lintCode(path, language, rules)
            }
        )

        // lint_webapi tool
        tools["lint_webapi"] = ToolDefinition(
            tool = Tool(
                name = "lint_webapi",
                description = "Analyze REST API design issues. Checks for URL naming conventions, HTTP method usage, parameter counts, and more.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to the project directory to analyze")
                        })
                        put("apis", buildJsonObject {
                            put("type", "array")
                            put("description", "Optional list of API definitions in JSON format")
                            put("items", buildJsonObject { put("type", "object") })
                        })
                    })
                    put("required", buildJsonArray { add("path") })
                }
            ),
            handler = { args ->
                val path = args["path"]?.jsonPrimitive?.content ?: ""
                val apis = args["apis"]?.jsonArray
                lintEngine.lintWebApi(path, apis)
            }
        )

        // lint_sql tool
        tools["lint_sql"] = ToolDefinition(
            tool = Tool(
                name = "lint_sql",
                description = "Analyze SQL and database design issues. Checks table naming, column sizes, JOIN counts, primary keys, and query patterns.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("sql", buildJsonObject {
                            put("type", "string")
                            put("description", "SQL statements to analyze")
                        })
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to SQL file(s) to analyze")
                        })
                    })
                }
            ),
            handler = { args ->
                val sql = args["sql"]?.jsonPrimitive?.content
                val path = args["path"]?.jsonPrimitive?.content
                lintEngine.lintSql(sql, path)
            }
        )

        // lint_test tool
        tools["lint_test"] = ToolDefinition(
            tool = Tool(
                name = "lint_test",
                description = "Analyze test code quality. Detects test smells like empty tests, ignored tests, sleep calls, redundant assertions, etc.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to the test directory or file")
                        })
                        put("language", buildJsonObject {
                            put("type", "string")
                            put("description", "Programming language of test code")
                        })
                    })
                    put("required", buildJsonArray { add("path") })
                }
            ),
            handler = { args ->
                val path = args["path"]?.jsonPrimitive?.content ?: ""
                val language = args["language"]?.jsonPrimitive?.content
                lintEngine.lintTest(path, language)
            }
        )

        // lint_layer tool
        tools["lint_layer"] = ToolDefinition(
            tool = Tool(
                name = "lint_layer",
                description = "Check layer architecture violations. Validates dependencies between architectural layers (e.g., controller -> service -> repository).",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to the project directory")
                        })
                        put("layers", buildJsonObject {
                            put("type", "array")
                            put("description", "Layer definitions in order from top to bottom")
                            put("items", buildJsonObject { put("type", "object") })
                        })
                    })
                    put("required", buildJsonArray { add("path") })
                }
            ),
            handler = { args ->
                val path = args["path"]?.jsonPrimitive?.content ?: ""
                val layers = args["layers"]?.jsonArray
                lintEngine.lintLayer(path, layers)
            }
        )

        // lint_all tool
        tools["lint_all"] = ToolDefinition(
            tool = Tool(
                name = "lint_all",
                description = "Run all applicable linters on a project. Performs comprehensive architectural analysis including code, API, SQL, test, and layer checks.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("path", buildJsonObject {
                            put("type", "string")
                            put("description", "Path to the project directory")
                        })
                        put("categories", buildJsonObject {
                            put("type", "array")
                            put("description", "Categories to include: code, webapi, sql, test, layer")
                            put("items", buildJsonObject { put("type", "string") })
                        })
                    })
                    put("required", buildJsonArray { add("path") })
                }
            ),
            handler = { args ->
                val path = args["path"]?.jsonPrimitive?.content ?: ""
                val categories = args["categories"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }
                lintEngine.lintAll(path, categories)
            }
        )

        // get_rules tool
        tools["get_rules"] = ToolDefinition(
            tool = Tool(
                name = "get_rules",
                description = "Get available linting rules by category. Returns rule definitions with IDs, names, descriptions, and severity levels.",
                inputSchema = buildJsonObject {
                    put("type", "object")
                    put("properties", buildJsonObject {
                        put("category", buildJsonObject {
                            put("type", "string")
                            put("description", "Rule category: code, webapi, sql, test, layer, comment, protobuf")
                        })
                    })
                }
            ),
            handler = { args ->
                val category = args["category"]?.jsonPrimitive?.content
                lintEngine.getRules(category)
            }
        )
    }

    /**
     * Lists all available tools.
     */
    fun listTools(): List<Tool> = tools.values.map { it.tool }

    /**
     * Executes a tool by name.
     */
    suspend fun executeTool(name: String, arguments: JsonObject): CallToolResult {
        val toolDef = tools[name]
            ?: return CallToolResult(
                content = listOf(ToolContent(text = "Tool not found: $name")),
                isError = true
            )
        return toolDef.handler(arguments)
    }
}

private data class ToolDefinition(
    val tool: Tool,
    val handler: suspend (JsonObject) -> CallToolResult
)
