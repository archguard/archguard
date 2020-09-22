package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.MethodCoupling
import com.thoughtworks.archguard.report.domain.coupling.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class MethodCouplingRepositoryImpl(val jdbi: Jdbi) : MethodCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<MethodCoupling> {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        TODO("Not yet implemented")
    }

    override fun getAllCoupling(systemId: Long): List<MethodCoupling> {
        TODO("Not yet implemented")
    }
}