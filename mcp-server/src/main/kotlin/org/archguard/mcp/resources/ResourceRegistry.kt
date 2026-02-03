package org.archguard.mcp.resources

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.linter.rule.code.AstRuleSetProvider
import org.archguard.linter.rule.comment.CommentRuleSetProvider
import org.archguard.linter.rule.layer.LayerRuleSetProvider
import org.archguard.linter.rule.protobuf.ProtobufRuleSetProvider
import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.linter.rule.testcode.TestSmellRuleSetProvider
import org.archguard.linter.rule.webapi.WebApiRuleSetProvider
import org.archguard.mcp.protocol.Resource
import org.archguard.mcp.protocol.ResourceContent
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider

/**
 * Registry for MCP Resources providing access to ArchGuard linting rules.
 */
class ResourceRegistry {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    // Map of category names to their RuleSetProviders
    private val ruleProviders: Map<String, RuleSetProvider> = mapOf(
        "code" to AstRuleSetProvider(),
        "webapi" to WebApiRuleSetProvider(),
        "sql" to SqlRuleSetProvider(),
        "test" to TestSmellRuleSetProvider(),
        "layer" to LayerRuleSetProvider(),
        "comment" to CommentRuleSetProvider(),
        "protobuf" to ProtobufRuleSetProvider()
    )

    // Map of categories to their descriptions
    private val categoryDescriptions: Map<String, String> = mapOf(
        "code" to "Rules for code quality, smells, and anti-patterns",
        "webapi" to "Rules for REST API design and conventions",
        "sql" to "Rules for SQL and database design quality",
        "test" to "Rules for test code quality and smells",
        "layer" to "Rules for layer architecture violations",
        "comment" to "Rules for code comment quality",
        "protobuf" to "Rules for Protocol Buffer design"
    )

    /**
     * Lists all available resources.
     */
    fun listResources(): List<Resource> {
        val resources = mutableListOf<Resource>()

        // Add main rules resource
        resources.add(
            Resource(
                uri = "archguard://rules",
                name = "Available Linting Rules",
                description = "Complete list of architectural linting rules across all categories",
                mimeType = "application/json"
            )
        )

        // Add category-specific resources
        ruleProviders.keys.forEach { category ->
            resources.add(
                Resource(
                    uri = "archguard://rules/$category",
                    name = "${category.replaceFirstChar { it.uppercase() }} Rules",
                    description = categoryDescriptions[category] ?: "Rules for $category",
                    mimeType = "application/json"
                )
            )
        }

        return resources
    }

    /**
     * Reads a resource by URI.
     */
    fun readResource(uri: String): List<ResourceContent> {
        return when {
            uri == "archguard://rules" -> listOf(
                ResourceContent(
                    uri = uri,
                    mimeType = "application/json",
                    text = getAllRulesJson()
                )
            )
            uri.startsWith("archguard://rules/") -> {
                val category = uri.substringAfter("archguard://rules/")
                listOf(
                    ResourceContent(
                        uri = uri,
                        mimeType = "application/json",
                        text = getRulesByCategoryJson(category)
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown resource: $uri")
        }
    }

    /**
     * Gets all rules from all categories as JSON.
     */
    private fun getAllRulesJson(): String {
        val allRules = RulesResponse(
            categories = ruleProviders.map { (category, provider) ->
                RuleCategory(
                    name = category,
                    description = categoryDescriptions[category] ?: "",
                    rules = ruleSetToRuleInfoList(provider.get())
                )
            }
        )
        return json.encodeToString(allRules)
    }

    /**
     * Gets rules for a specific category as JSON.
     */
    private fun getRulesByCategoryJson(category: String): String {
        val provider = ruleProviders[category]
            ?: return json.encodeToString(
                RuleCategory(
                    name = category,
                    description = "Unknown category",
                    rules = emptyList()
                )
            )

        val ruleSet = provider.get()
        val categoryResponse = RuleCategory(
            name = category,
            description = categoryDescriptions[category] ?: "",
            rules = ruleSetToRuleInfoList(ruleSet)
        )
        return json.encodeToString(categoryResponse)
    }

    private fun ruleSetToRuleInfoList(ruleSet: RuleSet): List<RuleInfo> {
        return ruleSet.rules.map { rule ->
            RuleInfo(
                id = rule.id.ifEmpty { rule.key },
                name = rule.name,
                description = rule.description,
                severity = rule.severity.name,
                type = ruleSet.type.name
            )
        }
    }

    /**
     * Gets all available categories.
     */
    fun getCategories(): List<String> = ruleProviders.keys.toList()

    /**
     * Gets the RuleSetProvider for a category.
     */
    fun getProvider(category: String): RuleSetProvider? = ruleProviders[category]
}
