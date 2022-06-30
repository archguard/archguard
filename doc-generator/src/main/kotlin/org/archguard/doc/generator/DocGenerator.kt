package org.archguard.doc.generator

import org.archguard.doc.generator.render.DocHeader
import org.archguard.doc.generator.render.DocPage
import org.archguard.doc.generator.render.DocText
import org.archguard.rule.core.Rule

open class DocGenerator {
    fun nodeFromRules(rules: Array<out Rule>): DocPage {
        val page = DocPage(content = listOf())
        rules.forEach {
            page.content += DocHeader(it.id, listOf(), level = 2)
            page.content += DocText("className: " + it.key)
            page.content += DocText("description: " + it.description)

            page.content += DocText("severity: " + it.severity)

            if (it.message.isNotEmpty()) {
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