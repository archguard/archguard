package org.archguard.linter.rule.sql.linter.rules.expression

import net.sf.jsqlparser.statement.select.Join
import org.archguard.linter.rule.sql.linter.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class LimitJoinsRule : SqlRule() {
    init {
        this.name = "LimitJoins"
        this.key = this.javaClass.name
        this.description = "join should less than 5"
        this.severity = Severity.BLOCKER
    }

    override fun visitJoinsExpr(joins: List<Join>, context: RuleContext, callback: IssueEmit) {
        if(joins.size >= 5) {
            callback(this, IssuePosition())
        }
    }
}
