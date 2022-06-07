package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.positionWith
import org.archguard.linter.rule.testcode.smellPosition

class UnknownTestRule : TbsRule() {
    private var hasAssert: Boolean = false

    init {
        this.id = "unknown-test"
        this.name = "UnknownTest"
        this.key = this.javaClass.name
        this.description = "缺乏了自动校验的测试用例，这将无法达到自动验证结果的目的。"
        this.message = "为每个测试用例都添加足够的自动校验 Assert 语句"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        if(isAssert(codeCall)) {
            this.hasAssert = true
        }
    }

    override fun beforeVisitFunction(function: CodeFunction, callback: IssueEmit) {
        this.hasAssert = false
    }

    override fun afterVisitFunction(function: CodeFunction, callback: IssueEmit) {
        if(!this.hasAssert) {
            callback(this, function.Position.positionWith(function.Name))
        }
    }
}
