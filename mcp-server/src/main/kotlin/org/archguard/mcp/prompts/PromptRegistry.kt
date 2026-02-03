package org.archguard.mcp.prompts

import org.archguard.mcp.protocol.GetPromptResult
import org.archguard.mcp.protocol.Prompt
import org.archguard.mcp.protocol.PromptArgument
import org.archguard.mcp.protocol.PromptContent
import org.archguard.mcp.protocol.PromptMessage

/**
 * Registry for MCP Prompts providing architectural analysis templates.
 */
class PromptRegistry {
    private val prompts = mutableMapOf<String, PromptDefinition>()

    init {
        registerDefaultPrompts()
    }

    private fun registerDefaultPrompts() {
        // Analyze Architecture prompt
        prompts["analyze-architecture"] = PromptDefinition(
            prompt = Prompt(
                name = "analyze-architecture",
                description = "Analyze the architecture of a codebase, focusing on code quality, API design, layer architecture, and test coverage.",
                arguments = listOf(
                    PromptArgument(name = "path", description = "Path to the codebase to analyze", required = true),
                    PromptArgument(name = "aspects", description = "Focus areas (code_quality, api_design, layer_architecture, test_quality, all)", required = false),
                    PromptArgument(name = "language", description = "Primary programming language of the codebase", required = false)
                )
            ),
            handler = { args ->
                val path = args["path"] ?: "{path}"
                val aspects = args["aspects"] ?: "all"
                val language = args["language"] ?: "auto-detect"

                GetPromptResult(
                    description = "Comprehensive architectural analysis of the codebase",
                    messages = listOf(
                        PromptMessage(
                            role = "user",
                            content = PromptContent(text = """
                                Analyze the architecture of the codebase at: $path

                                Focus areas: $aspects
                                Language: $language

                                Please use the ArchGuard MCP tools to perform the analysis:

                                1. **Code Quality Analysis** (lint_code)
                                   - Identify code smells and anti-patterns
                                   - Check for architectural violations
                                   - Suggest improvements

                                2. **API Design Review** (lint_webapi)
                                   - Review REST API naming conventions
                                   - Check HTTP method usage
                                   - Validate URL patterns
                                   - Assess parameter counts

                                3. **SQL Quality Check** (lint_sql)
                                   - Review database schema design
                                   - Check naming conventions
                                   - Validate query patterns

                                4. **Test Quality Assessment** (lint_test)
                                   - Identify test smells
                                   - Check for ignored or empty tests
                                   - Assess assertion quality

                                5. **Layer Architecture Validation** (lint_layer)
                                   - Verify layer dependencies
                                   - Check for architectural violations
                                   - Ensure proper separation of concerns

                                For each area, provide:
                                - Summary of findings
                                - List of specific issues with locations
                                - Recommendations for improvement
                                - Priority ranking of issues
                            """.trimIndent())
                        )
                    )
                )
            }
        )

        // Fix Issues prompt
        prompts["fix-issues"] = PromptDefinition(
            prompt = Prompt(
                name = "fix-issues",
                description = "Review linting issues and suggest fixes with code examples.",
                arguments = listOf(
                    PromptArgument(name = "file", description = "File path containing the issues", required = true),
                    PromptArgument(name = "issues", description = "JSON list of issues to fix", required = true),
                    PromptArgument(name = "context", description = "Additional context about the code or project", required = false)
                )
            ),
            handler = { args ->
                val file = args["file"] ?: "{file}"
                val issues = args["issues"] ?: "[]"
                val context = args["context"] ?: ""

                GetPromptResult(
                    description = "Detailed fix suggestions for linting issues",
                    messages = listOf(
                        PromptMessage(
                            role = "user",
                            content = PromptContent(text = """
                                Review and fix the following linting issues in: $file

                                Issues to address:
                                ```json
                                $issues
                                ```

                                ${if (context.isNotEmpty()) "Additional context: $context" else ""}

                                For each issue:
                                1. Explain why this is a problem
                                2. Show the problematic code snippet
                                3. Provide a corrected code example
                                4. Explain the fix and its benefits
                                5. Note any related issues that might be affected
                            """.trimIndent())
                        )
                    )
                )
            }
        )

        // Code Review prompt
        prompts["code-review"] = PromptDefinition(
            prompt = Prompt(
                name = "code-review",
                description = "Perform a comprehensive code review using ArchGuard's architectural linting rules.",
                arguments = listOf(
                    PromptArgument(name = "path", description = "Path to the code to review", required = true),
                    PromptArgument(name = "focus", description = "Specific aspects to focus on (security, performance, maintainability, all)", required = false)
                )
            ),
            handler = { args ->
                val path = args["path"] ?: "{path}"
                val focus = args["focus"] ?: "all"

                GetPromptResult(
                    description = "Comprehensive code review with architectural focus",
                    messages = listOf(
                        PromptMessage(
                            role = "user",
                            content = PromptContent(text = """
                                Perform a comprehensive code review for: $path

                                Focus: $focus

                                Steps:
                                1. First, use get_rules to understand available linting rules
                                2. Run lint_all on the path to identify issues
                                3. For any API-related code, run lint_webapi
                                4. For database-related code, run lint_sql
                                5. For test files, run lint_test

                                Provide:
                                - Executive summary
                                - Detailed findings by category
                                - Severity-ranked list of issues
                                - Specific actionable recommendations
                            """.trimIndent())
                        )
                    )
                )
            }
        )

        // API Design Review prompt
        prompts["api-design-review"] = PromptDefinition(
            prompt = Prompt(
                name = "api-design-review",
                description = "Review REST API design for best practices and conventions.",
                arguments = listOf(
                    PromptArgument(name = "apis", description = "JSON array of API definitions with url, method, and parameters", required = true),
                    PromptArgument(name = "style", description = "API style guide to follow (rest, graphql, custom)", required = false)
                )
            ),
            handler = { args ->
                val apis = args["apis"] ?: "[]"
                val style = args["style"] ?: "rest"

                GetPromptResult(
                    description = "REST API design review and recommendations",
                    messages = listOf(
                        PromptMessage(
                            role = "user",
                            content = PromptContent(text = """
                                Review the following API design for best practices:

                                API Style: $style

                                API Definitions:
                                ```json
                                $apis
                                ```

                                Use lint_webapi tool to analyze these APIs, then provide:

                                1. **Naming Conventions**
                                   - URL path naming (lowercase, hyphenated)
                                   - Resource naming (plural nouns)
                                   - No verbs in URLs

                                2. **HTTP Method Usage**
                                   - Correct use of GET, POST, PUT, DELETE, PATCH
                                   - No HTTP methods in URL paths

                                3. **Parameter Design**
                                   - Query vs path parameters
                                   - Parameter count limits
                                   - Required vs optional

                                4. **Recommendations**
                                   - Specific fixes for each issue
                                   - Before/after examples
                                   - Priority ranking
                            """.trimIndent())
                        )
                    )
                )
            }
        )

        // SQL Review prompt
        prompts["sql-review"] = PromptDefinition(
            prompt = Prompt(
                name = "sql-review",
                description = "Review SQL queries and database schema for quality and best practices.",
                arguments = listOf(
                    PromptArgument(name = "sql", description = "SQL statements to review", required = true),
                    PromptArgument(name = "database", description = "Database type (mysql, postgresql, sqlite, etc.)", required = false)
                )
            ),
            handler = { args ->
                val sql = args["sql"] ?: ""
                val database = args["database"] ?: "generic"

                GetPromptResult(
                    description = "SQL and database schema quality review",
                    messages = listOf(
                        PromptMessage(
                            role = "user",
                            content = PromptContent(text = """
                                Review the following SQL for quality and best practices:

                                Database: $database

                                SQL:
                                ```sql
                                $sql
                                ```

                                Use lint_sql tool to analyze, then provide:

                                1. **Schema Design**
                                   - Table naming conventions (snake_case)
                                   - Column naming and types
                                   - Primary key constraints
                                   - Index recommendations

                                2. **Query Patterns**
                                   - JOIN complexity
                                   - WHERE clause optimization
                                   - LIKE pattern usage
                                   - NULL handling

                                3. **Recommendations**
                                   - Optimized query rewrites
                                   - Schema improvements
                                   - Index suggestions
                            """.trimIndent())
                        )
                    )
                )
            }
        )
    }

    /**
     * Lists all available prompts.
     */
    fun listPrompts(): List<Prompt> = prompts.values.map { it.prompt }

    /**
     * Gets a prompt by name with arguments.
     */
    fun getPrompt(name: String, arguments: Map<String, String>): GetPromptResult {
        val promptDef = prompts[name]
            ?: throw IllegalArgumentException("Prompt not found: $name")
        return promptDef.handler(arguments)
    }
}

private data class PromptDefinition(
    val prompt: Prompt,
    val handler: (Map<String, String>) -> GetPromptResult
)
