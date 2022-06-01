package org.archguard.rule.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * **Issue** is the analysis result of items.
 */
@Serializable
class Issue(
    val position: IssuePosition,
    val ruleId: String,
    val name: String,
    val detail: String,
    val ruleType: RuleType,
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
