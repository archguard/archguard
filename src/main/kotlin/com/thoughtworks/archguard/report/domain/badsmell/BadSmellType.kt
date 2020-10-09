package com.thoughtworks.archguard.report.domain.badsmell

import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellLevelCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.CircularDependencyCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.ClassHubCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.ClassOverSizingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.DataClassCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.DataClumpsCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.DeepInheritanceCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.MethodHubCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.MethodOverSizingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.ModuleHubCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.ModuleOverSizingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.OverGeneralizationCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.PackageHubCouplingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.PackageOverSizingCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.RedundantElementCalculator
import com.thoughtworks.archguard.report.domain.overview.calculator.ShotgunSurgeryCalculator
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


enum class BadSmellType(val value: String, var badSmellCalculator: BadSmellLevelCalculator?) {
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
    SIZINGCLASS("类过大", null),

    DATA_CLASS("数据类", null),
    SHOTGUN_SURGERY("散弹式修改", null),

    REDUNDANT_ELEMENT("冗余元素", null),
    OVER_GENERALIZATION("过度泛化", null);

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
                               val circularDependencyCalculator: CircularDependencyCalculator,
                               val redundantElementCalculator: RedundantElementCalculator,
                               val overGeneralizationCalculator: OverGeneralizationCalculator,
                               val dataClassCalculator: DataClassCalculator,
                               val shotgunSurgeryCalculator: ShotgunSurgeryCalculator
    ) {
        @PostConstruct
        fun postConstruct() {
            SIZINGMODULES.badSmellCalculator = moduleCalculator
            SIZINGPACKAGE.badSmellCalculator = packageCalculator
            SIZINGMETHOD.badSmellCalculator = methodCalculator
            SIZINGCLASS.badSmellCalculator = classCalculator

            DATACLUMPS.badSmellCalculator = dataClumpsCouplingCalculator
            DEEPINHERITANCE.badSmellCalculator = deepInheritanceCouplingCalculator
            CLASSHUB.badSmellCalculator = classHubCalculator
            METHODHUB.badSmellCalculator = methodHubCalculator
            PACKAGEHUB.badSmellCalculator = packageHubCalculator
            MODULEHUB.badSmellCalculator = moduleHubCalculator
            CYCLEDEPENDENCY.badSmellCalculator = circularDependencyCalculator

            REDUNDANT_ELEMENT.badSmellCalculator = redundantElementCalculator
            OVER_GENERALIZATION.badSmellCalculator = overGeneralizationCalculator

            DATA_CLASS.badSmellCalculator = dataClassCalculator
            SHOTGUN_SURGERY.badSmellCalculator = shotgunSurgeryCalculator
        }
    }
}

enum class BadSmellLevel {
    A, B, C, D
}