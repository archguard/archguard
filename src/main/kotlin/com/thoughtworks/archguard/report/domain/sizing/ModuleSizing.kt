package com.thoughtworks.archguard.report.domain.sizing

data class ModuleSizing(val systemId: Long,
                        val moduleName: String? = null,
                        val packageCount: Int = 0,
                        val classCount: Int = 0,
                        val lines: Int = 0)
