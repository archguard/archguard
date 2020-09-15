package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BadSmellCalculateService() {

    @Autowired
    lateinit var moduleCalculator: ModuleOverSizingCalculator

    fun calculateBadSmell(badSmell: BadSmell, systemId: Long): BadSmellOverviewItem {
        return when (badSmell) {
            BadSmell.MODULE_OVER_SIZING -> moduleCalculator.getOverSizingOverviewItem(systemId)
        }
    }
}
