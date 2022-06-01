package org.archguard.linter.rule.code

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor

/*
 * Low level provider
 */
class AstRuleVisitor(rootNode: List<CodeDataStruct>) : RuleVisitor(rootNode) {
    override fun ticket(): List<String> {
        return listOf(CodeDataStruct.Companion::class.java.name)
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        return listOf()
    }
}