package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.util.NameUtil.getClassName
import com.thoughtworks.archguard.report.util.NameUtil.getPackageName

data class ClassCouplingPO(val id: String, val moduleName: String? = null,
                           val classFullName: String, val fanIn: Int, val fanOut: Int) {
    fun toClassCoupling(): ClassCoupling {
        var coupling = 1 - 1.0 / (fanIn + fanOut)
        if (coupling.isInfinite() || coupling.isNaN()) {
            coupling = 0.0
        }
        var instability = fanOut.toDouble() / (fanIn + fanOut)
        if (instability.isInfinite() || instability.isNaN()) {
            instability = 0.0
        }
        return ClassCoupling(id, moduleName, getPackageName(classFullName), getClassName(classFullName), fanIn, fanOut, coupling, instability)
    }
}

