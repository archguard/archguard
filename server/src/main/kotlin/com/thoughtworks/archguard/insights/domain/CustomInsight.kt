package com.thoughtworks.archguard.insights.domain

data class CustomInsight (
    val id: Long? = null,
    val systemId: Long = 0L,
    val name: String? = "Default",
    val expression: String,
    val type: String = "sca",
    val schedule: String? = "",
)