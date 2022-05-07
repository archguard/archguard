package org.archguard.linter.rule.sql.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class LimitColumnSizeRule : SqlRule() {
    private val MAX_COLUMN_SIZE = 20
    private var maxColumnSize: Int = 0

    init {
        this.id = "limit-column-size"
        this.name = "LimitColumnSize"
        this.key = this.javaClass.name
        this.maxColumnSize = this.MAX_COLUMN_SIZE
        this.description = "表的字段应该控制在 20 以内。"
        this.severity = Severity.INFO
    }

    override fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {
        if (createStmt.columnDefinitions != null) {
            if (createStmt.columnDefinitions.size >= maxColumnSize) {
                callback(this, IssuePosition())
            }
        }
    }
}
