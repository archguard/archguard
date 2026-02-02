package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: MESSAGE_NAMES_UPPER_CAMEL_CASE
 *
 * Implementation strategy:
 * - We don't rely on chapi's protobuf AST internals to keep it stable/customizable.
 * - Instead, scan raw file content for `message <Name> {` and validate naming.
 */
class MessageNamesUpperCamelCaseRule : ProtobufRule() {
    init {
        id = "MESSAGE_NAMES_UPPER_CAMEL_CASE"
        key = id
        name = "Message names should be UpperCamelCase"
        description = "message 名称应该是 UpperCamelCase。"
        severity = Severity.WARN
    }

    private val messageDecl = Regex("^\\s*message\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*\\{")

    override fun visitFile(
        filePath: String,
        structsInFile: List<CodeDataStruct>,
        context: ProtobufRuleContext,
        callback: IssueEmit,
    ) {
        val lines = context.fileLines(filePath)
        lines.forEachIndexed { idx, raw ->
            val line = raw.substringBefore("//") // drop line comments
            val m = messageDecl.find(line) ?: return@forEachIndexed
            val messageName = m.groupValues[1]
            if (!ProtobufNaming.isUpperCamelCase(messageName)) {
                val lineNo = idx + 1
                emit(callback, IssuePosition(startLine = lineNo, startColumn = 1, endLine = lineNo, endColumn = 1))
            }
        }
    }
}

