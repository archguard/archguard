package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling

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

fun getPackageName(name: String): String {
    if (!name.contains('.')) return ""

    val endIndex = name.indexOfLast { it == '.' }
    return name.substring(0, endIndex)
}

fun getClassName(name: String): String {
    if (!name.contains('.')) return name
    val endIndex = name.indexOfLast { it == '.' }
    return name.substring(endIndex + 1)
}