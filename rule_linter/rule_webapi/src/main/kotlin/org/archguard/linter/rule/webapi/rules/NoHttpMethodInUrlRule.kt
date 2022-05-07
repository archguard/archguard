package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule
import org.archguard.scanner.core.client.dto.ContainerResource

class NoHttpMethodInUrlRule : WebApiRule() {
    init {
        this.id = "no-http-method-in-url"
        this.name = "NoHttpMethodInUrl"
        this.key = this.javaClass.name
        this.description = "URL 中不应该包含 CRUD 方法。"
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
