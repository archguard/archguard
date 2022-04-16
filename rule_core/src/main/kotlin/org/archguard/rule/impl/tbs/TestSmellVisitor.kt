package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor

class TestSmellVisitor: RuleVisitor {
    internal fun visitor(ruleSets: Iterable<RuleSet>, rootNode: CodeDataStruct) {
        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach {
                it.visit(rootNode) {
                    return@visit
                }
            }
        }
    }
}
