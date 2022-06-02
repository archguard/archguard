package org.archguard.linter.rule.sql

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation

class DatamapRuleVisitor(relations: List<CodeDatabaseRelation>): RuleVisitor(relations) {
    private var statements: List<Statement>

    init {
        this.statements = relations.flatMap { relation ->
            relation.sqls.mapNotNull {
                try {
                    CCJSqlParserUtil.parseStatements(it)
                } catch (e: Exception) {
                    null
                }
            }.flatMap {
                it.statements
            }
        }
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach { rule ->
                // todo: cast by plugins
                val sqlRule = rule as SqlRule
                sqlRule.visit(statements, context, fun(rule: Rule, position: IssuePosition) {
                    results += Issue(
                        position,
                        ruleId = rule.key,
                        name = rule.name,
                        detail = rule.description,
                        ruleType = RuleType.HTTP_API_SMELL,
                        source = statements.toString()
                    )
                })
            }
        }

        return results
    }

}