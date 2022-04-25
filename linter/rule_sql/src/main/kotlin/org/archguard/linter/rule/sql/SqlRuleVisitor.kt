package org.archguard.linter.rule.sql

import net.sf.jsqlparser.statement.Statement
import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor

class SqlRuleVisitor(val statements: List<Statement>) : RuleVisitor {
    fun visitor(ruleSets: Iterable<RuleSet>): Array<Issue> {
        var results: Array<Issue> = arrayOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach { rule ->
                // todo: cast by plugins
                val rule = rule as SqlRule
                rule.visit(statements, context, fun(rule: Rule, position: IssuePosition) {
                    results += Issue(
                        position,
                        ruleId = rule.key,
                        name = rule.name,
                        detail = rule.description,
                        ruleType = RuleType.HTTP_API_SMELL
                    )
                })
            }
        }

        return results
    }

}