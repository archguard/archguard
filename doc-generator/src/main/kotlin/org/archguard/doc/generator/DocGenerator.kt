package org.archguard.doc.generator

import org.archguard.doc.generator.render.DocHeader
import org.archguard.doc.generator.render.DocPage
import org.archguard.doc.generator.render.DocText
import org.archguard.rule.core.Rule
import java.io.File

abstract class DocGenerator {
    val baseDir = "build" + File.separator

    open fun execute() {}

    fun stringify(page: DocPage): String {
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