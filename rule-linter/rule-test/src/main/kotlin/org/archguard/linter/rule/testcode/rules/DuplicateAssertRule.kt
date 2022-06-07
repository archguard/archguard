package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeFunction
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.positionWith
import org.archguard.linter.rule.testcode.smellPosition
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Severity

private const val MAX_ASSERTS = 5

class DuplicateAssertRule : TbsRule() {
    init {
        this.id = "duplicated-assert"
        this.name = "DuplicateAssertTest"
        this.key = this.javaClass.name
        this.description = "包含了过多 Assert 语句的测试用例"
        this.message = "建议每个测试用例聚焦于一个测试场景和目的，不要企图编写一个各种场景面面俱到的巨无霸测试，这将让后期的维护更加困难"
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {
        if (function.FunctionCalls.isNotEmpty()) {
            val asserts = function.FunctionCalls.filter { isAssert(it) }.toList()
            if (asserts.size >= MAX_ASSERTS) {
                callback(this, function.Position.positionWith(function.Name))
            }
        }
    }
}
