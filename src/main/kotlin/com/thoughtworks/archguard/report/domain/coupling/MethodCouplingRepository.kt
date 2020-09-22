package com.thoughtworks.archguard.report.domain.coupling

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

interface MethodCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<MethodCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
    fun getAllCoupling(systemId: Long): List<MethodCoupling>
}
