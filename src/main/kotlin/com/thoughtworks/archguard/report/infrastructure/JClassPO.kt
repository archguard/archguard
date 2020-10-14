package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithLine
import com.thoughtworks.archguard.report.util.NameUtil

data class JClassPO(val id: String,
                    val systemId: Long,
                    val module: String? = null,
                    val loc: Int,
                    val name: String) {
    fun toClassSizingWithLine(): ClassSizingWithLine {
        return ClassSizingWithLine(id, systemId, module, NameUtil.getPackageName(name),
                NameUtil.getClassName(name), loc)
    }
}