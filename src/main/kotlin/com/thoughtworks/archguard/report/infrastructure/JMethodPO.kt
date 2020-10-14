package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.MethodSizing
import com.thoughtworks.archguard.report.domain.testing.MethodInfo
import com.thoughtworks.archguard.report.util.NameUtil

data class JMethodPO(val id: String,
                val systemId: Long,
                val module: String? = null,
                val clzname: String,
                val name: String,
                val loc: Int = 0) {

    fun toMethodInfo(): MethodInfo {
        return MethodInfo(id, systemId, module, NameUtil.getPackageName(clzname),
                NameUtil.getClassName(clzname), name)
    }

    fun toMethodSizing(): MethodSizing {
        return MethodSizing(id, systemId, module, NameUtil.getPackageName(clzname),
                NameUtil.getClassName(clzname), name, loc)
    }
}