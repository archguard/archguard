package org.archguard.rule.core

import kotlinx.serialization.Serializable

@Serializable
class RuleResult (
    val line: Int,
    val col: Int,
    val ruleId: String,
    val detail: String
)

