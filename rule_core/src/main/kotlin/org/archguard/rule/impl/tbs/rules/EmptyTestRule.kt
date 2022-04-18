package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.tbs.TbsRule

class EmptyTestRule: TbsRule() {
    init {
        this.name = "EmptyTestRule"
        this.key = this.javaClass.name
        this.description = "Some casing description"
        // rule for AstPath or ognl?
        this.given = listOf("$.class.function.calls <= 0")
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int) {
        if(function.isJUnitTest() && function.FunctionCalls.isEmpty()) {
            //
        }
    }
}