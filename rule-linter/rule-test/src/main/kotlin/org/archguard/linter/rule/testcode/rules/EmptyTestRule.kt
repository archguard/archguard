package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.positionWith
import org.archguard.linter.rule.testcode.smellPosition

class EmptyTestRule: TbsRule() {
    init {
        this.id = "empty-test"
        this.name = "EmptyTest"
        this.key = this.javaClass.name
        this.description = "Test is without any code"
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {
        if(function.isJUnitTest() && function.FunctionCalls.isEmpty()) {
            callback(this, function.Position.positionWith(function.Name))
        }
    }
}