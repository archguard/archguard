package org.archguard.rule.core

import kotlinx.serialization.Serializable

@Serializable
class RuleResult (
    val position: SmellPosition,
    val ruleId: String,
    val name: String,
    val detail: String
)

@Serializable
class SmellPosition (
    var startLine: Int = 0,
    var startColumn: Int = 0,
    var endLine: Int = 0,
    var endColumn: Int = 0
) {
    override fun toString(): String {
        return "SmellPosition(startLine=$startLine, startColumn=$startColumn, endLine=$endLine, endColumn=$endColumn)"
    }
}