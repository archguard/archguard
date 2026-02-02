package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: FIELD_NAMES_LOWER_SNAKE_CASE
 *
 * Implementation strategy:
 * - parse raw lines for field declarations like:
 *   `repeated <type> <name> = <number> ...;`
 *   `<type> <name> = <number> ...;`
 * - ignore obvious non-field statements (`option`, `reserved`, `message`, `enum`, `service`, `rpc`, `oneof` headers)
 * - keep it intentionally conservative & customizable
 */
class FieldNamesLowerSnakeCaseRule : ProtobufRule() {
    init {
        id = "FIELD_NAMES_LOWER_SNAKE_CASE"
        key = id
        name = "Field names should be lower_snake_case"
        description = "field 名称应该是 lower_snake_case。"
        severity = Severity.WARN
    }

    private val fieldDecl = Regex(
        // optional label + type + name + = number
        "^\\s*(?:repeated\\s+)?(?:map\\s*<[^>]+>|[A-Za-z_][A-Za-z0-9_\\.]*)\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*\\d+\\b"
    )

    private val skipPrefixes = listOf(
        "syntax",
        "package",
        "import",
        "option",
        "reserved",
        "message",
        "enum",
        "service",
        "rpc",
        "oneof", // oneof header is not a field; fields inside oneof still match fieldDecl
        "extensions",
    )

    override fun visitFile(
        filePath: String,
        structsInFile: List<CodeDataStruct>,
        context: ProtobufRuleContext,
        callback: IssueEmit,
    ) {
        val lines = context.fileLines(filePath)
        lines.forEachIndexed { idx, raw ->
            val line = raw.substringBefore("//").trim()
            if (line.isEmpty()) return@forEachIndexed
            if (line.startsWith("/*") || line.startsWith("*")) return@forEachIndexed // simplistic block comment skip
            if (skipPrefixes.any { line.startsWith(it) }) return@forEachIndexed

            val m = fieldDecl.find(line) ?: return@forEachIndexed
            val fieldName = m.groupValues[1]
            if (!ProtobufNaming.isLowerSnakeCase(fieldName)) {
                val lineNo = idx + 1
                emit(callback, IssuePosition(startLine = lineNo, startColumn = 1, endLine = lineNo, endColumn = 1))
            }
        }
    }
}

