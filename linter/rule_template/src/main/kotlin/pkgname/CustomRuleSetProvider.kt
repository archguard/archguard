package pkgname

import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

class CustomRuleSetProvider : RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.SQL_SMELL,
            "custom"
        )
    }
}
