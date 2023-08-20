package org.archguard.scanner.analyser.markdown

import org.commonmark.ext.gfm.tables.TableCell
import org.commonmark.ext.gfm.tables.TableHead
import org.commonmark.node.*
import org.commonmark.parser.Parser

class MarkdownVerifier {
    private val parser: Parser = createParser()

    fun tableVerifier(markdown: String, headers: List<String>): Boolean {
        var content = markdown
        // 1. if there is inside code block, remove block, like: ```markdown xxx ```
        if (markdown.contains("```")) {
            val code = parseMarkdownCodeBlock(markdown)
            if (code.isEmpty()) {
                return false
            }

            content = code.first()
        }

        // 2. if no table, return false
        if (!markdown.contains("|")) {
            return false
        }

        // 3. parse table header and verify table header
        val tableHeader = parseTableHeader(content)
        if (tableHeader.size != headers.size) {
            return false
        }

        // 4. verify table header
        tableHeader.forEachIndexed { index, s ->
            if (s != headers[index]) {
                return false
            }
        }

        return true
    }

    private fun parseTableHeader(content: String): List<String> {
        val node = parser.parse(content)
        val visitor = TableHeaderVisitor()
        node.accept(visitor)
        return visitor.headers
    }

    private fun parseMarkdownCodeBlock(markdown: String): List<String> {
        val node = parser.parse(markdown)
        val visitor = CodeFilter(lang = "markdown")
        node.accept(visitor)
        return visitor.code
    }
}

internal class TableHeaderVisitor : AbstractVisitor() {
    val headers = mutableListOf<String>()
    private var isBeforeHeadLine = true
    override fun visit(customNode: CustomNode?) {
        super.visit(customNode)

        when (customNode) {
            is TableHead -> {
                isBeforeHeadLine = false
            }

            is TableCell -> {
                if (isBeforeHeadLine) {
                    headers += (customNode.firstChild as Text).literal
                }
            }
        }
    }

    override fun visit(customBlock: CustomBlock?) {
        super.visit(customBlock)
    }
}

internal class CodeFilter(val lang: String) : AbstractVisitor() {
    var code = listOf<String>()

    override fun visit(fencedCodeBlock: FencedCodeBlock?) {
        if (fencedCodeBlock?.literal != null) {
            if (fencedCodeBlock.info == lang) {
                this.code += fencedCodeBlock.literal
            }
        }
    }
}
