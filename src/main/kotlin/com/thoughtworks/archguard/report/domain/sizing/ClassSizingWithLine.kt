package com.thoughtworks.archguard.report.domain.sizing

data class ClassSizingWithLine(val id: String,
                               val systemId: Long,
                               val moduleName: String? = null,
                               val packageName: String,
                               val typeName: String,
                               val lines: Int)