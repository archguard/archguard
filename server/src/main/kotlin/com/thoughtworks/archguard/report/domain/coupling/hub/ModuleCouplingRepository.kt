package com.thoughtworks.archguard.report.domain.coupling.hub

import org.archguard.smell.BadSmellLevel

interface ModuleCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ModuleCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellLevel
    fun getAllCoupling(systemId: Long): List<ModuleCoupling>
}
