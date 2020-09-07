package com.thoughtworks.archguard.report.domain.sizing

data class MethodSizing(val id: String,
                        val systemId: Long,
                        val moduleName: String? = null,
                        val packageName: String,
                        val typeName: String,
                        val methodName: String,
                        val lines: Int)