package com.thoughtworks.archguard.report.domain.coupling.hub

data class PackageCoupling(val id: String, val moduleName: String? = null, val packageName: String,
                           val fanIn: Int, val fanOut: Int, val coupling: Double, val instability: Double)