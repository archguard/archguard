package org.archguard.rule.impl.container.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.container.model.ContainerResource
import org.archguard.rule.impl.container.ContainerRule

private const val MAX_URL_NODE_LENGTH = 20
private val CHAR_REGEX = "[a-zA-Z]+".toRegex()

class UrlSplitNamingRule : ContainerRule() {
    init {
        this.name = "UrlSplitNamingRule"
        this.key = this.javaClass.name
        this.description = "resource node should less than 20"
        this.severity = Severity.WARN
    }

    override fun visitResource(resource: ContainerResource, context: RuleContext, callback: IssueEmit) {
        resource.sourceUrl.split("/").forEach {
            if(CHAR_REGEX.matches(it) && it.length >= MAX_URL_NODE_LENGTH) {
                callback(this, IssuePosition())
            }
        }
    }
}