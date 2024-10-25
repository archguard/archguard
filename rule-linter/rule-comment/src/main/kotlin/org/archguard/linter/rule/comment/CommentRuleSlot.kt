package org.archguard.linter.rule.comment

import chapi.domain.core.CodeContainer
import org.archguard.linter.rule.comment.model.CodeComment
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.model.ContainerService

class CommentRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(ContainerService::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(CommentRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): List<Any> {
        val codeContainer = (items as List<CodeContainer>).first()
        val comments = CodeComment.parseComment(codeContainer.Content)
        if (comments.isEmpty()) {
            return listOf()
        }

        return CommentRuleVisitor(comments, codeContainer).visitor(this.material as Iterable<RuleSet>)
    }
}