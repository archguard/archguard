package org.archguard.rule.core

import kotlinx.serialization.Serializable

/**
 * Represents the position of an issue in a code file.
 *
 * This class is used to store the start and end positions of an issue in terms of lines and columns.
 * It also provides an optional map to store additional information related to the issue.
 *
 * @property startLine The line number where the issue starts. Default value is 0.
 * @property startColumn The column number where the issue starts. Default value is 0.
 * @property endLine The line number where the issue ends. Default value is 0.
 * @property endColumn The column number where the issue ends. Default value is 0.
 * @property additions A map of additional information related to the issue. Default value is an empty map.
 */
@Serializable
data class IssuePosition(
    var startLine: Int = 0,
    var startColumn: Int = 0,
    var endLine: Int = 0,
    var endColumn: Int = 0,

    // for additions info
    var additions: Map<String, String> = mapOf(),
) {

}
