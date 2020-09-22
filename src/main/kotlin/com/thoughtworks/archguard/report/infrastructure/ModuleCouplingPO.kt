package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.ModuleCoupling

data class ModuleCouplingPO(val id: String, val moduleName: String,
                            val fanIn: Int, val fanOut: Int) {
    fun toModuleCoupling(): ModuleCoupling {
        var coupling = 1 - 1.0 / (fanIn + fanOut)
        if (coupling.isInfinite() || coupling.isNaN()) {
            coupling = 0.0
        }
        var instability = fanOut.toDouble() / (fanIn + fanOut)
        if (instability.isInfinite() || instability.isNaN()) {
            instability = 0.0
        }
        return ModuleCoupling(id, moduleName, fanIn, fanOut, coupling, instability)
    }
}