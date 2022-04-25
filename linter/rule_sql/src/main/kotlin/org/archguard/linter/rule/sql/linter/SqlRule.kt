package org.archguard.linter.rule.sql.linter

import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Join
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext

open class SqlRule: Rule() {
    fun visit(stmts: List<Statement>, context: RuleContext, callback: IssueEmit) {
        stmts.forEach {
            when(it) {
                is CreateTable -> {
                    this.visitCreateTable(it, context, callback)
                }
                is Select -> {
                    this.visitSelect(it, context, callback)
                }
                is Insert -> {
                    this.visitInsert(it, context, callback)
                }
                is Update -> {
                    this.visitUpdate(it, context, callback)
                }
                is Delete -> {
                    this.visitDelete(it, context, callback)
                }
            }
        }
    }

    open fun visitInsert(insert: Insert, context: RuleContext, callback: IssueEmit) {}
    open fun visitSelect(select: Select, context: RuleContext, callback: IssueEmit) {
        when (val selectBody = select.selectBody) {
            is PlainSelect -> {
                if (selectBody.joins != null) {
                    this.visitJoinsExpr(selectBody.joins, context, callback)
                }
            }
        }
    }

    open fun visitUpdate(update: Update, context: RuleContext, callback: IssueEmit) {
        if (update.joins != null) {
            this.visitJoinsExpr(update.joins, context, callback)
        }
    }
    open fun visitDelete(delete: Delete, context: RuleContext, callback: IssueEmit) {}

    open fun visitCreateTable(createStmt: CreateTable, context: RuleContext, callback: IssueEmit) {}

    open fun visitJoinsExpr(joins: List<Join>, context: RuleContext, callback: IssueEmit) {}
}