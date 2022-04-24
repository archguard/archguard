package org.archguard.ident.mysql.linter

import org.archguard.ident.mysql.linter.rules.FuzzyPercentNotAtStartRule
import org.archguard.ident.mysql.linter.rules.UnknownNumberColumnRule
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

class SqlRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.SQL_SMELL,
            "normal",
            UnknownNumberColumnRule(),
            FuzzyPercentNotAtStartRule(),
        )
    }
}
