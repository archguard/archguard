package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition


class RedundantAssertionRule : TbsRule() {
    init {
        this.name = "RedundantAssertionTest"
        this.key = this.javaClass.name
        this.description = "The test is contains invalid assert, like assertEquals(true, true)"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        val assertParametersSize = 2
        if (isAssert(codeCall)) {
            if (codeCall.Parameters.size == assertParametersSize) {
                if (codeCall.Parameters[0].TypeValue == codeCall.Parameters[1].TypeValue) {
                    callback(this, codeCall.Position.smellPosition())
                }
            }
        }
    }
}
