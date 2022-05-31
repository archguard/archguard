package org.archguard.linter.rule.webapi

import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor
import org.archguard.scanner.core.sourcecode.ContainerResource

class WebApiRuleVisitor(private val resources: List<ContainerResource>) : RuleVisitor(resources) {
    override fun requires(): List<String> {
        return listOf(ContainerResource.Companion::class.java.name)
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach { rule ->
                // todo: cast by plugins
                val apiRule = rule as WebApiRule
                resources.map {
                    apiRule.visitResources(this.resources, context, fun(rule: Rule, position: IssuePosition) {
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
        }

        return results
    }
}
