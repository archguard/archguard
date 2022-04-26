package org.archguard.linter.rule.testcode

import org.archguard.linter.rule.testcode.rules.DuplicateAssertRule
import org.archguard.linter.rule.testcode.rules.EmptyTestRule
import org.archguard.linter.rule.testcode.rules.NoIgnoreTestRule
import org.archguard.linter.rule.testcode.rules.RedundantAssertionRule
import org.archguard.linter.rule.testcode.rules.RedundantPrintRule
import org.archguard.linter.rule.testcode.rules.SleepyTestRule
import org.archguard.linter.rule.testcode.rules.UnknownTestRule
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType
import org.archguard.rule.common.CasingRule

/*
 * Low level provider
 */
class TestSmellProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.CHANGE_SMELL,
            "normal",
            EmptyTestRule(),
            NoIgnoreTestRule(),
            SleepyTestRule(),
            RedundantPrintRule(),
            RedundantAssertionRule(),
            UnknownTestRule(),
            DuplicateAssertRule(),
        )
    }
}
