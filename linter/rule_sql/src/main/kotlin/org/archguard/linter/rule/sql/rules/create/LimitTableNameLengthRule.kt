package org.archguard.linter.rule.sql.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

private const val TABLE_LENGTH = 32

class LimitTableNameLengthRule : SqlRule() {
    init {
        this.id = "limit-table-name-length"
        this.name = "LimitTableNameLength"
        this.key = this.javaClass.name
        this.description = "table name should < 32"
        this.severity = Severity.INFO
    }

    override fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {
        if (createStmt.table.name.length >= TABLE_LENGTH) {
            callback(this, IssuePosition())
        }
    }
}

