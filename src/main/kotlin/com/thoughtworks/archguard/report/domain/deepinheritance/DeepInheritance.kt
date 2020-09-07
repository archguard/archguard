package com.thoughtworks.archguard.report.domain.deepinheritance

data class DeepInheritance(val id: String,
                           val systemId: Long,
                           val moduleName: String? = null,
                           val classFullName: String,
                           val dit: Int)