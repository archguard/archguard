package org.archguard.linter.rule.sql.rules

import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

class UnknownColumnSizeRule : SqlRule() {
    init {
        this.id = "unknown-column-size"
        this.name = "UnknownColumnSize"
        this.key = this.javaClass.name
        this.description = "禁止使用 SELECT * 进行查询。建议按需求选择合适的字段列，杜绝直接 SELECT * 读取全部字段，减少网络带宽消耗，有效利用覆盖索引；"
        this.severity = Severity.BLOCKER
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
