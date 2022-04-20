package org.archguard.rule.impl.container

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.impl.container.model.ContainerResource

open class ContainerRule: Rule() {
    open fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {

    }
}