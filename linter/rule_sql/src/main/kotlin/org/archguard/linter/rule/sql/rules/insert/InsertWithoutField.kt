package org.archguard.linter.rule.sql.rules.insert

import net.sf.jsqlparser.statement.insert.Insert
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class InsertWithoutField : SqlRule() {
    init {
        this.name = "InsertWithoutField"
        this.key = this.javaClass.name
        this.description = "insert should with fields"
        this.severity = Severity.BLOCKER
    }

    override fun visitInsert(insert: Insert, context: RuleContext, callback: IssueEmit) {
        if (insert.columns == null) {
            callback(this, IssuePosition())
        }
    }
}
