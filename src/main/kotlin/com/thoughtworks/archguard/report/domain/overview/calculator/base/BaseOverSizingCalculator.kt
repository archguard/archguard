package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.application.DashboardGroup

abstract class BaseOverSizingCalculator : BaseCalculator() {
    override fun getBadSmellCategory(): DashboardGroup {
        return DashboardGroup.SIZING
    }
}
