package org.archguard.metric

data class ModuleMetric(
    val systemId: Long,
    val moduleName: String,
    val fanIn: Int?,
    val fanOut: Int?
)
