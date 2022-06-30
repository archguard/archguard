package org.archguard.doc.generator

import org.archguard.doc.generator.render.CustomJekyllFrontMatter
import org.archguard.doc.generator.render.DocHeader
import org.archguard.doc.generator.render.DocPage
import org.archguard.doc.generator.render.DocText
import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.linter.rule.testcode.TestSmellRuleSetProvider
import org.archguard.linter.rule.webapi.WebApiRuleSetProvider
import org.archguard.rule.core.Rule
import java.io.File

class RuleDocGenerator : DocGenerator() {
    fun execute() {
        val baseDir = "build" + File.separator

        generateForRule(baseDir)

    }

    private fun generateForRule(baseDir: String) {
        val sqlStr = listOf(
            CustomJekyllFrontMatter(title = "SQL", navOrder = 1, permalink = "sql").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(SqlRuleSetProvider().get().rules))
        ).joinToString("\n")

        File(baseDir + "sql.md").writeText(sqlStr)

        val apiStr = listOf(
            CustomJekyllFrontMatter(title = "Test smell", navOrder = 2, permalink = "test-smell").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(TestSmellRuleSetProvider().get().rules))
        ).joinToString("\n")

        File(baseDir + "test-smell.md").writeText(apiStr)

        val testStr = listOf(
            CustomJekyllFrontMatter(title = "Web API", navOrder = 99, permalink = "web-api").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(WebApiRuleSetProvider().get().rules))
        ).joinToString("\n")

        File(baseDir + "web-api.md").writeText(testStr)
    }

}