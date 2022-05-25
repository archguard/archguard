package com.thoughtworks.archguard.report.domain.badsmell

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

@Component
class BadSmellTypeInjector(
    val moduleCalculator: ModuleOverSizingCalculator,
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
        BadSmellType.SIZINGMODULES.badSmellCalculator = moduleCalculator
        BadSmellType.SIZINGPACKAGE.badSmellCalculator = packageCalculator
        BadSmellType.SIZINGMETHOD.badSmellCalculator = methodCalculator
        BadSmellType.SIZINGCLASS.badSmellCalculator = classCalculator

        BadSmellType.DATACLUMPS.badSmellCalculator = dataClumpsCouplingCalculator
        BadSmellType.DEEPINHERITANCE.badSmellCalculator = deepInheritanceCouplingCalculator
        BadSmellType.CLASSHUB.badSmellCalculator = classHubCalculator
        BadSmellType.METHODHUB.badSmellCalculator = methodHubCalculator
        BadSmellType.PACKAGEHUB.badSmellCalculator = packageHubCalculator
        BadSmellType.MODULEHUB.badSmellCalculator = moduleHubCalculator
        BadSmellType.CYCLEDEPENDENCY.badSmellCalculator = circularDependencyCalculator

        BadSmellType.REDUNDANT_ELEMENT.badSmellCalculator = redundantElementCalculator
        BadSmellType.OVER_GENERALIZATION.badSmellCalculator = overGeneralizationCalculator

        BadSmellType.DATA_CLASS.badSmellCalculator = dataClassCalculator
        BadSmellType.SHOTGUN_SURGERY.badSmellCalculator = shotgunSurgeryCalculator
    }
}
