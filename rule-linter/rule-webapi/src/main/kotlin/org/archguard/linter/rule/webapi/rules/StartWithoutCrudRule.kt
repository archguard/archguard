package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule
import org.archguard.model.ContainerSupply

class StartWithoutCrudRule : WebApiRule() {
    init {
        this.id = "start-without-crud"
        this.name = "StartWithoutCrudRule"
        this.key = this.javaClass.name
        this.description = "URL 不应该以 CRUD 开头。错误示例： `/api/getbook`， 正确示例： `GET /api/book`"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerSupply, context: RuleContext, callback: IssueEmit) {
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
