package org.archguard.mcp.resources

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for ArchGuard MCP ResourceRegistry.
 */
class ResourceRegistryTest {
    private val registry = ResourceRegistry()

    @Test
    fun `should list all rule categories`() {
        val categories = registry.getCategories()

        assertEquals(7, categories.size)
        assertTrue(categories.containsAll(listOf(
            "code", "webapi", "sql", "test", "layer", "comment", "protobuf"
        )))
    }

    @Test
    fun `should list resources`() {
        val resources = registry.listResources()

        // Should have 1 main resource + 7 category resources
        assertEquals(8, resources.size)

        // Check main resource
        val mainResource = resources.find { it.uri == "archguard://rules" }
        assertNotNull(mainResource)
        assertEquals("Available Linting Rules", mainResource.name)
    }

    @Test
    fun `should get webapi rules`() {
        val provider = registry.getProvider("webapi")
        assertNotNull(provider)

        val ruleSet = provider.get()
        assertNotNull(ruleSet)
        assertTrue(ruleSet.rules.toList().isNotEmpty())

        // Check that rules have required fields
        ruleSet.rules.forEach { rule ->
            assertTrue(rule.id.isNotEmpty() || rule.key.isNotEmpty(),
                "Rule should have id or key: ${rule.name}")
        }
    }

    @Test
    fun `should get sql rules`() {
        val provider = registry.getProvider("sql")
        assertNotNull(provider)

        val ruleSet = provider.get()
        assertNotNull(ruleSet)
        assertTrue(ruleSet.rules.toList().isNotEmpty())
    }

    @Test
    fun `should get test rules`() {
        val provider = registry.getProvider("test")
        assertNotNull(provider)

        val ruleSet = provider.get()
        assertNotNull(ruleSet)
        assertTrue(ruleSet.rules.toList().isNotEmpty())
    }

    @Test
    fun `should return null for unknown category`() {
        val provider = registry.getProvider("unknown")
        assertEquals(null, provider)
    }

    @Test
    fun `should read all rules resource`() {
        val contents = registry.readResource("archguard://rules")
        
        assertEquals(1, contents.size)
        assertEquals("archguard://rules", contents[0].uri)
        assertNotNull(contents[0].text)
        assertTrue(contents[0].text!!.contains("categories"))
    }

    @Test
    fun `should read category resource`() {
        val contents = registry.readResource("archguard://rules/webapi")
        
        assertEquals(1, contents.size)
        assertEquals("archguard://rules/webapi", contents[0].uri)
        assertNotNull(contents[0].text)
        assertTrue(contents[0].text!!.contains("webapi"))
    }
}
