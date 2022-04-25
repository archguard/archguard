package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.ContainerRule
import org.archguard.linter.rule.webapi.model.ContainerResource

class NoCrudEndRule: ContainerRule() {
    init {
        this.id = "no-crud-end"
        this.name = "NoCrudEndRule"
        this.key = this.javaClass.name
        this.description = "url should not end with crud (like /create)"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        val split = resource.sourceUrl.split("/")
        if(CRUD.contains(split.last().lowercase())) {
            callback(this, IssuePosition())
        }
    }
}