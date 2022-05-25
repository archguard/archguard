package org.archguard.linter.rule.sql.rules.insert

import net.sf.jsqlparser.statement.insert.Insert
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class InsertWithoutField : SqlRule() {
    init {
        this.id = "insert-without-field"
        this.name = "InsertWithoutField"
        this.key = this.javaClass.name
        this.description = "INSERT 应该包含字段键名。"
        this.message = "正确示例：`INSERT INTO system (`name`) VALUES ('archguard');`\n"
        this.severity = Severity.BLOCKER
    }

    override fun visitInsert(insert: Insert, context: RuleContext, callback: IssueEmit) {
        if (insert.columns == null) {
            callback(this, IssuePosition())
        }
    }
}
