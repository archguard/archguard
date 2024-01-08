package org.archguard.linter.rule.comment

import org.archguard.linter.rule.comment.model.CodeComment
import chapi.domain.core.CodeContainer
import org.archguard.rule.core.*

class CommentRuleVisitor(val comments: List<CodeComment>, val container: CodeContainer) : RuleVisitor(comments) {
    private val lineCommentMap = CodeComment.lineCommentMap(comments)

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val context = RuleContext()

        container.DataStructures.forEach { struct ->
            ruleSets.forEach { ruleSet ->
                ruleSet.rules.forEach { rule ->
                    val apiRule = rule as CommentRule
                    val classComment = lineCommentMap[struct.Position.StartLine - 1]
                    if (classComment != null) {
                        apiRule.visitRoot(struct, classComment, context, fun(rule: Rule, position: IssuePosition) {
                            results += Issue(
                                position,
                                ruleId = rule.key,
                                name = rule.name,
                                detail = rule.description,
                                ruleType = RuleType.CODE_SMELL,
                                fullName = "${struct.Module}:${struct.Package}:${struct.NodeName}",
                                source = struct.FilePath
                            )
                        })
                    }

                    struct.Functions.forEach { method ->
                        val methodComment = lineCommentMap[method.Position.StartLine - 1] ?: return@forEach
                        apiRule.visitFunction(method, methodComment, context, fun(rule: Rule, position: IssuePosition) {
                            results += Issue(
                                position,
                                ruleId = rule.key,
                                name = rule.name,
                                detail = rule.description,
                                ruleType = RuleType.CODE_SMELL,
                                fullName = "${struct.Module}:${struct.Package}:${struct.NodeName}",
                                source = struct.FilePath
                            )
                        })
                    }
                }
            }
        }

        return results
    }
}
