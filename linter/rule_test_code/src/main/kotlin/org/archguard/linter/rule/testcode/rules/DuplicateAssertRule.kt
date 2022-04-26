package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeFunction
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Severity

private const val MAX_ASSERTS = 5

// todo: add Rule from annotation
class DuplicateAssertRule : TbsRule() {
    init {
        this.name = "DuplicateAssertTest"
        this.key = this.javaClass.name
        this.description = "has multiple asserts"
        this.severity = Severity.WARN
    }

    override fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {
        if (function.FunctionCalls.isNotEmpty()) {
            val asserts = function.FunctionCalls.filter { isAssert(it) }.toList()
            if (asserts.size >= MAX_ASSERTS) {
                callback(this, function.Position.smellPosition())
            }
        }
    }
}
