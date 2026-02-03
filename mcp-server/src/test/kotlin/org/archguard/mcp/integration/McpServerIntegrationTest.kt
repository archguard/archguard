package org.archguard.mcp.integration

import org.archguard.mcp.resources.ResourceRegistry
import org.archguard.mcp.server.ArchGuardMcpServer
import org.archguard.mcp.server.ServerConfig
import org.archguard.mcp.tools.LintEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the ArchGuard MCP Server.
 */
class McpServerIntegrationTest {

    @Test
    fun `should create handler with default configuration`() {
        val config = ServerConfig()
        val handler = ArchGuardMcpServer.createHandler(config)

        assertNotNull(handler)
    }

    @Test
    fun `should have all rule categories available`() {
        val registry = ResourceRegistry()
        val categories = registry.getCategories()

        assertTrue(categories.contains("code"))
        assertTrue(categories.contains("webapi"))
        assertTrue(categories.contains("sql"))
        assertTrue(categories.contains("test"))
        assertTrue(categories.contains("layer"))
        assertTrue(categories.contains("comment"))
        assertTrue(categories.contains("protobuf"))
    }

    @Test
    fun `should get rule providers for each category`() {
        val registry = ResourceRegistry()
        val categories = registry.getCategories()

        categories.forEach { category ->
            val provider = registry.getProvider(category)
            assertNotNull(provider, "Provider for $category should not be null")

            val ruleSet = provider.get()
            assertNotNull(ruleSet, "RuleSet for $category should not be null")
        }
    }

    @Test
    fun `should lint sql statements`() {
        val config = ServerConfig()
        val engine = LintEngine(config)

        val sql = """
            CREATE TABLE users (
                id INT PRIMARY KEY,
                username VARCHAR(50),
                email VARCHAR(100)
            );
        """.trimIndent()

        val result = engine.lintSql(sql, null)
        assertNotNull(result)
        assertTrue(result.content.isNotEmpty())
    }

    @Test
    fun `should return error for missing sql input`() {
        val config = ServerConfig()
        val engine = LintEngine(config)

        val result = engine.lintSql(null, null)
        assertNotNull(result)
        assertTrue(result.isError)
    }

    @Test
    fun `should get rules by category`() {
        val config = ServerConfig()
        val engine = LintEngine(config)

        val result = engine.getRules("webapi")
        assertNotNull(result)
        assertTrue(result.content.isNotEmpty())
    }

    @Test
    fun `should return error for unknown category`() {
        val config = ServerConfig()
        val engine = LintEngine(config)

        val result = engine.getRules("unknown")
        assertNotNull(result)
        assertTrue(result.isError)
    }
}
