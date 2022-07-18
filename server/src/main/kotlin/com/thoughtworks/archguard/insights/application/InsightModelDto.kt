package com.thoughtworks.archguard.insights.application

import kotlinx.serialization.Serializable

data class InsightModel(
    val size: Int,
    var content: List<ModelContent> = listOf(),
)

@Serializable
sealed class ModelContent

// TODO: we keep `rule_id` naming in the model for now, but we should remove it in the future.
data class IssueModelDto(
    val name: String,
    val rule_id: String,
    val rule_type: String,
    val severity: String,
) : ModelContent()


// TODO: we keep `dep_*` naming in the model for now, but we should remove it in the future.
data class InsightModelDto(
    val dep_name: String,
    val dep_group: String,
    val dep_artifact: String,
    val dep_version: String,
) : ModelContent()


// TODO: we keep `package_name` naming in the model for now, but we should remove it in the future.
data class StructureModelDto(
    val name: String,
    val package_name: String,
) : ModelContent()