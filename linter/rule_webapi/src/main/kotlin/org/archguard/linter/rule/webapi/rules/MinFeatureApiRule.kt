package org.archguard.linter.rule.webapi.rules

import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.linter.rule.webapi.WebApiRule

class MinFeatureApiRule : WebApiRule() {
    init {
        this.id = "min-feature"
        this.name = "MinFeature"
        this.key = this.javaClass.name
        this.description = "API 应该保持单一职责的原则，一个 API 只做一件事。"
        this.message = "> 让每个程序只做好一件事。要完成一项工作，构造全新的比在复杂的旧程序里添加新特性更好。 —— McIlroy, Pinson 和 Tague。\n" +
                "\n" +
                "—— 《Google 系统架构解密：构建安全可靠的系统》\n"
        this.severity = Severity.INFO
    }

    override fun visitSegment(it: String, context: RuleContext, callback: IssueEmit) {
        if (it.contains("-and-") || it.contains("-with-")) {
            callback(this, IssuePosition())
        }
    }
}
