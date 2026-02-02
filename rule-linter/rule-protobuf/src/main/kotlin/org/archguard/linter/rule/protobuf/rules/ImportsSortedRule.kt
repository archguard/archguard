package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: IMPORTS_SORTED
 *
 * Implementation strategy (minimal & customizable):
 * - collect `import ... "path";` lines
 * - compare appearance order with lexicographical order by imported path
 * - emit an issue on the first out-of-order import
 */
class ImportsSortedRule : ProtobufRule() {
    init {
        id = "IMPORTS_SORTED"
        key = id
        name = "Imports should be sorted"
        description = "import 应该按字典序排序。"
        severity = Severity.INFO
    }

    private val importDecl = Regex("^\\s*import\\s+(?:public\\s+|weak\\s+)?\"([^\"]+)\"\\s*;\\s*$")

    override fun visitFile(
        filePath: String,
        structsInFile: List<CodeDataStruct>,
        context: ProtobufRuleContext,
        callback: IssueEmit,
    ) {
        val lines = context.fileLines(filePath)
        val imports = mutableListOf<Pair<Int, String>>() // (lineNo, path)

        lines.forEachIndexed { idx, raw ->
            val line = raw.substringBefore("//")
            val m = importDecl.matchEntire(line.trim()) ?: return@forEachIndexed
            imports += (idx + 1) to m.groupValues[1]
        }

        if (imports.size <= 1) return

        val expected = imports.map { it.second }.sorted()
        val actual = imports.map { it.second }
        if (expected == actual) return

        // Find first mismatch for stable position
        val firstBadIdx = actual.indices.firstOrNull { actual[it] != expected[it] } ?: return
        val lineNo = imports[firstBadIdx].first
        emit(callback, IssuePosition(startLine = lineNo, startColumn = 1, endLine = lineNo, endColumn = 1))
    }
}

