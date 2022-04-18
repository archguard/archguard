package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Severity
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.impl.tbs.TbsRule

class SleepyTestRule : TbsRule() {
    init {
        this.name = "UnknownTest"
        this.key = this.javaClass.name
        this.description = "test not assert"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(codeCall: CodeCall, index: Int, callback: SmellEmit) {
        if (codeCall.FunctionName == "sleep" && codeCall.NodeName == "Thread") {
            callback(this)
        }
    }
}