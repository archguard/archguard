package org.archguard.linter.rule.layer

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

class LayerRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.LAYER,
            "normal",
        )
    }
}