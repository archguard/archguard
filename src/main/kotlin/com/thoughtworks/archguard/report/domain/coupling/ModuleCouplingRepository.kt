import com.thoughtworks.archguard.report.domain.coupling.ModuleCoupling

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

interface ModuleCouplingRepository {
    fun getCouplingAboveThreshold(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ModuleCoupling>
    fun getCouplingAboveThresholdCount(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int): Long
    fun getCouplingAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
    fun getAllCoupling(systemId: Long): List<ModuleCoupling>
}