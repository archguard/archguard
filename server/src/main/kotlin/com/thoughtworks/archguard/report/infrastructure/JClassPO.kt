package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithLine
import com.thoughtworks.archguard.report.domain.sizing.ClassSizingWithMethodCount

data class JClassPO(
    val id: String="",
    val systemId: Long=0,
    val module: String? = null,
    val loc: Int? = null,
    val className: String,
    val packageName: String,
    val count: Int? = null
) {
    fun toClassSizingWithLine(): ClassSizingWithLine {
        return ClassSizingWithLine(id, systemId, module, packageName, className, loc ?: 0)
    }

    fun toClassSizingWithMethodCount(): ClassSizingWithMethodCount {
        return ClassSizingWithMethodCount(id, systemId, module, packageName, className, count ?: 0)
    }
}
