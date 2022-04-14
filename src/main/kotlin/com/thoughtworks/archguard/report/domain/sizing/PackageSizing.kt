package com.thoughtworks.archguard.report.domain.sizing

import java.util.UUID

data class PackageSizing(
    val id: String = UUID.randomUUID().toString(),
    val systemId: Long,
    val moduleName: String? = null,
    val packageName: String,
    val classCount: Int = 0,
    val lines: Int = 0
)
