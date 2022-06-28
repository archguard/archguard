package com.thoughtworks.archguard.insights.application

import kotlinx.serialization.Serializable

data class InsightModel(
    val size: Int,
    var content: List<ModelContent> = listOf(),
)

@Serializable
sealed class ModelContent

data class IssueModelDto(
    val name: String,
    val rule_id: String,
    val rule_type: String,
    val severity: String
): ModelContent() {
}

data class InsightModelDto(
    val dep_name: String,
    val dep_group: String,
    val dep_artifact: String,
    val dep_version: String,
): ModelContent() {
}