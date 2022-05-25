package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCoupling

data class PackageCouplingPO(
    val id: String,
    val moduleName: String,
    val packageName: String,
    val fanIn: Int,
    val fanOut: Int
) {
    fun toPackageCoupling(): PackageCoupling {
        var coupling = 1 - 1.0 / (fanIn + fanOut)
        if (coupling.isInfinite() || coupling.isNaN()) {
            coupling = 0.0
        }
        var instability = fanOut.toDouble() / (fanIn + fanOut)
        if (instability.isInfinite() || instability.isNaN()) {
            instability = 0.0
        }
        return PackageCoupling(id, moduleName, packageName, fanIn, fanOut, coupling, instability)
    }
}
