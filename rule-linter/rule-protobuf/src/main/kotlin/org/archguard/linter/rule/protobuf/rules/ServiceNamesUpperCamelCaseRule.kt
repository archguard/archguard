package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: SERVICE_NAMES_UPPER_CAMEL_CASE
 */
class ServiceNamesUpperCamelCaseRule : ProtobufRule() {
    init {
        id = "SERVICE_NAMES_UPPER_CAMEL_CASE"
        key = id
        name = "Service names should be UpperCamelCase"
        description = "service 名称应该是 UpperCamelCase。"
        severity = Severity.WARN
    }

    override fun visitFile(filePath: String, structsInFile: List<CodeDataStruct>, context: ProtobufRuleContext, callback: IssueEmit) {
        val serviceStructs = structsInFile.filter { it.Functions.isNotEmpty() }
        serviceStructs.forEach { service ->
            val name = service.NodeName
            if (name.isBlank()) return@forEach
            if (!ProtobufNaming.isUpperCamelCase(name)) {
                val line = service.Position.StartLine
                emit(callback, IssuePosition(startLine = line, startColumn = 1, endLine = line, endColumn = 1))
            }
        }
    }
}

