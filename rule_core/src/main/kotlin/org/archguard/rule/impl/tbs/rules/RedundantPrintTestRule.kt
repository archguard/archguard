package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.impl.tbs.TbsLanguage
import org.archguard.rule.impl.tbs.TbsRule
import org.archguard.rule.impl.tbs.smellPosition

class RedundantPrintTestRule : TbsRule() {
    init {
        this.name = "RedundantPrintTest"
        this.key = this.javaClass.name
        this.description = "The test is contains print function, it can be removed"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: SmellEmit) {
        when(this.language) {
            TbsLanguage.JAVA -> {
                if (codeCall.NodeName == "System.out" && (codeCall.FunctionName == "println" || codeCall.FunctionName == "printf" || codeCall.FunctionName == "print")) {
                    callback(this, function.Position.smellPosition())
                }
            }
            else -> {}
        }
    }
}
