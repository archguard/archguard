package com.thoughtworks.archguard.metrics.domain.coupling

import org.nield.kotlinstatistics.median

@Deprecated("")
data class PackageMetricsLegacy(
        var id: Long?,
        var moduleId: Long,
        var packageName: String,
        var classMetrics: List<ClassMetricsLegacy>,
        var outerInstabilityAvg: Double,
        var outerInstabilityMed: Double,
        var outerCouplingAvg: Double,
        var outerCouplingMed: Double,
        var innerInstabilityAvg: Double,
        var innerInstabilityMed: Double,
        var innerCouplingAvg: Double,
        var innerCouplingMed: Double
) {
    constructor() : this(null, 0, "", emptyList(), 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0)

    companion object {
        fun of(packageName: String, classMetrics: List<ClassMetricsLegacy>) = PackageMetricsLegacy(
                id = null,
                moduleId = 0,
                packageName = packageName,
                classMetrics = classMetrics,
                outerInstabilityAvg = classMetrics.map { it.outerInstability }.average(),
                outerInstabilityMed = classMetrics.map { it.outerInstability }.median(),
                outerCouplingAvg = classMetrics.map { it.outerCoupling }.average(),
                outerCouplingMed = classMetrics.map { it.outerCoupling }.median(),
                innerInstabilityAvg = classMetrics.map { it.innerInstability }.average(),
                innerInstabilityMed = classMetrics.map { it.innerInstability }.median(),
                innerCouplingAvg = classMetrics.map { it.innerCoupling }.average(),
                innerCouplingMed = classMetrics.map { it.innerCoupling }.median()
        )
    }
}