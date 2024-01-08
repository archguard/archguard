package org.archguard.linter.rule.comment

import org.archguard.linter.rule.comment.rules.MissingParameterDescRule
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

class CommentRuleSetProvider: RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.COMMENT_SMELL,
            "normal",
            MissingParameterDescRule()
        )
    }
}
