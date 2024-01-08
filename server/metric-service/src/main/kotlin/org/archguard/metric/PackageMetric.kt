package org.archguard.metric

data class PackageMetric(
    val systemId: Long,
    val moduleName: String,
    val packageName: String,
    val fanIn: Int?,
    val fanOut: Int?
)
