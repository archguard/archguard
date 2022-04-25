package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition

class SleepyTestRule : TbsRule() {
    init {
        this.name = "SleepyTest"
        this.key = this.javaClass.name
        this.description = "is contains Thread.sleep, it wil slow down test speeds"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        if (codeCall.FunctionName == "sleep" && codeCall.NodeName == "Thread") {
            callback(this, codeCall.Position.smellPosition())
        }
    }
}