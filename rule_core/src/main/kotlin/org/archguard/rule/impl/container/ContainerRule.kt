package org.archguard.rule.impl.container

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.impl.container.model.ContainerResource

open class ContainerRule : Rule() {
    open fun visitResources(resources: Array<ContainerResource>, context: RuleContext, callback: IssueEmit) {
        resources.forEach {
            this.visitResource(it, context, callback)
        }
    }

    open fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        resource.sourceUrl.split("/").forEach {
            this.visitSegment(it, context, callback)
        }
    }

    open fun visitSegment(it: String, context: RuleContext, callback: IssueEmit) {

    }
}