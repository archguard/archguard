package com.thoughtworks.archguard.insights

data class CustomInsight (
    val id: Long? = null,
    val systemId: Long,
    val name: String? = "Default",
    val expression: String,
    val schedule: String? = "1d",
)