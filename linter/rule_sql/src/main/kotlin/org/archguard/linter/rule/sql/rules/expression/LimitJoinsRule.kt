package org.archguard.linter.rule.sql.rules.expression

import net.sf.jsqlparser.statement.select.Join
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class LimitJoinsRule : SqlRule() {
    init {
        this.id = "limit-joins"
        this.name = "LimitJoins"
        this.key = this.javaClass.name
        this.description = "建议 JOIN 的表不要超过 5 个，JOIN 多表查询比较耗时时间，关联的表越多越耗时间，防止执行超时或死锁。"
        this.severity = Severity.BLOCKER
    }

    override fun visitJoinsExpr(joins: List<Join>, context: RuleContext, callback: IssueEmit) {
        if(joins.size >= 5) {
            callback(this, IssuePosition())
        }
    }
}
