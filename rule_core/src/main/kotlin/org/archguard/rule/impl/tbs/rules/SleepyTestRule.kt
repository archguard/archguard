package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.tbs.TbsRule

class SleepyTestRule : TbsRule() {
    init {
        this.name = "UnknownTest"
        this.key = this.javaClass.name
        this.description = "test not assert"
        // rule for AstPath or ognl?
        this.given = listOf("$.class.function.calls")
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(codeCall: CodeCall, index: Int) {
        if (codeCall.FunctionName == "sleep" && codeCall.NodeName == "Thread") {
            //
        }
    }
}