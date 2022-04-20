package org.archguard.rule.impl.container

import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor
import org.archguard.rule.impl.container.model.ContainerResource

class ContainerVisitor(private val resources: Array<ContainerResource>) : RuleVisitor {
    fun visitor(ruleSets: Iterable<RuleSet>, rootNode: ContainerResource): Array<Issue> {
        var results: Array<Issue> = arrayOf()
        val context = RuleContext()

        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach {
                // todo: cast by plugins
                val rule = it as ContainerRule
                rule.visitResource(rootNode, context, fun(rule: Rule, position: IssuePosition) {
                    results += Issue(
                        position,
                        ruleId = rule.key,
                        name = rule.name,
                        detail = rule.description
                    )
                })
            }
        }

        return results
    }
}
