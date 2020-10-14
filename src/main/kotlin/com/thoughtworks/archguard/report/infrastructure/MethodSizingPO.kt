package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.MethodSizing

data class MethodSizingPO(val id: String,
                          val systemId: Long,
                          val module: String? = null,
                          val clzname: String,
                          val name: String,
                          val loc: Int) {
    fun toMethodSizing(): MethodSizing {
        val lastIndexOf = clzname.lastIndexOf(".")
        val packageName = clzname.substring(0, lastIndexOf)
        val typeName = clzname.substring(lastIndexOf + 1)
        return MethodSizing(id, systemId, module, packageName, typeName, name, loc)
    }

}
