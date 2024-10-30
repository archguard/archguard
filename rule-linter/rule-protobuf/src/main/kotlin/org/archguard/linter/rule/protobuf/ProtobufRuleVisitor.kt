package org.archguard.linter.rule.protobuf

import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.rule.core.*

class ProtobufRuleVisitor (services: List<ContainerService>) : RuleVisitor(services) {
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
                val protobufRule = rule as ProtobufRule
                resources.map {
                    protobufRule.visitResource(it, context, fun(rule: Rule, position: IssuePosition) {
                        results += Issue(
                            position,
                            ruleId = rule.key,
                            name = rule.name,
                            detail = rule.description,
                            ruleType = RuleType.PROTOBUF_SMELL,
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