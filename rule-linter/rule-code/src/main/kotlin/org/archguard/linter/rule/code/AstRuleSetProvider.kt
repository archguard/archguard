package org.archguard.linter.rule.code

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

/**
 * The `AstRuleSetProvider` class is a low-level provider that implements the `RuleSetProvider` interface.
 * It provides a rule set for the Kotlin language.
 *
 * @constructor Creates a new instance of `AstRuleSetProvider`.
 */
class AstRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.CHANGE_SMELL,
            "normal",
        )
    }
}