package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.ContainerRule
import org.archguard.linter.rule.webapi.model.ContainerResource

class StartWithoutCrudRule : ContainerRule() {
    init {
        this.name = "StartWithoutCrudRule"
        this.key = this.javaClass.name
        this.description = "url should not end with crud (like /create)"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        val split = resource.sourceUrl.split("/")
        split.forEach { node ->
            CRUD.forEach {
                if (node != it && node.lowercase().startsWith(it)) {
                    callback(this, IssuePosition())
                }
            }
        }
    }
}