package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule
import org.archguard.linter.rule.webapi.model.ContainerResource

class NoHttpMethodInUrlRule : WebApiRule() {
    init {
        this.name = "NoHttpMethodInUrl"
        this.key = this.javaClass.name
        this.description = "url resource should not equal crud "
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        val split = resource.sourceUrl.split("/")

        split.map {
            if (CRUD.contains(it)) {
                callback(this, IssuePosition())
            }
        }
    }
}
