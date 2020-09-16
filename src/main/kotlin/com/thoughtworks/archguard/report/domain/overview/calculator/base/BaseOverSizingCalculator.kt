package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory

abstract class BaseOverSizingCalculator : BaseCalculator() {
    override fun getBadSmellCategory(): BadSmellCategory {
        return BadSmellCategory.OVER_SIZING
    }
}
