package com.thoughtworks.archguard.report_bak.domain.model

data class MethodLine(val systemId: Long,
                      val moduleName: String? = null,
                      val packageName: String,
                      val typeName: String,
                      val methodName: String,
                      val lines: Int)