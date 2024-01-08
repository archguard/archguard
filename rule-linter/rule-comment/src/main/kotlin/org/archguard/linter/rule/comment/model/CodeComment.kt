package org.archguard.linter.rule.comment.model

import chapi.domain.core.CodePosition
import kotlinx.serialization.Serializable


/**
 * a doc must have at least 3 lines, start with /** and end with */, and have at least one line of content
 */
const val DOC_THRESHOLD = 3

@Serializable
data class CodeComment(
    val content: String,
    val position: CodePosition,
) {
    companion object {
        /**
         * Re-indent the comment to remove the leading spaces.
         *
         * @param content the comment content to be re-indented
         * @return the re-indented comment content
         */
        fun reIndentComment(content: String): String {
            val lines = content.split("\n")
            val indent = lines[1].takeWhile { it == ' ' }
            val linesWithoutIndent = lines.map { it.removePrefix(indent) }

            // except the first line, every line should have one leading space
            val linesWithLeadingSpace = linesWithoutIndent.mapIndexed { index, line ->
                if (index == 0) {
                    line
                } else {
                    " $line"
                }
            }

            return linesWithLeadingSpace.joinToString("\n")
        }

        /**
         * Extracts Kotlin comments from the given code and returns a list of [CodeComment] objects.
         *
         * @param code The Kotlin code from which to extract comments.
         * @return A list of [CodeComment] objects representing the extracted comments.
         */
        fun parseComment(code: String): List<CodeComment> {
            val pattern = Regex("""/\*\*[^*]*\*+([^/*][^*]*\*+)*/""")

            val matches = pattern.findAll(code)

            return matches.map { match ->
                val commentContent = match.value.trimIndent()
                val startLine = code.substring(0, match.range.first).count { it == '\n' } + 1
                val stopLine = code.substring(0, match.range.last).count { it == '\n' } + 1
                val startLinePosition = match.range.first - code.lastIndexOf('\n', match.range.first) - 1
                val stopLinePosition = match.range.last - code.lastIndexOf('\n', match.range.last) - 1

                val position = CodePosition(startLine, startLinePosition, stopLine, stopLinePosition)
                val content = reIndentComment(commentContent)

                CodeComment(content, position)
            }.toList()
        }

        /**
         * Returns a map of line numbers to code comments.
         *
         * This method takes a list of `CodeComment` objects and filters out any comments that are blank or have a length less than the `DOC_THRESHOLD` constant. It then associates each comment with its corresponding stop line number in the source code, resulting in a map where the keys are line numbers and the values are the associated comments.
         *
         * @param posComments the list of `CodeComment` objects to process
         * @return a map of line numbers to code comments
         */
        fun lineCommentMap(posComments: List<CodeComment>): Map<Int, CodeComment> {
            val startLineCommentMap: Map<Int, CodeComment> =
                posComments.filter { it.content.isNotBlank() && it.content.length >= DOC_THRESHOLD }.associateBy {
                    it.position.StopLine
                }

            return startLineCommentMap
        }

        fun fromCode(content: String): MutableMap<Int, CodeComment> {
            val comments = parseComment(content)
            val startLineCommentMap = lineCommentMap(comments)
            return startLineCommentMap.toMutableMap()
        }
    }
}