package com.thoughtworks.archguard.report.domain.sizing

import java.util.*

data class ClassSizingWithMethodCount(val id: String = UUID.randomUUID().toString(),
                                      val systemId: Long,
                                      val moduleName: String? = null,
                                      val packageName: String,
                                      val typeName: String,
                                      val methodCount: Int)
