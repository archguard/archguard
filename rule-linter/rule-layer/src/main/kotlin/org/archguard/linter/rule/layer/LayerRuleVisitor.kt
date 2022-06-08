package org.archguard.linter.rule.layer

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor

class LayerRuleVisitor(rootNode: List<CodeDataStruct>) : RuleVisitor(rootNode) {
    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        return listOf()
    }
}