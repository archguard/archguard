package com.thoughtworks.archguard.report.domain.sizing

import java.util.*

data class ModuleSizing(val id: String,
                        val systemId: Long,
                        val moduleName: String? = null,
                        val packageCount: Int = 0,
                        val classCount: Int = 0,
                        val lines: Int = 0)
