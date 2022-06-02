package org.archguard.linter.rule.sql

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statements
import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation

data class CodeSqlStmt(
    val packageName: String = "",
    val className: String = "",
    val functionName: String = "",
    val statements: List<Statements> = listOf()
)

class DatamapRuleVisitor(relations: List<CodeDatabaseRelation>): RuleVisitor(relations) {
    private val codeSqlStmts: List<CodeSqlStmt> = relations.map { relation ->
        val stmts = relation.sqls.mapNotNull {
            try {
                CCJSqlParserUtil.parseStatements(it)
            } catch (e: Exception) {
                null
            }
        }

        CodeSqlStmt(relation.packageName, relation.className, relation.functionName, stmts)
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach { rule ->
                // todo: cast by plugins
                val sqlRule = rule as SqlRule

                codeSqlStmts.map {
                    sqlRule.visit(it.statements, context, fun(rule: Rule, position: IssuePosition) {
                        results += Issue(
                            position,
                            ruleId = rule.key,
                            name = rule.name,
                            detail = rule.description,
                            ruleType = RuleType.HTTP_API_SMELL,
                            fullName = "${it.packageName}:${it.className}:${it.functionName}",
                            source = it.toString()
                        )
                    })
                }
            }
        }

        return results
    }

}