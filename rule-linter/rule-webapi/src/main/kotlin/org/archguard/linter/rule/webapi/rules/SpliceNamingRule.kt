package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.model.ContainerSupply
import org.archguard.linter.rule.webapi.WebApiRule

private const val MAX_URL_NODE_LENGTH = 20
private val CHAR_REGEX = "[a-zA-Z]+".toRegex()

class SpliceNamingRule : WebApiRule() {
    init {
        this.id = "splice-naming"
        this.name = "SpliceNamingRule"
        this.key = this.javaClass.name
        this.description = "API 应该采用 - 的方式命名，单个资源的长度通常不超过 20 字符。"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerSupply, context: RuleContext, callback: IssueEmit) {
        resource.sourceUrl.split("/").forEach {
            if(CHAR_REGEX.matches(it) && it.length >= MAX_URL_NODE_LENGTH) {
                callback(this, IssuePosition())
            }
        }
    }
}
