package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule
import org.archguard.scanner.core.client.dto.ContainerResource

class NoCrudEndRule: WebApiRule() {
    init {
        this.id = "no-crud-end"
        this.name = "NoCrudEndRule"
        this.key = this.javaClass.name
        this.description = "URL 不应该以 CRUD 结尾，错误的方式 `/api/book/get`，正确的方式： `GET /api/book`"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        val split = resource.sourceUrl.split("/")
        if(CRUD.contains(split.last().lowercase())) {
            callback(this, IssuePosition())
        }
    }
}