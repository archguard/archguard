package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

abstract class BaseCouplingCalculator : BaseCalculator() {
    override fun getBadSmellCategory(): BadSmellCategory {
        return BadSmellCategory.COUPLING
    }

    override fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return BadSmellCalculateResult()
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        TODO("Not yet implemented")
    }
}
