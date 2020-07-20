package com.thoughtworks.archguard.module.domain.metrics

import org.nield.kotlinstatistics.median

data class ModuleMetrics(
        val moduleName: String,
        val packageMetrics: List<PackageMetrics>
) {
    var id: Long? = null
    val outerInstabilityAvg: Double = packageMetrics.flatMap { it.classMetrics }.map { it.outerInstability }.average()
    val outerInstabilityMed: Double = packageMetrics.flatMap { it.classMetrics }.map { it.outerInstability }.median()
    val outerCouplingAvg: Double = packageMetrics.flatMap { it.classMetrics }.map { it.outerCoupling }.average()
    val outerCouplingMed: Double = packageMetrics.flatMap { it.classMetrics }.map { it.outerCoupling }.median()
    val innerInstabilityAvg: Double = packageMetrics.flatMap { it.classMetrics }.map { it.innerInstability }.average()
    val innerInstabilityMed: Double = packageMetrics.flatMap { it.classMetrics }.map { it.innerInstability }.median()
    val innerCouplingAvg: Double = packageMetrics.flatMap { it.classMetrics }.map { it.innerCoupling }.average()
    val innerCouplingMed: Double = packageMetrics.flatMap { it.classMetrics }.map { it.innerCoupling }.median()
}