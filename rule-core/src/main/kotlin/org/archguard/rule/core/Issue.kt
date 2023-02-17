package org.archguard.rule.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * **Issue** is the analysis result of [org.archguard.rule.core.Rule] items.
 */
@Serializable
data class Issue(
    val position: IssuePosition,
    val ruleId: String,
    val name: String,
    val detail: String,
    val ruleType: RuleType,
    val severity: Severity = Severity.HINT,
    // full name: Module:Package:Class:Method
    val fullName: String = "",
    // source of item, like URL, FilePath
    val source: String = "",
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
