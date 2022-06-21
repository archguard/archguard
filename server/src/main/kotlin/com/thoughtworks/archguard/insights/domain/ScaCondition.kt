package com.thoughtworks.archguard.insights.domain

data class ScaCondition(
    val type: String,
    val comparison: String,
    val version: String
)

