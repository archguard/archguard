package org.archguard.rule.impl.tbs.rules

import chapi.domain.core.CodeAnnotation
import org.archguard.rule.core.Severity
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.impl.tbs.TbsRule

class NoIgnoreTestRule : TbsRule() {
    init {
        this.name = "NoIgnoreTest"
        this.key = this.javaClass.name
        this.description = "The test is ignore or disabled"
        this.severity = Severity.WARN
    }

    override fun visitAnnotation(annotation: CodeAnnotation, index: Int, callback: SmellEmit) {
        if(annotation.Name == "Ignore" || annotation.Name == "Disabled") {
            callback(this)
        }
    }
}