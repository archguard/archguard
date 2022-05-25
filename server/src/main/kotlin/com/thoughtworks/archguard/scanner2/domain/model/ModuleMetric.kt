package com.thoughtworks.archguard.scanner2.domain.model

data class ModuleMetric(
    val systemId: Long,
    val moduleName: String,
    val fanIn: Int?,
    val fanOut: Int?
)
