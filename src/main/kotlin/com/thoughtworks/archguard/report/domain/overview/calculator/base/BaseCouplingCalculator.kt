package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.application.DashboardGroup
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

abstract class BaseCouplingCalculator : BaseCalculator() {
    override fun getBadSmellCategory(): DashboardGroup {
        return DashboardGroup.COUPLING
    }

    override fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return BadSmellCalculateResult()
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        TODO("Not yet implemented")
    }
}
