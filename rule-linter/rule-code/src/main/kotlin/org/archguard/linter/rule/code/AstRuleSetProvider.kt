package org.archguard.linter.rule.code

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

/*
 * Low level provider
 */
class AstRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.CHANGE_SMELL,
            "normal",
        )
    }
}