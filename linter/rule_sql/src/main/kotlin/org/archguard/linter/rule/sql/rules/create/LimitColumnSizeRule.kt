package org.archguard.linter.rule.sql.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class LimitColumnSizeRule : SqlRule() {
    private val MAX_COLUMN_SIZE = 20

    init {
        this.name = "LimitColumnSize"
        this.key = this.javaClass.name
        this.description = "table column should less than 20"
        this.severity = Severity.INFO
    }

    override fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {
        if (createStmt.columnDefinitions != null) {
            if (createStmt.columnDefinitions.size >= MAX_COLUMN_SIZE) {
                callback(this, IssuePosition())
            }
        }
    }
}
