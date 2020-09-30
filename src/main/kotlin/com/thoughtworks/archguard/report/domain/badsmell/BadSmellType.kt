package com.thoughtworks.archguard.report.domain.badsmell

import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import com.thoughtworks.archguard.report.domain.overview.calculator.*
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCalculator
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


enum class BadSmellType(val value: String, var badSmellCalculator: BaseCalculator?) {
    DATACLUMPS("数据泥团", null),
    DEEPINHERITANCE("过深继承", null),
    CLASSHUB("枢纽类", null),
    METHODHUB("枢纽方法", null),
    PACKAGEHUB("枢纽包", null),
    MODULEHUB("枢纽模块", null),
    CYCLEDEPENDENCY("循环依赖", null),

    SIZINGMODULES("子模块过大", null),
    SIZINGPACKAGE("包过大", null),
    SIZINGMETHOD("方法过大", null),
    SIZINGCLASS("类过大", null);

    fun calculate(systemId: Long): BadSmellOverviewItem? {
        return badSmellCalculator?.getBadSmellOverviewItem(systemId, this)
    }

    @Component
    class BadSmellTypeInjector(val moduleCalculator: ModuleOverSizingCalculator,
                               val packageCalculator: PackageOverSizingCalculator,
                               val classCalculator: ClassOverSizingCalculator,
                               val methodCalculator: MethodOverSizingCalculator,
                               val classHubCalculator: ClassHubCouplingCalculator,
                               val methodHubCalculator: MethodHubCouplingCalculator,
                               val packageHubCalculator: PackageHubCouplingCalculator,
                               val moduleHubCalculator: ModuleHubCouplingCalculator,
                               val dataClumpsCouplingCalculator: DataClumpsCouplingCalculator,
                               val deepInheritanceCouplingCalculator: DeepInheritanceCouplingCalculator,
                               val circularDependencyCalculator: CircularDependencyCalculator) {
        @PostConstruct
        fun postConstruct() {
            DATACLUMPS.badSmellCalculator = dataClumpsCouplingCalculator
            DEEPINHERITANCE.badSmellCalculator = deepInheritanceCouplingCalculator
            CLASSHUB.badSmellCalculator = classHubCalculator
            METHODHUB.badSmellCalculator = methodCalculator
            PACKAGEHUB.badSmellCalculator = packageHubCalculator
            MODULEHUB.badSmellCalculator = moduleHubCalculator
            CYCLEDEPENDENCY.badSmellCalculator = circularDependencyCalculator

            SIZINGMODULES.badSmellCalculator = moduleCalculator
            SIZINGPACKAGE.badSmellCalculator = packageCalculator
            SIZINGMETHOD.badSmellCalculator = methodHubCalculator
            SIZINGCLASS.badSmellCalculator = classCalculator
        }
    }
}

enum class BadSmellLevel {
    A, B, C, D
}