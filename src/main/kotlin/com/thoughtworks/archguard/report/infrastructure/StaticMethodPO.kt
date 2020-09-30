package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.testing.StaticMethod
import com.thoughtworks.archguard.report.util.NameUtil

class StaticMethodPO(val id: String,
                     val systemId: Long,
                     val moduleName: String? = null,
                     val classFullName: String,
                     val methodName: String) {
    fun toStaticMethod(): StaticMethod {
        return StaticMethod(id, systemId, moduleName, NameUtil.getPackageName(classFullName),
                NameUtil.getClassName(classFullName), methodName)
    }
}