package org.archguard.linter.rule.sql.linter.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.linter.rule.sql.linter.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity
import org.archguard.rule.impl.common.Casing

class SnakeCasingRule : SqlRule() {
    init {
        this.name = "SnakeCasing"
        this.key = this.javaClass.name
        this.description = "table name and field should be use-kebab"
        this.severity = Severity.INFO
    }

    override fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {
        if (!Casing.is_nake(createStmt.table.name)) {
            callback(this, IssuePosition())
        }

        if (createStmt.columns != null) {
            createStmt.columns.forEach {
                if (!Casing.is_nake(it)) {
                    callback(this, IssuePosition())
                }
            }
        }
    }
}
