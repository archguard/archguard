package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: PACKAGE_NAME_LOWER_CASE
 */
class PackageNameLowerCaseRule : ProtobufRule() {
    init {
        id = "PACKAGE_NAME_LOWER_CASE"
        key = id
        name = "Package name should be lower case"
        description = "package 名称应该只包含小写字母/数字/下划线，并用 '.' 分段。"
        severity = Severity.WARN
    }

    private val pkgRegex = Regex("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$")
    private val pkgLineRegex = Regex("^\\s*package\\s+([^;]+);\\s*$")

    override fun visitFile(filePath: String, structsInFile: List<CodeDataStruct>, context: ProtobufRuleContext, callback: IssueEmit) {
        val pkg = structsInFile.mapNotNull { it.Package.takeIf { p -> p.isNotBlank() } }.distinct().firstOrNull()
            ?: extractPackageFromFile(context.fileLines(filePath))
            ?: return

        if (!pkgRegex.matches(pkg)) {
            val lineNo = findPackageLine(context.fileLines(filePath)) ?: 1
            emit(callback, IssuePosition(startLine = lineNo, startColumn = 1, endLine = lineNo, endColumn = 1))
        }
    }

    private fun extractPackageFromFile(lines: List<String>): String? {
        return lines.asSequence()
            .mapNotNull { pkgLineRegex.matchEntire(it)?.groupValues?.getOrNull(1) }
            .firstOrNull()
            ?.trim()
    }

    private fun findPackageLine(lines: List<String>): Int? {
        lines.forEachIndexed { idx, line ->
            if (pkgLineRegex.matches(line)) return idx + 1
        }
        return null
    }
}

