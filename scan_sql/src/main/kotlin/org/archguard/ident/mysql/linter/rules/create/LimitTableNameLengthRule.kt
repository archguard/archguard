package org.archguard.ident.mysql.linter.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.ident.mysql.linter.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

private const val TABLE_LENGTH = 32

class LimitTableNameLengthRule : SqlRule() {
    init {
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

