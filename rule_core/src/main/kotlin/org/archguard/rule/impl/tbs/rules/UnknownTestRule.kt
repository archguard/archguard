package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.impl.tbs.TbsRule
import org.archguard.rule.impl.tbs.smellPosition

class UnknownTestRule : TbsRule() {
    init {
        this.name = "UnknownTest"
        this.key = this.javaClass.name
        this.description = "don't have assert"
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int, callback: SmellEmit) {
        if (function.FunctionCalls.isNotEmpty()) {
            val hasAsserts = function.FunctionCalls.filter { isAssert(it) }.toList().isNotEmpty()
            if (!hasAsserts) {
                callback(this, function.Position.smellPosition())
            }
        }
    }
}
