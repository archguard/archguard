package com.thoughtworks.archguard.report.domain.testing

data class MethodInfo(val id: String,
                      val systemId: Long,
                      val moduleName: String? = null,
                      val packageName: String,
                      val typeName: String,
                      val methodName: String)