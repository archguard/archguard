package org.archguard.rule.core

import kotlinx.serialization.Serializable

@Serializable
class IssuePosition(
    var startLine: Int = 0,
    var startColumn: Int = 0,
    var endLine: Int = 0,
    var endColumn: Int = 0,

    // for additions info
    var additions: Map<String, String> = mapOf(),
) {
    override fun toString(): String {
        return "IssuePosition(startLine=$startLine, startColumn=$startColumn, endLine=$endLine, endColumn=$endColumn, additions=$additions)"
    }
}
