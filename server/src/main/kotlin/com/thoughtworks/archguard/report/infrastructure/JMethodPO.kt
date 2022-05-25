package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.MethodSizing
import com.thoughtworks.archguard.report.domain.testing.MethodInfo

data class JMethodPO(
    val id: String,
    val systemId: Long,
    val module: String? = null,
    val className: String,
    val packageName: String,
    val name: String,
    val loc: Int = 0
) {

    fun toMethodInfo(): MethodInfo {
        return MethodInfo(id, systemId, module, packageName, className, name)
    }

    fun toMethodSizing(): MethodSizing {
        return MethodSizing(id, systemId, module, packageName, className, name, loc)
    }
}
