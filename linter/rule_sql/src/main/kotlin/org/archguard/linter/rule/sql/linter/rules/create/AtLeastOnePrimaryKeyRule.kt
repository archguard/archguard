package org.archguard.linter.rule.sql.linter.rules.create

import net.sf.jsqlparser.statement.create.table.CreateTable
import org.archguard.linter.rule.sql.linter.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class AtLeastOnePrimaryKeyRule : SqlRule() {
    init {
        this.name = "AtLeastOnePrimaryKey"
        this.key = this.javaClass.name
        this.description = "at least one primary key"
        this.severity = Severity.INFO
    }

    override fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {
        if (createStmt.columnDefinitions != null) {
            var hasPrimaryKey = false
            createStmt.columnDefinitions.forEach {
                if (it.columnSpecs != null) {
                    val specs = it.columnSpecs.joinToString(" ").lowercase()
                    if (specs.contains("primary")) {
                        hasPrimaryKey = true
                    }
                }
            }

            if (!hasPrimaryKey) {
                callback(this, IssuePosition())
            }
        }
    }
}
