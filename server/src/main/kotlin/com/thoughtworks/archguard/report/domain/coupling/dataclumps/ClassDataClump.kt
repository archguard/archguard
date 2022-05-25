package com.thoughtworks.archguard.report.domain.coupling.dataclumps

data class ClassDataClump(
    val id: String,
    val systemId: Long,
    val moduleName: String? = null,
    val packageName: String,
    val typeName: String,
    val lcom4: Int
)
