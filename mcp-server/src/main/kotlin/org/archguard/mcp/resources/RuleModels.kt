package org.archguard.mcp.resources

import kotlinx.serialization.Serializable

@Serializable
data class RulesResponse(
    val categories: List<RuleCategory>
)

@Serializable
data class RuleCategory(
    val name: String,
    val description: String,
    val rules: List<RuleInfo>
)

@Serializable
data class RuleInfo(
    val id: String,
    val name: String,
    val description: String,
    val severity: String,
    val type: String
)
