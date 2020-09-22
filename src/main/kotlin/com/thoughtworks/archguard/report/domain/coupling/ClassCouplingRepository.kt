package com.thoughtworks.archguard.report.domain.coupling

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult


interface ClassCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ClassCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
    fun getAllCoupling(systemId: Long): List<ClassCoupling>
}
