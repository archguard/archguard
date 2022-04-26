package org.archguard.linter.rule.sql.rules

import net.sf.jsqlparser.expression.StringValue
import net.sf.jsqlparser.expression.operators.relational.LikeExpression
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import org.archguard.linter.rule.sql.SqlRule
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleDecl
import org.archguard.rule.core.Severity

@RuleDecl(
    name = "LikeStartWithoutPercent",
    description = "使用 like 模糊匹配时，查找字符串中通配符 % 放首位会导致无法使用索引。"
)
class LikeStartWithoutPercentRule : SqlRule() {
    init {
        this.id = "like-start-without-percent"
        this.name = "LikeStartWithoutPercent"
        this.key = this.javaClass.name
        this.description = "使用 like 模糊匹配时，查找字符串中通配符 % 放首位会导致无法使用索引。"
        this.severity = Severity.INFO
    }

    override fun visitSelect(select: Select, context: RuleContext, callback: IssueEmit) {
        when (val selectBody = select.selectBody) {
            is PlainSelect -> {
                when(val where = selectBody.where) {
                    is LikeExpression -> {
                        when(val right = where.rightExpression) {
                            is StringValue -> {
                                if(right.value.startsWith("%")) {
                                    callback(this, IssuePosition())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
