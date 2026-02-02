package org.archguard.linter.rule.protobuf.rules

import chapi.domain.core.CodeDataStruct
import org.archguard.linter.rule.protobuf.ProtobufRule
import org.archguard.linter.rule.protobuf.ProtobufRuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Severity

/**
 * Similar to protolint: RPC_NAMES_UPPER_CAMEL_CASE
 */
class RpcNamesUpperCamelCaseRule : ProtobufRule() {
    init {
        id = "RPC_NAMES_UPPER_CAMEL_CASE"
        key = id
        name = "RPC names should be UpperCamelCase"
        description = "rpc 名称应该是 UpperCamelCase。"
        severity = Severity.WARN
    }

    override fun visitFile(filePath: String, structsInFile: List<CodeDataStruct>, context: ProtobufRuleContext, callback: IssueEmit) {
        val serviceStructs = structsInFile.filter { it.Functions.isNotEmpty() }
        for (service in serviceStructs) {
            for (fn in service.Functions) {
                val rpcName = fn.Name
                if (rpcName.isBlank()) continue
                if (!ProtobufNaming.isUpperCamelCase(rpcName)) {
                    val line = fn.Position.StartLine
                    emit(callback, IssuePosition(startLine = line, startColumn = 1, endLine = line, endColumn = 1))
                }
            }
        }
    }
}

