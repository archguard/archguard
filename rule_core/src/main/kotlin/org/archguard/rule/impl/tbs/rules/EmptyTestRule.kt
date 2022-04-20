package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.impl.tbs.TbsRule
import org.archguard.rule.impl.tbs.smellPosition

class EmptyTestRule: TbsRule() {
    init {
        this.name = "EmptyTest"
        this.key = this.javaClass.name
        this.description = "Test is without any code"
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {
        if(function.isJUnitTest() && function.FunctionCalls.isEmpty()) {
            callback(this, function.Position.smellPosition())
        }
    }
}