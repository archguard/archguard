package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.impl.common.Language
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition

class RedundantPrintRule : TbsRule() {
    init {
        this.name = "RedundantPrintTest"
        this.key = this.javaClass.name
        this.description = "The test is contains print function, it can be removed"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        when(this.language) {
            Language.JAVA -> {
                if (codeCall.NodeName == "System.out" && (codeCall.FunctionName == "println" || codeCall.FunctionName == "printf" || codeCall.FunctionName == "print")) {
                    callback(this, codeCall.Position.smellPosition())
                }
            }
            else -> {}
        }
    }
}
