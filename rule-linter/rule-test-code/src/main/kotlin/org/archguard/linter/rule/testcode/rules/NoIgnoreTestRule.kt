package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition

class NoIgnoreTestRule : TbsRule() {
    init {
        this.id = "no-ignore-test"
        this.name = "IgnoreTest"
        this.key = this.javaClass.name
        this.description = "被忽略（Ignore、Disabled）的测试用例"
        this.message = "当需求修改导致测试用例失败、失效了，应该尽快修复或移除而不是忽略。"
        this.severity = Severity.WARN
    }

    override fun visitFunctionAnnotation(function: CodeFunction, annotation: CodeAnnotation, index: Int, callback: IssueEmit) {
        if (annotation.Name == "Ignore" || annotation.Name == "Disabled") {
            callback(this, function.Position.smellPosition())
        }
    }
}
