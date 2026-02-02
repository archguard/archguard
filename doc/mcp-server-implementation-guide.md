# MCP Server Implementation Guide

This guide provides a step-by-step approach to implementing the ArchGuard MCP Server.

## Prerequisites

- Kotlin 1.8+
- Gradle 7.0+
- Understanding of MCP (Model Context Protocol)
- Familiarity with ArchGuard's linter architecture

## Project Structure

```
archguard/
├── mcp-server/                    # New module
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── org/archguard/mcp/
│   │   │   │       ├── server/
│   │   │   │       │   ├── McpServer.kt         # Main server
│   │   │   │       │   ├── McpProtocolHandler.kt # JSON-RPC handler
│   │   │   │       │   └── ServerConfig.kt      # Configuration
│   │   │   │       ├── resources/
│   │   │   │       │   ├── RulesResource.kt     # archguard://rules
│   │   │   │       │   └── ResourceProvider.kt  # Resource registry
│   │   │   │       ├── tools/
│   │   │   │       │   ├── LintCodeTool.kt      # lint_code
│   │   │   │       │   ├── LintWebapiTool.kt    # lint_webapi
│   │   │   │       │   ├── LintSqlTool.kt       # lint_sql
│   │   │   │       │   └── ToolRegistry.kt      # Tool registration
│   │   │   │       ├── prompts/
│   │   │   │       │   ├── AnalyzeArchitecturePrompt.kt
│   │   │   │       │   └── PromptRegistry.kt
│   │   │   │       └── Main.kt                  # Entry point
│   │   │   └── resources/
│   │   │       └── application.conf
│   │   └── test/
│   │       └── kotlin/
│   │           └── org/archguard/mcp/
│   │               └── integration/
│   └── build.gradle.kts
└── settings.gradle.kts            # Add mcp-server module
```

## Step 1: Module Setup

### 1.1 Add Module to settings.gradle.kts

```kotlin
// settings.gradle.kts
include("mcp-server")
```

### 1.2 Create build.gradle.kts

```kotlin
// mcp-server/build.gradle.kts
plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    application
}

application {
    mainClass.set("org.archguard.mcp.MainKt")
}

dependencies {
    // ArchGuard dependencies
    implementation(project(":rule-core"))
    implementation(project(":rule-linter:rule-code"))
    implementation(project(":rule-linter:rule-webapi"))
    implementation(project(":rule-linter:rule-sql"))
    implementation(project(":rule-linter:rule-test"))
    implementation(project(":rule-linter:rule-layer"))
    implementation(project(":scanner_core"))
    
    // Ktor for HTTP server
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // JSON serialization (already in rule-core)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    
    // Testing
    testImplementation("io.ktor:ktor-server-tests:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.archguard.mcp.MainKt"
        }
        // Create fat JAR
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
```

## Step 2: Core MCP Protocol Implementation

### 2.1 MCP Request/Response Models

```kotlin
// src/main/kotlin/org/archguard/mcp/protocol/McpModels.kt
package org.archguard.mcp.protocol

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class McpRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: JsonObject? = null,
    val id: Int? = null
)

@Serializable
data class McpResponse(
    val jsonrpc: String = "2.0",
    val result: JsonElement? = null,
    val error: McpError? = null,
    val id: Int? = null
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
```

### 2.2 MCP Protocol Handler

```kotlin
// src/main/kotlin/org/archguard/mcp/server/McpProtocolHandler.kt
package org.archguard.mcp.server

import kotlinx.serialization.json.*
import org.archguard.mcp.protocol.*
import org.archguard.mcp.resources.ResourceProvider
import org.archguard.mcp.tools.ToolRegistry
import org.archguard.mcp.prompts.PromptRegistry
import mu.KotlinLogging

class McpProtocolHandler(
    private val resourceProvider: ResourceProvider,
    private val toolRegistry: ToolRegistry,
    private val promptRegistry: PromptRegistry
) {
    private val logger = KotlinLogging.logger {}
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    suspend fun handleRequest(requestJson: String): String {
        return try {
            val request = json.decodeFromString<McpRequest>(requestJson)
            val response = processRequest(request)
            json.encodeToString(McpResponse.serializer(), response)
        } catch (e: Exception) {
            logger.error(e) { "Failed to process request" }
            json.encodeToString(
                McpResponse.serializer(),
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
            "resources/list" -> handleResourcesList(request)
            "resources/read" -> handleResourcesRead(request)
            "tools/list" -> handleToolsList(request)
            "tools/call" -> handleToolsCall(request)
            "prompts/list" -> handlePromptsList(request)
            "prompts/get" -> handlePromptsGet(request)
            "initialize" -> handleInitialize(request)
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
        return McpResponse(
            result = buildJsonObject {
                put("protocolVersion", "2024-11-05")
                put("serverInfo", buildJsonObject {
                    put("name", "archguard-mcp-server")
                    put("version", "1.0.0")
                })
                put("capabilities", buildJsonObject {
                    put("resources", buildJsonObject {})
                    put("tools", buildJsonObject {})
                    put("prompts", buildJsonObject {})
                })
            },
            id = request.id
        )
    }

    private suspend fun handleResourcesList(request: McpRequest): McpResponse {
        val resources = resourceProvider.listResources()
        return McpResponse(
            result = json.encodeToJsonElement(resources),
            id = request.id
        )
    }

    private suspend fun handleResourcesRead(request: McpRequest): McpResponse {
        val uri = request.params?.get("uri")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing uri parameter"),
                id = request.id
            )
        
        val resource = resourceProvider.readResource(uri)
        return McpResponse(
            result = json.encodeToJsonElement(resource),
            id = request.id
        )
    }

    private suspend fun handleToolsList(request: McpRequest): McpResponse {
        val tools = toolRegistry.listTools()
        return McpResponse(
            result = json.encodeToJsonElement(tools),
            id = request.id
        )
    }

    private suspend fun handleToolsCall(request: McpRequest): McpResponse {
        val toolName = request.params?.get("name")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing name parameter"),
                id = request.id
            )
        
        val arguments = request.params["arguments"]?.jsonObject
        
        return try {
            val result = toolRegistry.executeTool(toolName, arguments)
            McpResponse(
                result = json.encodeToJsonElement(result),
                id = request.id
            )
        } catch (e: Exception) {
            logger.error(e) { "Tool execution failed" }
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
        return McpResponse(
            result = json.encodeToJsonElement(prompts),
            id = request.id
        )
    }

    private fun handlePromptsGet(request: McpRequest): McpResponse {
        val name = request.params?.get("name")?.jsonPrimitive?.content
            ?: return McpResponse(
                error = McpError(McpErrorCodes.INVALID_PARAMS, "Missing name parameter"),
                id = request.id
            )
        
        val prompt = promptRegistry.getPrompt(name)
        return McpResponse(
            result = json.encodeToJsonElement(prompt),
            id = request.id
        )
    }
}
```

## Step 3: Implement Resources

### 3.1 Rules Resource

```kotlin
// src/main/kotlin/org/archguard/mcp/resources/RulesResource.kt
package org.archguard.mcp.resources

import kotlinx.serialization.Serializable
import org.archguard.rule.core.RuleSetProvider
import java.util.ServiceLoader

@Serializable
data class Resource(
    val uri: String,
    val name: String,
    val description: String,
    val mimeType: String = "application/json"
)

@Serializable
data class ResourceContent(
    val uri: String,
    val mimeType: String = "application/json",
    val text: String
)

class RulesResource {
    private val ruleSetProviders = ServiceLoader.load(RuleSetProvider::class.java).toList()

    fun listResources(): List<Resource> {
        return listOf(
            Resource(
                uri = "archguard://rules",
                name = "Available Linting Rules",
                description = "Complete list of architectural linting rules"
            ),
            Resource(
                uri = "archguard://rules/code",
                name = "Code Quality Rules",
                description = "Rules for code quality and smells"
            ),
            Resource(
                uri = "archguard://rules/webapi",
                name = "Web API Rules",
                description = "Rules for REST API design"
            ),
            Resource(
                uri = "archguard://rules/sql",
                name = "SQL Rules",
                description = "Rules for SQL and database design"
            )
        )
    }

    fun readResource(uri: String): ResourceContent {
        return when {
            uri == "archguard://rules" -> getAllRules()
            uri.startsWith("archguard://rules/") -> {
                val category = uri.substringAfter("archguard://rules/")
                getRulesByCategory(category)
            }
            else -> throw IllegalArgumentException("Unknown resource: $uri")
        }
    }

    private fun getAllRules(): ResourceContent {
        // Implementation to gather all rules
        val rules = mutableMapOf<String, Any>()
        // ... collect rules from providers
        return ResourceContent(
            uri = "archguard://rules",
            text = kotlinx.serialization.json.Json.encodeToString(
                kotlinx.serialization.json.JsonObject.serializer(),
                kotlinx.serialization.json.buildJsonObject {
                    // ... build rules JSON
                }
            )
        )
    }

    private fun getRulesByCategory(category: String): ResourceContent {
        // Implementation for specific category
        TODO("Implement category-specific rules")
    }
}
```

### 3.2 Resource Provider

```kotlin
// src/main/kotlin/org/archguard/mcp/resources/ResourceProvider.kt
package org.archguard.mcp.resources

class ResourceProvider {
    private val rulesResource = RulesResource()

    suspend fun listResources(): Map<String, List<Resource>> {
        return mapOf(
            "resources" to rulesResource.listResources()
        )
    }

    suspend fun readResource(uri: String): ResourceContent {
        return when {
            uri.startsWith("archguard://rules") -> rulesResource.readResource(uri)
            else -> throw IllegalArgumentException("Unknown resource URI: $uri")
        }
    }
}
```

## Step 4: Implement Tools

### 4.1 Base Tool Interface

```kotlin
// src/main/kotlin/org/archguard/mcp/tools/Tool.kt
package org.archguard.mcp.tools

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ToolDefinition(
    val name: String,
    val description: String,
    val inputSchema: JsonObject
)

@Serializable
data class ToolResult(
    val content: List<ToolContent>
)

@Serializable
data class ToolContent(
    val type: String = "text",
    val text: String
)

interface Tool {
    fun definition(): ToolDefinition
    suspend fun execute(arguments: JsonObject?): ToolResult
}
```

### 4.2 Lint Code Tool

```kotlin
// src/main/kotlin/org/archguard/mcp/tools/LintCodeTool.kt
package org.archguard.mcp.tools

import kotlinx.serialization.json.*
import org.archguard.scanner.core.context.Context
import org.archguard.rule.core.RuleType
// Import rule-linter components

class LintCodeTool : Tool {
    override fun definition() = ToolDefinition(
        name = "lint_code",
        description = "Analyze code quality issues using ArchGuard rules",
        inputSchema = buildJsonObject {
            put("type", "object")
            put("properties", buildJsonObject {
                put("path", buildJsonObject {
                    put("type", "string")
                    put("description", "Path to the project directory")
                })
                put("language", buildJsonObject {
                    put("type", "string")
                    put("description", "Programming language (java, kotlin, etc.)")
                })
                put("rules", buildJsonObject {
                    put("type", "array")
                    put("description", "List of rule IDs to apply, or ['all']")
                })
            })
            put("required", buildJsonArray {
                add("path")
            })
        }
    )

    override suspend fun execute(arguments: JsonObject?): ToolResult {
        val path = arguments?.get("path")?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing required parameter: path")
        
        val language = arguments["language"]?.jsonPrimitive?.content ?: "java"
        val rules = arguments["rules"]?.jsonArray?.map { it.jsonPrimitive.content } ?: listOf("all")

        // Execute linting
        val issues = performLinting(path, language, rules)
        
        val resultText = formatIssues(issues)
        
        return ToolResult(
            content = listOf(ToolContent(text = resultText))
        )
    }

    private fun performLinting(path: String, language: String, rules: List<String>): List<Any> {
        // Implementation using existing ArchGuard linter
        // This would integrate with RuleAnalyser, RuleSetProvider, etc.
        return emptyList() // Placeholder
    }

    private fun formatIssues(issues: List<Any>): String {
        // Format issues as JSON
        return Json.encodeToString(
            kotlinx.serialization.json.JsonObject.serializer(),
            buildJsonObject {
                put("totalIssues", issues.size)
                // ... format issues
            }
        )
    }
}
```

### 4.3 Tool Registry

```kotlin
// src/main/kotlin/org/archguard/mcp/tools/ToolRegistry.kt
package org.archguard.mcp.tools

import kotlinx.serialization.json.JsonObject

class ToolRegistry {
    private val tools = mutableMapOf<String, Tool>()

    init {
        register(LintCodeTool())
        register(LintWebapiTool())
        register(LintSqlTool())
        // ... register other tools
    }

    private fun register(tool: Tool) {
        tools[tool.definition().name] = tool
    }

    fun listTools(): Map<String, List<ToolDefinition>> {
        return mapOf(
            "tools" to tools.values.map { it.definition() }
        )
    }

    suspend fun executeTool(name: String, arguments: JsonObject?): ToolResult {
        val tool = tools[name] 
            ?: throw IllegalArgumentException("Tool not found: $name")
        return tool.execute(arguments)
    }
}
```

## Step 5: Main Server Application

```kotlin
// src/main/kotlin/org/archguard/mcp/Main.kt
package org.archguard.mcp

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import org.archguard.mcp.server.*
import org.archguard.mcp.resources.ResourceProvider
import org.archguard.mcp.tools.ToolRegistry
import org.archguard.mcp.prompts.PromptRegistry
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val mode = System.getenv("MCP_MODE") ?: "http"
    val port = System.getenv("MCP_PORT")?.toIntOrNull() ?: 8080

    when (mode) {
        "stdio" -> runStdioServer()
        "http" -> runHttpServer(port)
        else -> {
            logger.error { "Unknown mode: $mode. Use 'stdio' or 'http'" }
        }
    }
}

fun runStdioServer() {
    logger.info { "Starting MCP server in stdio mode" }
    
    val handler = createProtocolHandler()
    
    // Read from stdin, write to stdout
    while (true) {
        val line = readLine() ?: break
        val response = kotlinx.coroutines.runBlocking {
            handler.handleRequest(line)
        }
        println(response)
    }
}

fun runHttpServer(port: Int) {
    logger.info { "Starting MCP server on port $port" }
    
    embeddedServer(Netty, port = port) {
        install(ContentNegotiation) {
            json()
        }

        val handler = createProtocolHandler()

        routing {
            post("/mcp") {
                val request = call.receiveText()
                val response = handler.handleRequest(request)
                call.respondText(response, ContentType.Application.Json)
            }

            get("/health") {
                call.respondText("OK")
            }
        }
    }.start(wait = true)
}

fun createProtocolHandler(): McpProtocolHandler {
    return McpProtocolHandler(
        resourceProvider = ResourceProvider(),
        toolRegistry = ToolRegistry(),
        promptRegistry = PromptRegistry()
    )
}
```

## Step 6: Testing

### 6.1 Integration Test

```kotlin
// src/test/kotlin/org/archguard/mcp/integration/McpServerIntegrationTest.kt
package org.archguard.mcp.integration

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import kotlinx.serialization.json.Json

class McpServerIntegrationTest {
    
    @Test
    fun testInitializeRequest() = testApplication {
        val response = client.post("/mcp") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "jsonrpc": "2.0",
                    "method": "initialize",
                    "id": 1
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("archguard-mcp-server"))
    }

    @Test
    fun testListResources() = testApplication {
        val response = client.post("/mcp") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "jsonrpc": "2.0",
                    "method": "resources/list",
                    "id": 2
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("archguard://rules"))
    }
}
```

## Step 7: Build and Package

### 7.1 Build Fat JAR

```bash
./gradlew :mcp-server:shadowJar
```

### 7.2 Docker Image

```dockerfile
# Dockerfile
FROM openjdk:17-slim

WORKDIR /app

COPY mcp-server/build/libs/mcp-server-all.jar /app/archguard-mcp-server.jar

ENV MCP_MODE=http
ENV MCP_PORT=8080

EXPOSE 8080

CMD ["java", "-jar", "/app/archguard-mcp-server.jar"]
```

### 7.3 Build Docker Image

```bash
docker build -t archguard/mcp-server:latest .
```

## Step 8: Usage Examples

### 8.1 With Claude Desktop (stdio mode)

```json
// ~/.config/Claude/claude_desktop_config.json
{
  "mcpServers": {
    "archguard": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/archguard-mcp-server.jar"
      ],
      "env": {
        "MCP_MODE": "stdio"
      }
    }
  }
}
```

### 8.2 With Docker (HTTP mode)

```bash
docker run -d \
  -p 8080:8080 \
  -v /path/to/project:/workspace \
  -e MCP_MODE=http \
  archguard/mcp-server:latest
```

### 8.3 Direct HTTP Request

```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "lint_code",
      "arguments": {
        "path": "/workspace/my-project",
        "language": "java"
      }
    },
    "id": 1
  }'
```

## Next Steps

1. Complete all tool implementations (webapi, sql, test, layer)
2. Add comprehensive error handling
3. Implement caching for performance
4. Add configuration file support (.archguard-mcp.yml)
5. Create user documentation
6. Set up CI/CD pipeline
7. Publish to Maven Central and Docker Hub

## Additional Resources

- [MCP Specification](https://modelcontextprotocol.io/specification)
- [Ktor Documentation](https://ktor.io/)
- [ArchGuard Documentation](https://archguard.org/)
