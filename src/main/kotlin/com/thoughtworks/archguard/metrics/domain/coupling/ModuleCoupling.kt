package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import org.nield.kotlinstatistics.median

class ModuleCoupling private constructor(
        val logicModule: LogicModule,
        val outerInstabilityAvg: Double,
        val outerInstabilityMed: Double,
        val outerCouplingAvg: Double,
        val outerCouplingMed: Double,
        val innerInstabilityAvg: Double,
        val innerInstabilityMed: Double,
        val innerCouplingAvg: Double,
        val innerCouplingMed: Double) {
    companion object {
        fun of(logicModule: LogicModule, classCouplings: List<ClassCoupling>) = ModuleCoupling(
                logicModule = logicModule,
                outerInstabilityAvg = classCouplings.map { it.outerInstability }.average(),
                outerInstabilityMed = classCouplings.map { it.outerInstability }.median(),
                outerCouplingAvg = classCouplings.map { it.outerCoupling }.average(),
                outerCouplingMed = classCouplings.map { it.outerCoupling }.median(),
                innerInstabilityAvg = classCouplings.map { it.innerInstability }.average(),
                innerInstabilityMed = classCouplings.map { it.innerInstability }.median(),
                innerCouplingAvg = classCouplings.map { it.innerCoupling }.average(),
                innerCouplingMed = classCouplings.map { it.innerCoupling }.median()
        )
    }
}
