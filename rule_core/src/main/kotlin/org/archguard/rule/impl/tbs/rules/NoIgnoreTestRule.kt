package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.impl.tbs.TbsRule
import org.archguard.rule.impl.tbs.smellPosition

class NoIgnoreTestRule : TbsRule() {
    init {
        this.name = "IgnoreTest"
        this.key = this.javaClass.name
        this.description = "The test is ignore or disabled"
        this.severity = Severity.WARN
    }

    override fun visitFunctionAnnotation(function: CodeFunction, annotation: CodeAnnotation, index: Int, callback: IssueEmit) {
        if (annotation.Name == "Ignore" || annotation.Name == "Disabled") {
            callback(this, function.Position.smellPosition())
        }
    }
}
