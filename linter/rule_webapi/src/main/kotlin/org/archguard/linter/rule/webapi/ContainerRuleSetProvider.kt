package org.archguard.linter.rule.webapi

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType
import org.archguard.linter.rule.webapi.rules.MultipleParametersRule
import org.archguard.linter.rule.webapi.rules.MinFeatureApiRule
import org.archguard.linter.rule.webapi.rules.NoCrudEndRule
import org.archguard.linter.rule.webapi.rules.NoHttpMethodInUrlRule
import org.archguard.linter.rule.webapi.rules.NotUppercaseRule
import org.archguard.linter.rule.webapi.rules.StartWithoutCrudRule
import org.archguard.linter.rule.webapi.rules.SpliceNamingRule

class ContainerRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.CHANGE_SMELL,
            "normal",
            SpliceNamingRule(),
            NoCrudEndRule(),
            NotUppercaseRule(),
            StartWithoutCrudRule(),
            NoHttpMethodInUrlRule(),
            MinFeatureApiRule(),
            MultipleParametersRule(),
        )
    }
}
