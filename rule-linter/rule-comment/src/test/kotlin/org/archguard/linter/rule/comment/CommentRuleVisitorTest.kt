package org.archguard.linter.rule.comment;

import chapi.ast.javaast.JavaAnalyser
import org.archguard.linter.rule.comment.model.CodeComment
import chapi.domain.core.CodeContainer
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodePosition
import org.archguard.rule.core.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommentRuleVisitorTest {
    @Test
    fun shouldReturnEmptyListWhenNoComments() {
        // Given
        val comments = emptyList<CodeComment>()
        val container = CodeContainer()
        val visitor = CommentRuleVisitor(comments, container)
        val ruleSets = emptyList<RuleSet>()

        // When
        val result = visitor.visitor(ruleSets)

        // Then
        assertEquals(emptyList<Issue>(), result)
    }

    @Test
    fun shouldSupportJavaCommentParse() {
        // given
        val code = """
            public class Test {
                /**
                 * Sum a and b, and return the result.
                 * @param x the first number
                 * @return the result of x + y
                 */
                public int calculateSum(int x, int y) {
                    return x + y;
                }
            }
            """.trimIndent()
        val container = JavaAnalyser().analysis(code, "Test.java")
        container.Content = code

        val comments = CodeComment.parseComment(container.Content)
        val commentAnalyser = CommentRuleVisitor(comments, container)

        // when
        val result = commentAnalyser.visitor(listOf(CommentRuleSetProvider().get()))

        // then
        assertEquals(1, result.size)
    }
}
