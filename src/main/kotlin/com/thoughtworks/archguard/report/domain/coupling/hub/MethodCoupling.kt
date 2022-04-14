package com.thoughtworks.archguard.report.domain.coupling.hub

data class MethodCoupling(
    val id: String,
    val moduleName: String? = null,
    val packageName: String,
    val typeName: String,
    val methodName: String,
    val args: String?,
    val fanIn: Int,
    val fanOut: Int,
    val coupling: Double,
    val instability: Double
)
