package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.code.module.domain.model.PackageVO
import org.nield.kotlinstatistics.median

class PackageCoupling private constructor(
    val packageVO: PackageVO,
    val outerInstabilityAvg: Double,
    val outerInstabilityMed: Double,
    val outerCouplingAvg: Double,
    val outerCouplingMed: Double,
    val innerInstabilityAvg: Double,
    val innerInstabilityMed: Double,
    val innerCouplingAvg: Double,
    val innerCouplingMed: Double
) {
    companion object {
        fun of(packageVO: PackageVO, classCouplings: List<ClassCoupling>) = PackageCoupling(
            packageVO = packageVO,
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
