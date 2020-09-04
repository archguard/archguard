package com.thoughtworks.archguard.report.domain.model

data class ClassSizingWithMethodCount(val systemId: Long,
                                      val moduleName: String? = null,
                                      val packageName: String,
                                      val typeName: String,
                                      val methodCount: Int)
