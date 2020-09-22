package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.PackageCoupling
import com.thoughtworks.archguard.report.domain.coupling.PackageCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class PackageCouplingRepositoryImpl(val jdbi: Jdbi) : PackageCouplingRepository {
    override fun getCouplingAboveThreshold(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<PackageCoupling> {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveThresholdCount(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult {
        TODO("Not yet implemented")
    }

    override fun getAllCoupling(systemId: Long): List<PackageCoupling> {
        TODO("Not yet implemented")
    }
}