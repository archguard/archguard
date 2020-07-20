package com.thoughtworks.archguard.module.domain.metrics

import org.nield.kotlinstatistics.median

data class PackageMetrics(
        val packageName: String,
        val classMetrics: List<ClassMetrics>
) {
    var id: Long? = null
    var moduleId: Long = 0
    var outerInstabilityAvg: Double = classMetrics.map { it.outerInstability }.average()
    var outerInstabilityMed: Double = classMetrics.map { it.outerInstability }.median()
    var outerCouplingAvg: Double = classMetrics.map { it.outerCoupling }.average()
    var outerCouplingMed: Double = classMetrics.map { it.outerCoupling }.median()
    var innerInstabilityAvg: Double = classMetrics.map { it.innerInstability }.average()
    var innerInstabilityMed: Double = classMetrics.map { it.innerInstability }.median()
    var innerCouplingAvg: Double = classMetrics.map { it.innerCoupling }.average()
    var innerCouplingMed: Double = classMetrics.map { it.innerCoupling }.median()
}