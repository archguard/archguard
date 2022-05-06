package org.archguard.linter.rule.code

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor

/*
 * Low level provider
 */
class AstRuleVisitor(rootNode: List<CodeDataStruct>): RuleVisitor(rootNode) {
    fun visitor(ruleSets: List<RuleSet>) {

    }
}