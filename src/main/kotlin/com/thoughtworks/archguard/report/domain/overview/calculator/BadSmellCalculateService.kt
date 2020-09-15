package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import org.springframework.stereotype.Service

@Service
class BadSmellCalculateService(val moduleCalculator: ModuleOverSizingCalculator,
                               val packageCalculator: PackageOverSizingCalculator,
                               val classCalculator: ClassOverSizingCalculator,
                               val methodCalculator: MethodOverSizingCalculator) {


    fun calculateBadSmell(badSmell: BadSmell, systemId: Long): BadSmellOverviewItem {
        return when (badSmell) {
            BadSmell.MODULE_OVER_SIZING -> moduleCalculator.getOverSizingOverviewItem(systemId)
            BadSmell.PACKAGE_OVER_SIZING -> packageCalculator.getOverSizingOverviewItem(systemId)
            BadSmell.CLASS_OVER_SIZING -> classCalculator.getOverSizingOverviewItem(systemId)
            BadSmell.METHOD_OVER_SIZING -> methodCalculator.getOverSizingOverviewItem(systemId)
            else -> throw RuntimeException()
        }
    }
}
