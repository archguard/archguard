package org.archguard.doc.generator

import org.archguard.doc.generator.render.CustomJekyllFrontMatter
import org.archguard.doc.generator.render.DocHeader
import org.archguard.doc.generator.render.DocPage
import org.archguard.doc.generator.render.DocText
import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.linter.rule.testcode.TestSmellProvider
import org.archguard.linter.rule.webapi.WebApiRuleSetProvider
import org.archguard.rule.core.Rule
import java.io.File

class RuleDocGenerator {
    fun execute() {
        val sqlStr = listOf(
            CustomJekyllFrontMatter(title = "SQL", navOrder = 1, permalink = "sql").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(SqlRuleSetProvider().get().rules))
        ).joinToString("\n")

        File("build" + File.separator + "sql.md").writeText(sqlStr)

        val apiStr = listOf(
            CustomJekyllFrontMatter(title = "Test smell", navOrder = 2, permalink = "test-smell").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(TestSmellProvider().get().rules))
        ).joinToString("\n")

        File("build" + File.separator + "test-smell.md").writeText(apiStr)

        val testStr = listOf(
            CustomJekyllFrontMatter(title = "Web API", navOrder = 99, permalink = "web-api").toMarkdown(),
            this.toMarkdown(this.nodeFromRules(WebApiRuleSetProvider().get().rules))
        ).joinToString("\n")

        File("build" + File.separator + "web-api.md").writeText(testStr)

    }

    fun nodeFromRules(rules: Array<out Rule>): DocPage {
        val page = DocPage(content = listOf())
        rules.forEach {
            page.content += DocHeader(it.id, listOf(), level = 2)
            page.content += DocText("className: " + it.key)
            page.content += DocText("description: " + it.description)

            if(it.message.isNotEmpty()) {
                page.content += DocText("suggest: " + it.message)
            }
        }

        return page
    }

    fun toMarkdown(page: DocPage): String {
        var output = ""
        page.content.forEach {
            when (it) {
                is DocText -> {
                    output += "${it.text}\n"
                }
                is DocHeader -> {
                    output += "#".repeat(it.level) + " " + it.title + "\n"
                }
            }
            output += "\n"
        }

        return output
    }
}