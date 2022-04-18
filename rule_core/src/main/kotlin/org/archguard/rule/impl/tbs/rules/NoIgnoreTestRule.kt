package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeAnnotation
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.tbs.TbsRule

class NoIgnoreTestRule : TbsRule() {
    init {
        this.name = "NoIgnoreTest"
        this.key = this.javaClass.name
        this.description = "The test is ignore or disabled"
        // rule for AstPath or ognl?
        this.given = listOf("$.class.function.annotations contains 'Ignore'")
        this.severity = Severity.WARN
    }

    override fun visitAnnotation(annotation: CodeAnnotation, index: Int) {
        if(annotation.Name == "Ignore" || annotation.Name == "Disabled") {
            //
        }
    }
}