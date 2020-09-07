package com.thoughtworks.archguard.report.domain.dataclumps

data class ClassDataClump(val id: String,
                          val systemId: Long,
                          val moduleName: String? = null,
                          val classFullName: String,
                          val lcom4: Int)