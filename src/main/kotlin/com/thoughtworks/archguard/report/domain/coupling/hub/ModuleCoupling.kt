package com.thoughtworks.archguard.report.domain.coupling.hub

data class ModuleCoupling(val id: String, val moduleName: String,
                          val fanIn: Int, val fanOut: Int, val coupling: Double, val instability: Double)