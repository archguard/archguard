package com.thoughtworks.archguard.metrics.domain.coupling

import org.nield.kotlinstatistics.median

@Deprecated("")
data class ModuleMetricsLegacy(
        var id: Long?,
        var moduleName: String,
        var packageMetrics: List<PackageMetricsLegacy>,
        var outerInstabilityAvg: Double,
        var outerInstabilityMed: Double,
        var outerCouplingAvg: Double,
        var outerCouplingMed: Double,
        var innerInstabilityAvg: Double,
        var innerInstabilityMed: Double,
        var innerCouplingAvg: Double,
        var innerCouplingMed: Double
) {
    constructor() : this(null, "", emptyList(), 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0)

    companion object {
        fun of(moduleName: String, packageMetrics: List<PackageMetricsLegacy>) = ModuleMetricsLegacy(
                id = null,
                moduleName = moduleName,
                packageMetrics = packageMetrics,
                outerInstabilityAvg = packageMetrics.flatMap { it.classMetrics }.map { it.outerInstability }.average(),
                outerInstabilityMed = packageMetrics.flatMap { it.classMetrics }.map { it.outerInstability }.median(),
                outerCouplingAvg = packageMetrics.flatMap { it.classMetrics }.map { it.outerCoupling }.average(),
                outerCouplingMed = packageMetrics.flatMap { it.classMetrics }.map { it.outerCoupling }.median(),
                innerInstabilityAvg = packageMetrics.flatMap { it.classMetrics }.map { it.innerInstability }.average(),
                innerInstabilityMed = packageMetrics.flatMap { it.classMetrics }.map { it.innerInstability }.median(),
                innerCouplingAvg = packageMetrics.flatMap { it.classMetrics }.map { it.innerCoupling }.average(),
                innerCouplingMed = packageMetrics.flatMap { it.classMetrics }.map { it.innerCoupling }.median()
        )
    }
}