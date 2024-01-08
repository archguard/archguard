package com.thoughtworks.archguard.scanner2.infrastructure.po

data class MethodMetricPO(
    val systemId: Long,
    val methodId: String,
    val fanIn: Int?,
    val fanOut: Int?
)
