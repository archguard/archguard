package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.impl.tbs.TbsRule
import org.archguard.rule.impl.tbs.smellPosition

val ASSERTION_LIST = arrayOf(
    "assert",
    "should",
    "check",    // ArchUnit,
    "maynotbe", // ArchUnit,
    "is",       // RestAssured,
    "spec",     // RestAssured,
    "verify"    // Mockito,
)

class RedundantAssertionRule : TbsRule() {
    init {
        this.name = "RedundantAssertionRuleTest"
        this.key = this.javaClass.name
        this.description = "The test is contains invalid assert, like assertEquals(true, true)"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: SmellEmit) {
        val assertParametersSize = 2
        val isAssert = ASSERTION_LIST.contains(codeCall.FunctionName) || codeCall.FunctionName.startsWith("assert")
        if (isAssert) {
            if (codeCall.Parameters.size == assertParametersSize) {
                if (codeCall.Parameters[0].TypeValue == codeCall.Parameters[1].TypeValue) {
                    callback(this, codeCall.Position.smellPosition())
                }
            }
        }
    }
}
