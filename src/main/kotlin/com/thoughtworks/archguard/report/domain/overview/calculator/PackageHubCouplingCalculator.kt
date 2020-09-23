package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.coupling.PackageCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCouplingCalculator
import org.springframework.stereotype.Component

@Component
class PackageHubCouplingCalculator(val packageCouplingRepository: PackageCouplingRepository) : BaseCouplingCalculator() {
    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return packageCouplingRepository.getCouplingAboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    override fun getBadSmellType(): BadSmellType {
        return BadSmellType.PACKAGEHUB
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 8L until 40L
        val countRangeLevel2 = 40L until 80L
        val countRangeLevel3 = 80L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
