package org.archguard.doc.generator

import org.archguard.doc.generator.render.DocHeader
import org.archguard.doc.generator.render.DocPage
import org.archguard.doc.generator.render.DocText
import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.rule.core.Rule

class RuleDocGenerator {
    fun sqlNodes(): DocPage {
        // 1. load ruleset to maps
        val ruleSetProvider = SqlRuleSetProvider()
        val rules = ruleSetProvider.get().rules

        return nodeFromRules(rules)
        // 2. load test as error cases
    }

    fun nodeFromRules(rules: Array<out Rule>): DocPage {
        val page = DocPage(content = listOf())
        rules.forEach {
            page.content += DocHeader(it.id, listOf(), level = 2)
            page.content += DocText("className: " + it.key)
            page.content += DocText("description: " + it.description)
        }

        return page
    }

    fun toMarkdown(page: DocPage): String {
        var output: String = ""
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