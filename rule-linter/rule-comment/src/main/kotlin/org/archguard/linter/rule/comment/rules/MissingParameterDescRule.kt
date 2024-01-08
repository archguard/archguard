package org.archguard.linter.rule.comment.rules

import chapi.domain.core.CodeFunction
import org.archguard.linter.rule.comment.CommentRule
import org.archguard.linter.rule.comment.model.CodeComment
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Severity

/**
 * Parse the documentation of the code and check whether the documentation is complete.
 *
 * For example, the following code is missing the parameter description:
 * ```java
 * /**
 *  * Sum a and b, and return the result.
 *  * @param a the first number
 *  * @return the result of a + b
 * */
 * public int calculateSum(int a, int b) {
 *    return a + b;
 * }
 * ```
 *
 * We can use this rule to check whether the documentation is complete.
 */
class MissingParameterDescRule : CommentRule() {
    init {
        this.id = "missing-parameter-desc"
        this.name = "MissingParameterDesc"
        this.key = this.javaClass.name
        this.description = "The documentation is un-complete, parameter description is missing"
        this.severity = Severity.WARN
    }

    private val pattern = Regex("""@param\s+(\w+)\s+([^@]+)""")

    override fun visitFunction(node: CodeFunction, comment: CodeComment, context: RuleContext, callback: IssueEmit) {
        val matches = pattern.findAll(comment.content)

        val nodeSize = node.Parameters.size

        if (matches.count() != nodeSize) {
            this.description = "The documentation is un-complete, parameter description is missing"
            callback(this, IssuePosition())
            return
        }

        val matchNames = matches.map { it.groupValues[1] }.toSet()
        val nodeNames = node.Parameters.map { it.TypeValue }.toSet()

        if (matchNames != nodeNames) {
            this.description = "The documentation is error, parameter name is not match"
            callback(this, IssuePosition())
        }
    }
}