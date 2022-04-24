package org.archguard.rule.impl.container.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.container.ContainerRule

class MinFeatureApiRule : ContainerRule() {
    init {
        this.name = "MinFeature"
        this.key = this.javaClass.name
        this.description = "api should have single ability"
        this.severity = Severity.INFO
    }

    override fun visitSegment(it: String, context: RuleContext, callback: IssueEmit) {
        if (it.contains("-and-") || it.contains("-with-")) {
            callback(this, IssuePosition())
        }
    }
}
