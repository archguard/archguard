package org.archguard.rule.impl.container.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.container.ContainerRule
import org.archguard.rule.impl.container.model.ContainerResource


private val CRUD = arrayOf("create", "update", "refresh", "delete", "get", "put")

class EndWithoutCrudRule: ContainerRule() {
    init {
        this.name = "EndWithoutCrudRule"
        this.key = this.javaClass.name
        this.description = "url should not end with crud (like /create)"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        val split = resource.sourceUrl.split("/")
        if(CRUD.contains(split.last())) {
            callback(this, IssuePosition())
        }
    }
}