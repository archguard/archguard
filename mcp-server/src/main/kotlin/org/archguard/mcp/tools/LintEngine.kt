package org.archguard.mcp.tools

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.context.ServiceSupplyType
import org.archguard.linter.rule.code.AstRuleSetProvider
import org.archguard.linter.rule.comment.CommentRuleSetProvider
import org.archguard.linter.rule.layer.LayerRuleSetProvider
import org.archguard.linter.rule.protobuf.ProtobufRuleSetProvider
import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.linter.rule.sql.SqlRuleVisitor
import org.archguard.linter.rule.testcode.TestSmellRuleSetProvider
import org.archguard.linter.rule.webapi.WebApiRuleSetProvider
import org.archguard.linter.rule.webapi.WebApiRuleVisitor
import org.archguard.mcp.protocol.CallToolResult
import org.archguard.mcp.protocol.ToolContent
import org.archguard.mcp.server.ServerConfig
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSetProvider
import java.io.File

/**
 * Engine for executing ArchGuard linting operations.
 */
class LintEngine(private val config: ServerConfig) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    // Rule set providers
    private val codeProvider = AstRuleSetProvider()
    private val webApiProvider = WebApiRuleSetProvider()
    private val sqlProvider = SqlRuleSetProvider()
    private val testProvider = TestSmellRuleSetProvider()
    private val layerProvider = LayerRuleSetProvider()
    private val commentProvider = CommentRuleSetProvider()
    private val protobufProvider = ProtobufRuleSetProvider()

    private val allProviders: Map<String, RuleSetProvider> = mapOf(
        "code" to codeProvider,
        "webapi" to webApiProvider,
        "sql" to sqlProvider,
        "test" to testProvider,
        "layer" to layerProvider,
        "comment" to commentProvider,
        "protobuf" to protobufProvider
    )

    /**
     * Lints code quality issues.
     */
    fun lintCode(path: String, language: String?, rules: List<String>): CallToolResult {
        return try {
            val ruleSet = codeProvider.get()
            val result = LintResult(
                tool = "lint_code",
                path = path,
                language = language,
                issues = emptyList(),
                summary = LintSummary(
                    totalIssues = 0,
                    message = "Code linting rules loaded. Note: Full AST analysis requires parsed source files."
                ),
                availableRules = ruleSet.rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
            )
            createSuccessResult(result)
        } catch (e: Exception) {
            createErrorResult("Code linting failed: ${e.message}")
        }
    }

    /**
     * Lints Web API design issues.
     */
    fun lintWebApi(path: String, apis: JsonArray?): CallToolResult {
        return try {
            val services = if (apis != null) {
                parseApiDefinitions(apis)
            } else {
                emptyList()
            }

            val issues = if (services.isNotEmpty()) {
                val visitor = WebApiRuleVisitor(services)
                visitor.visitor(listOf(webApiProvider.get()))
            } else {
                emptyList()
            }

            val result = LintResult(
                tool = "lint_webapi",
                path = path,
                issues = issues.map { it.toLintIssue() },
                summary = LintSummary(
                    totalIssues = issues.size,
                    bySeverity = issues.groupBy { it.severity.name }.mapValues { it.value.size },
                    byRule = issues.groupBy { it.ruleId }.mapValues { it.value.size },
                    message = if (services.isEmpty()) {
                        "No API definitions provided. Pass API definitions via the 'apis' parameter."
                    } else {
                        "Found ${issues.size} API design issues in ${services.size} services."
                    }
                ),
                availableRules = webApiProvider.get().rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
            )
            createSuccessResult(result)
        } catch (e: Exception) {
            createErrorResult("Web API linting failed: ${e.message}")
        }
    }

    /**
     * Lints SQL statements.
     */
    fun lintSql(sql: String?, path: String?): CallToolResult {
        return try {
            val sqlStatements = mutableListOf<String>()

            sql?.let { sqlStatements.add(it) }

            path?.let { sqlPath ->
                val file = File(sqlPath)
                if (file.exists()) {
                    if (file.isFile && file.extension.lowercase() == "sql") {
                        sqlStatements.add(file.readText())
                    } else if (file.isDirectory) {
                        file.walkTopDown()
                            .filter { it.extension.lowercase() == "sql" }
                            .forEach { sqlStatements.add(it.readText()) }
                    }
                }
            }

            if (sqlStatements.isEmpty()) {
                return createErrorResult("No SQL provided. Pass SQL via 'sql' parameter or 'path' to SQL files.")
            }

            val visitor = SqlRuleVisitor(sqlStatements)
            val issues = visitor.visitor(listOf(sqlProvider.get()))

            val result = LintResult(
                tool = "lint_sql",
                path = path,
                issues = issues.map { it.toLintIssue() },
                summary = LintSummary(
                    totalIssues = issues.size,
                    bySeverity = issues.groupBy { it.severity.name }.mapValues { it.value.size },
                    byRule = issues.groupBy { it.ruleId }.mapValues { it.value.size },
                    message = "Found ${issues.size} SQL issues."
                ),
                availableRules = sqlProvider.get().rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
            )
            createSuccessResult(result)
        } catch (e: Exception) {
            createErrorResult("SQL linting failed: ${e.message}")
        }
    }

    /**
     * Lints test code quality.
     */
    fun lintTest(path: String, language: String?): CallToolResult {
        return try {
            val ruleSet = testProvider.get()
            val result = LintResult(
                tool = "lint_test",
                path = path,
                language = language,
                issues = emptyList(),
                summary = LintSummary(
                    totalIssues = 0,
                    message = "Test smell rules loaded. Note: Full analysis requires parsed test source files."
                ),
                availableRules = ruleSet.rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
            )
            createSuccessResult(result)
        } catch (e: Exception) {
            createErrorResult("Test linting failed: ${e.message}")
        }
    }

    /**
     * Lints layer architecture violations.
     */
    fun lintLayer(path: String, layers: JsonArray?): CallToolResult {
        return try {
            val ruleSet = layerProvider.get()
            val result = LintResult(
                tool = "lint_layer",
                path = path,
                issues = emptyList(),
                summary = LintSummary(
                    totalIssues = 0,
                    message = "Layer architecture rules loaded. Note: Full analysis requires parsed source files with dependency information."
                ),
                availableRules = ruleSet.rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
            )
            createSuccessResult(result)
        } catch (e: Exception) {
            createErrorResult("Layer linting failed: ${e.message}")
        }
    }

    /**
     * Runs all applicable linters.
     */
    fun lintAll(path: String, categories: List<String>?): CallToolResult {
        return try {
            val categoriesToRun = categories ?: allProviders.keys.toList()
            val categoryResults = mutableListOf<CategoryResult>()

            categoriesToRun.forEach { category ->
                val provider = allProviders[category]
                if (provider != null) {
                    val ruleSet = provider.get()
                    categoryResults.add(CategoryResult(
                        category = category,
                        rulesCount = ruleSet.rules.toList().size,
                        rules = ruleSet.rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description) }
                    ))
                }
            }

            val result = LintAllResult(
                tool = "lint_all",
                path = path,
                categories = categoriesToRun,
                results = categoryResults,
                summary = LintSummary(
                    totalIssues = 0,
                    message = "Loaded ${categoryResults.size} linter categories. Full analysis requires parsed source files."
                )
            )
            CallToolResult(
                content = listOf(ToolContent(text = json.encodeToString(result)))
            )
        } catch (e: Exception) {
            createErrorResult("Full linting failed: ${e.message}")
        }
    }

    /**
     * Gets available rules by category.
     */
    fun getRules(category: String?): CallToolResult {
        return try {
            val categoryResults = if (category != null) {
                val provider = allProviders[category]
                    ?: return createErrorResult("Unknown category: $category. Available: ${allProviders.keys.joinToString()}")
                listOf(CategoryResult(
                    category = category,
                    rulesCount = provider.get().rules.toList().size,
                    rules = provider.get().rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description, it.severity.name) }
                ))
            } else {
                allProviders.map { (cat, provider) ->
                    CategoryResult(
                        category = cat,
                        rulesCount = provider.get().rules.toList().size,
                        rules = provider.get().rules.map { RuleRef(it.id.ifEmpty { it.key }, it.name, it.description, it.severity.name) }
                    )
                }
            }

            val result = GetRulesResult(
                categories = allProviders.keys.toList(),
                rules = categoryResults
            )
            CallToolResult(
                content = listOf(ToolContent(text = json.encodeToString(result)))
            )
        } catch (e: Exception) {
            createErrorResult("Failed to get rules: ${e.message}")
        }
    }

    private fun parseApiDefinitions(apis: JsonArray): List<ContainerService> {
        return apis.mapNotNull { element ->
            try {
                val obj = element.jsonObject
                ContainerService(
                    name = obj["name"]?.jsonPrimitive?.content ?: "",
                    resources = obj["resources"]?.jsonArray?.mapNotNull { res ->
                        val resObj = res.jsonObject
                        ContainerSupply(
                            sourceUrl = resObj["url"]?.jsonPrimitive?.content
                                ?: resObj["sourceUrl"]?.jsonPrimitive?.content ?: "",
                            sourceHttpMethod = resObj["method"]?.jsonPrimitive?.content
                                ?: resObj["sourceHttpMethod"]?.jsonPrimitive?.content ?: "GET",
                            packageName = resObj["packageName"]?.jsonPrimitive?.content ?: "",
                            className = resObj["className"]?.jsonPrimitive?.content ?: "",
                            methodName = resObj["methodName"]?.jsonPrimitive?.content ?: "",
                            supplyType = ServiceSupplyType.HTTP_API
                        )
                    } ?: emptyList()
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun createSuccessResult(result: LintResult): CallToolResult {
        return CallToolResult(
            content = listOf(ToolContent(text = json.encodeToString(result)))
        )
    }

    private fun createErrorResult(message: String): CallToolResult {
        return CallToolResult(
            content = listOf(ToolContent(text = json.encodeToString(ErrorResult(
                error = true,
                message = message
            )))),
            isError = true
        )
    }

    private fun Issue.toLintIssue(): LintIssue {
        return LintIssue(
            ruleId = this.ruleId,
            name = this.name,
            detail = this.detail,
            severity = this.severity.name,
            source = this.source,
            fullName = this.fullName,
            position = LintPosition(
                startLine = this.position.startLine,
                startColumn = this.position.startColumn,
                endLine = this.position.endLine,
                endColumn = this.position.endColumn
            )
        )
    }
}

@Serializable
data class LintResult(
    val tool: String,
    val path: String? = null,
    val language: String? = null,
    val issues: List<LintIssue>,
    val summary: LintSummary,
    val availableRules: List<RuleRef>? = null
)

@Serializable
data class LintIssue(
    val ruleId: String,
    val name: String,
    val detail: String,
    val severity: String,
    val source: String = "",
    val fullName: String = "",
    val position: LintPosition = LintPosition()
)

@Serializable
data class LintPosition(
    val startLine: Int = 0,
    val startColumn: Int = 0,
    val endLine: Int = 0,
    val endColumn: Int = 0
)

@Serializable
data class LintSummary(
    val totalIssues: Int,
    val bySeverity: Map<String, Int> = emptyMap(),
    val byRule: Map<String, Int> = emptyMap(),
    val message: String = ""
)

@Serializable
data class RuleRef(
    val id: String,
    val name: String,
    val description: String,
    val severity: String = ""
)

@Serializable
data class LintAllResult(
    val tool: String,
    val path: String,
    val categories: List<String>,
    val results: List<CategoryResult>,
    val summary: LintSummary
)

@Serializable
data class CategoryResult(
    val category: String,
    val rulesCount: Int,
    val rules: List<RuleRef>
)

@Serializable
data class GetRulesResult(
    val categories: List<String>,
    val rules: List<CategoryResult>
)

@Serializable
data class ErrorResult(
    val error: Boolean,
    val message: String
)
