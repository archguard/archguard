package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.testing.MethodInfo
import com.thoughtworks.archguard.report.util.NameUtil

class StaticMethodPO(val id: String,
                     val systemId: Long,
                     val moduleName: String? = null,
                     val classFullName: String,
                     val methodName: String) {
    fun toMethodInfo(): MethodInfo {
        return MethodInfo(id, systemId, moduleName, NameUtil.getPackageName(classFullName),
                NameUtil.getClassName(classFullName), methodName)
    }
}