package com.thoughtworks.archguard.insights.domain

data class ScaModelDto(
    // name = `artifact.group`
    val dep_name: String,
    val dep_group: String,
    val dep_artifact: String,
    val dep_version: String,
) {
}