package org.archguard.linter.rule.comment.model

import chapi.domain.core.CodePosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CodeCommentTest {

    @Test
    fun should_reIndentComment() {
        // Given
        val commentContent = """
            |    This is a comment.
            |    It has multiple lines.
            |        And some indented lines.
        """.trimMargin()

        // When
        val reIndentedComment = CodeComment.reIndentComment(commentContent)

        // Then
        val expectedComment = """
            |This is a comment.
            | It has multiple lines.
            |     And some indented lines.
        """.trimMargin()

        assertEquals(expectedComment, reIndentedComment)
    }

    @Test
    fun should_extractKotlinComment() {
        // Given
        val code = """
            |fun main() {
            |    /**
            |     * This is a comment.
            |     * It has multiple lines.
            |     */
            |    println("Hello, World!")
            |}
        """.trimMargin()

        // When
        val extractedComments = CodeComment.parseComment(code)

        // Then
        val expectedComment = CodeComment(
            """
            |/**
            | * This is a comment.
            | * It has multiple lines.
            | */
            """.trimMargin(),
            CodePosition(2, 4, 5, 6)
        )

        assertEquals(listOf(expectedComment), extractedComments)
    }
}
