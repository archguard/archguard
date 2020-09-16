package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory

abstract class BaseCouplingCalculator : BaseCalculator() {
    override fun getBadSmellCategory(): BadSmellCategory {
        return BadSmellCategory.COUPLING
    }
}
