package org.archguard.linter.rule.protobuf.rules

import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity
import java.io.File

/**
 * Similar to protolint: FILE_NAMES_LOWER_SNAKE_CASE
 */
class FileNamesLowerSnakeCaseRule : ProtobufRule() {
    init {
        id = "FILE_NAMES_LOWER_SNAKE_CASE"
        key = id
        name = "File names should be lower_snake_case.proto"
        description = "Protobuf 文件名应该是 lower_snake_case.proto。"
        severity = Severity.WARN
    }

    override fun visitFile(filePath: String, structsInFile: List<chapi.domain.core.CodeDataStruct>, context: ProtobufRuleContext, callback: IssueEmit) {
        val fileName = File(filePath).name
        if (!fileName.endsWith(".proto")) return

        val base = fileName.removeSuffix(".proto")
        if (!ProtobufNaming.isLowerSnakeCase(base)) {
            emit(callback, IssuePosition(startLine = 1, startColumn = 1, endLine = 1, endColumn = 1))
        }
    }
}

