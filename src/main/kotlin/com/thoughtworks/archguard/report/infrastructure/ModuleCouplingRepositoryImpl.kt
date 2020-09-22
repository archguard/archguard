package com.thoughtworks.archguard.report.infrastructure

import ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.ModuleCoupling
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ModuleCouplingRepositoryImpl(val jdbi: Jdbi) : ModuleCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ModuleCoupling> {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        TODO("Not yet implemented")
    }

    override fun getAllCoupling(systemId: Long): List<ModuleCoupling> {
        TODO("Not yet implemented")
    }
}