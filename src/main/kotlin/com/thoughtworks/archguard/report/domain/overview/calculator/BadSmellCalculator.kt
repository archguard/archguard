package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class BadSmellCalculator(val moduleCalculator: ModuleOverSizingCalculator,
                         val packageCalculator: PackageOverSizingCalculator,
                         val classCalculator: ClassOverSizingCalculator,
                         val methodCalculator: MethodOverSizingCalculator,
                         val classHubCalculator: ClassHubCouplingCalculator,
                         val dataClumpsCouplingCalculator: DataClumpsCouplingCalculator,
                         val deepInheritanceCouplingCalculator: DeepInheritanceCouplingCalculator) {


    fun calculateBadSmell(badSmell: BadSmellType, systemId: Long): BadSmellOverviewItem {
        return when (badSmell) {
            BadSmellType.SIZINGMODULES -> moduleCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.SIZINGPACKAGE -> packageCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.SIZINGCLASS -> classCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.SIZINGMETHOD -> methodCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.CLASSHUB -> classHubCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.DATACLUMPS -> dataClumpsCouplingCalculator.getOverSizingOverviewItem(systemId)
            BadSmellType.DEEPINHERITANCE -> deepInheritanceCouplingCalculator.getOverSizingOverviewItem(systemId)
            else -> throw RuntimeException()
        }
    }
}
