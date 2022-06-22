package com.thoughtworks.archguard.insights.domain

data class ScaModelDto(
    val dep_name: String,
    val dep_artifact: String,
    val dep_group: String,
    val dep_version: String,
) {
}