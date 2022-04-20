package org.archguard.rule.impl.ast

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.impl.common.Language

class AstRule(var language: Language = Language.JAVA) : Rule() {
    override fun visit(rootNode: CodeDataStruct, context: RuleContext, callback: IssueEmit) {

    }
}