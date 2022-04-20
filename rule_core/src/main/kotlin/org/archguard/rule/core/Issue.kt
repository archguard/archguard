package org.archguard.rule.core

import kotlinx.serialization.Serializable

@Serializable
class Issue (
    val position: IssuePosition,
    val ruleId: String,
    val name: String,
    val detail: String
)
