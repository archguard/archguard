package com.thoughtworks.archguard.insights.application

import kotlinx.serialization.Serializable

data class InsightModel(
    val size: Int,
    var content: List<ModelContent> = listOf(),
)

@Serializable
sealed class ModelContent

data class InsightModelDto(
    val dep_name: String,
    val dep_group: String,
    val dep_artifact: String,
    val dep_version: String,
): ModelContent() {
}