package com.thoughtworks.archguard.dependence.domain.logic_module

data class ModuleCoupling(var moduleCouplings: List<ModuleCouplingReport>? = null) {
    val moduleInstabilityAverage = moduleCouplings?.map { it.moduleInstability }?.average() ?: 0.0
    val highInstabilityCount = moduleCouplings?.map { it.moduleInstability >= 0.8 }?.count() ?: 0
    val mediumInstabilityCount = moduleCouplings?.map {
        it.moduleInstability < 0.8 && it.moduleInstability >= 0.6
    }?.count() ?: 0
    val lowInstabilityCount = moduleCouplings?.map { it.moduleInstability < 0.6 }?.count() ?: 0
    val moduleCouplingAverage = moduleCouplings?.map { it.moduleCoupling }?.average() ?: 0.0
    val highCouplingCount = moduleCouplings?.map { it.moduleCoupling >= 0.8 }?.count() ?: 0
    val mediumCouplingCount = moduleCouplings?.map {
        it.moduleCoupling < 0.8 && it.moduleCoupling >= 0.6
    }?.count() ?: 0
    val lowCouplingCount = moduleCouplings?.map { it.moduleCoupling < 0.6 }?.count() ?: 0
}
