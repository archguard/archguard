package com.thoughtworks.archguard.report.domain.coupling.deepinheritance

data class DeepInheritance(
    val id: String,
    val systemId: Long,
    val moduleName: String? = null,
    val packageName: String,
    val typeName: String,
    val dit: Int
)
