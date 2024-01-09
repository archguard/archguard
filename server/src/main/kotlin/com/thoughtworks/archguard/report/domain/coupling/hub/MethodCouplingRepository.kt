package com.thoughtworks.archguard.report.domain.coupling.hub

import org.archguard.smell.BadSmellResult

interface MethodCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<MethodCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellResult
    fun getAllCoupling(systemId: Long): List<MethodCoupling>
}
