package org.archguard.rule.impl.container

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType
import org.archguard.rule.impl.container.rules.EndWithoutCrudRule
import org.archguard.rule.impl.container.rules.NotUppercaseRule
import org.archguard.rule.impl.container.rules.StartWithoutCrudRule
import org.archguard.rule.impl.container.rules.UrlSplitNamingRule

class ContainerRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.CHANGE_SMELL,
            "normal",
            UrlSplitNamingRule(),
            EndWithoutCrudRule(),
            NotUppercaseRule(),
            StartWithoutCrudRule(),
        )
    }
}
