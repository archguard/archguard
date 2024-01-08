package com.thoughtworks.archguard.scanner2.infrastructure.po

data class ClassMetricPO(
    val systemId: Long,
    val classId: String,
    val dit: Int?,
    val noc: Int?,
    val lcom4: Int?,
    val fanIn: Int?,
    val fanOut: Int?
)
