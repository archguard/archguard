package org.archguard.linter.rule.sql.rules

import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class UnknownNumberColumnRule : SqlRule() {
    init {
        this.name = "UnknownNumberColumn"
        this.key = this.javaClass.name
        this.description = "could not use * as list SELECT * from xxx"
        this.severity = Severity.WARN
    }

    override fun visitSelect(select: Select, context: RuleContext, callback: IssueEmit) {
        when (val selectBody = select.selectBody) {
            is PlainSelect -> {
                if (selectBody.selectItems.size == 1 && selectBody.selectItems[0].toString() == "*") {
                    callback(this, IssuePosition())
                }
            }
        }
    }
}
