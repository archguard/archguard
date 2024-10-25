package org.archguard.linter.rule.webapi

import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor
import org.archguard.context.ContainerSupply
import org.archguard.context.ContainerService

class WebApiRuleVisitor(services: List<ContainerService>) : RuleVisitor(services) {
    private var resources: List<ContainerSupply>

    init {
        resources = services.flatMap {
            it.resources
        }
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach { rule ->
                val apiRule = rule as WebApiRule
                resources.map {
                    apiRule.visitResource(it, context, fun(rule: Rule, position: IssuePosition) {
                        results += Issue(
                            position,
                            ruleId = rule.key,
                            name = rule.name,
                            detail = rule.description,
                            ruleType = RuleType.HTTP_API_SMELL,
                            fullName = "${it.packageName}:${it.className}:${it.methodName}",
                            source = "${it.sourceHttpMethod} ${it.sourceUrl}"
                        )
                    })
                }
            }
        }

        return results
    }
}
