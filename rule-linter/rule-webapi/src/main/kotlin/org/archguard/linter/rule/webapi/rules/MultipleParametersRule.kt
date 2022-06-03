package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule
import org.archguard.scanner.core.sourcecode.ContainerSupply

class MultipleParametersRule : WebApiRule() {
    init {
        this.id = "multiple-parameters"
        this.name = "MultipleParameters"
        this.key = this.javaClass.name
        this.description = "URL 中的参数不宜超过 3 个，可以放到 body 中。错误示例：/api/book/{bookType}/{bookId}/{bookChildType}/{childId}"
        this.severity = Severity.INFO
    }

    override fun visitResource(resource: ContainerSupply, context: RuleContext, callback: IssueEmit) {
        var paramsCount = 0
        resource.sourceUrl.split("/").forEach {
            if(it.startsWith("{") && it.endsWith("}")) {
                paramsCount++
            }
        }

        if(paramsCount >= 3) {
            callback(this, IssuePosition())
        }
    }
}
