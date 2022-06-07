package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.common.Language
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.positionWith
import org.archguard.linter.rule.testcode.smellPosition

class RedundantPrintRule : TbsRule() {
    init {
        this.id = "redundant-print"
        this.name = "RedundantPrintTest"
        this.key = this.javaClass.name
        this.description = "包含了过多调试打印信息的测试用例"
        this.message = "自动化测试用例中，应该使用自动的 Assert 语句，替代需要人眼观察的 Print"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        when(this.language) {
            Language.JAVA -> {
                val isPrint =
                    codeCall.FunctionName == "println" || codeCall.FunctionName == "printf" || codeCall.FunctionName == "print"
                if (codeCall.NodeName == "System.out" && isPrint) {
                    callback(this, function.Position.positionWith(function.Name))
                }
            }
            else -> {}
        }
    }
}
