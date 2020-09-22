package com.thoughtworks.archguard.report.domain.coupling

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

interface PackageCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<PackageCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
    fun getAllCoupling(systemId: Long): List<PackageCoupling>
}