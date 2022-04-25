package org.archguard.linter.rule.sql

import org.archguard.linter.rule.sql.rules.FuzzyPercentNotAtStartRule
import org.archguard.linter.rule.sql.rules.UnknownNumberColumnRule
import org.archguard.linter.rule.sql.rules.create.AtLeastOnePrimaryKeyRule
import org.archguard.linter.rule.sql.rules.create.LimitColumnSizeRule
import org.archguard.linter.rule.sql.rules.create.SnakeCasingRule
import org.archguard.linter.rule.sql.rules.create.LimitTableNameLengthRule
import org.archguard.linter.rule.sql.rules.expression.LimitJoinsRule
import org.archguard.linter.rule.sql.rules.insert.InsertWithoutField
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
            LimitTableNameLengthRule(),
            SnakeCasingRule(),
            InsertWithoutField(),
            LimitJoinsRule(),
            AtLeastOnePrimaryKeyRule(),
            LimitColumnSizeRule()
        )
    }
}
