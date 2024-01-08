package org.archguard.linter.rule.comment

import org.archguard.linter.rule.comment.model.CodeComment
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext

open class CommentRule : Rule() {
    open fun visitRoot(node: CodeDataStruct, comment: CodeComment, context: RuleContext, callback: IssueEmit) {

    }

    open fun visitFunction(node: CodeFunction, comment: CodeComment, context: RuleContext, callback: IssueEmit) {

    }
}
