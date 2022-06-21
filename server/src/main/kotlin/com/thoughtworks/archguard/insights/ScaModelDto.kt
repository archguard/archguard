package com.thoughtworks.archguard.insights

data class ScaModelDto(
    val dep_artifact: String,
    val dep_group: String,
    val dep_version: String,
) {
}