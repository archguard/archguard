package org.archguard.linter.rule.webapi

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.scanner.core.sourcecode.ContainerSupply

open class WebApiRule : Rule() {
    open fun visitResource(resource: ContainerSupply, context: RuleContext, callback: IssueEmit) {
        resource.sourceUrl.split("/").forEach {
            this.visitSegment(it, context, callback)
        }
    }

    open fun visitSegment(it: String, context: RuleContext, callback: IssueEmit) {

    }
}
