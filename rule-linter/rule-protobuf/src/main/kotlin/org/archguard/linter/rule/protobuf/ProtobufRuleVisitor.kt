package org.archguard.linter.rule.protobuf

import org.archguard.context.ContainerService
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor

class ProtobufRuleVisitor (services: List<ContainerService>) : RuleVisitor(services) {
    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        TODO()
    }
}