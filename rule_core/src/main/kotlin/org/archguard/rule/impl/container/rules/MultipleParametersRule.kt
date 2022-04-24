package org.archguard.rule.impl.container.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.container.ContainerRule
import org.archguard.rule.impl.container.model.ContainerResource

class MultipleParametersRule : ContainerRule() {
    init {
        this.name = "MultipleParameters"
        this.key = this.javaClass.name
        this.description = "api should"
        this.message = "error samples: /api/book/{bookType}/{bookId}/{bookChildType}/{childId}"
        this.severity = Severity.INFO
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        var paramsCount = 0
        resource.sourceUrl.split("/").forEach {
            if(it.startsWith("{") && it.endsWith("}")) {
                paramsCount++
            }
        }

        if(paramsCount >= 3) {
            callback(this, IssuePosition())
        }
    }
}
