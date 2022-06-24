package com.thoughtworks.archguard.insights

data class CustomInsight (
    val id: Long? = null,
    val systemId: Long = 0L,
    val name: String? = "Default",
    val expression: String,
    val schedule: String? = "",
)