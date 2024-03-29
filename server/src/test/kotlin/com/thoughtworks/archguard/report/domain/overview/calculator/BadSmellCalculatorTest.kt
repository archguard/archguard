package com.thoughtworks.archguard.report.domain.overview.calculator

import org.archguard.smell.BadSmellLevelType
import org.archguard.smell.BadSmellType
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellTypeInjector
import org.archguard.smell.BadSmellGroup
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCouplingRepository
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationRepository
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyRepository
import com.thoughtworks.archguard.report.domain.sizing.SizingService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.archguard.smell.BadSmellLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BadSmellCalculatorTest {

    lateinit var classHubCalculator: ClassHubCouplingCalculator
    lateinit var dataClumpsCalculator: DataClumpsCouplingCalculator
    lateinit var deepInheritanceCalculator: DeepInheritanceCouplingCalculator
    lateinit var moduleCalculator: ModuleOverSizingCalculator
    lateinit var packageCalculator: PackageOverSizingCalculator
    lateinit var classCalculator: ClassOverSizingCalculator
    lateinit var methodCalculator: MethodOverSizingCalculator
    lateinit var circularDependencyCalculator: CircularDependencyCalculator
    lateinit var methodHubCalculator: MethodHubCouplingCalculator
    lateinit var packageHubCalculator: PackageHubCouplingCalculator
    lateinit var moduleHubCalculator: ModuleHubCouplingCalculator
    lateinit var redundantElementCalculator: RedundantElementCalculator
    lateinit var overGeneralizationCalculator: OverGeneralizationCalculator
    lateinit var shotgunSurgeryCalculator: ShotgunSurgeryCalculator
    lateinit var dataClassCalculator: DataClassCalculator

    @MockK
    lateinit var classCouplingRepository: ClassCouplingRepository

    @MockK
    lateinit var dataClumpsRepository: DataClumpsRepository

    @MockK
    lateinit var deepInheritanceRepository: DeepInheritanceRepository

    @MockK
    lateinit var sizingService: SizingService

    @MockK
    lateinit var circularDenpendencyRepository: CircularDependencyRepository

    @MockK
    lateinit var methodCouplingRepository: MethodCouplingRepository

    @MockK
    lateinit var packageCouplingRepository: PackageCouplingRepository

    @MockK
    lateinit var moduleCouplingRepository: ModuleCouplingRepository

    @MockK
    lateinit var redundancyRepository: RedundancyRepository

    @MockK
    lateinit var dataClassRepository: DataClassRepository

    @MockK
    lateinit var overGeneralizationRepository: OverGeneralizationRepository

    @MockK
    lateinit var shotgunSurgeryService: ShotgunSurgeryService

    @MockK
    lateinit var sleepCalculator: TestSleepCalculator

    @MockK
    lateinit var ignoreCalculator: TestIgnoreCalculator

    @MockK
    lateinit var unassertCalculator: TestUnassertCalculator

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        classHubCalculator = ClassHubCouplingCalculator(classCouplingRepository)
        dataClumpsCalculator = DataClumpsCouplingCalculator(dataClumpsRepository)
        deepInheritanceCalculator = DeepInheritanceCouplingCalculator(deepInheritanceRepository)
        moduleCalculator = ModuleOverSizingCalculator(sizingService)
        packageCalculator = PackageOverSizingCalculator(sizingService)
        classCalculator = ClassOverSizingCalculator(sizingService)
        methodCalculator = MethodOverSizingCalculator(sizingService)
        circularDependencyCalculator = CircularDependencyCalculator(circularDenpendencyRepository)
        methodHubCalculator = MethodHubCouplingCalculator(methodCouplingRepository)
        packageHubCalculator = PackageHubCouplingCalculator(packageCouplingRepository)
        moduleHubCalculator = ModuleHubCouplingCalculator(moduleCouplingRepository)
        redundantElementCalculator = RedundantElementCalculator(redundancyRepository, dataClassRepository)
        overGeneralizationCalculator = OverGeneralizationCalculator(overGeneralizationRepository)
        shotgunSurgeryCalculator = ShotgunSurgeryCalculator(shotgunSurgeryService)
        dataClassCalculator = DataClassCalculator(dataClassRepository)

        BadSmellTypeInjector(
            moduleCalculator, packageCalculator, classCalculator, methodCalculator,
            classHubCalculator, methodHubCalculator, packageHubCalculator, moduleHubCalculator,
            dataClumpsCalculator, deepInheritanceCalculator, circularDependencyCalculator,
            redundantElementCalculator, overGeneralizationCalculator,
            dataClassCalculator, shotgunSurgeryCalculator, sleepCalculator, unassertCalculator, ignoreCalculator
        )
            .postConstruct()
    }

    @Test
    fun should_calculate_class_hub_bad_smell_result() {
        val mockResult = BadSmellLevel(3, 3, 3)
        every { classCouplingRepository.getCouplingAboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result = BadSmellType.CLASSHUB.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.CLASSHUB)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.CLASSHUB.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.D)
        assertThat(result?.count).isEqualTo(9)
    }

    @Test
    fun should_calculate_Data_clumps_bad_smell_result() {
        val mockResult = BadSmellLevel(0, 0, 0)
        every { dataClumpsRepository.getLCOM4AboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result = BadSmellType.DATACLUMPS.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.DATACLUMPS)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.DATACLUMPS.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.A)
        assertThat(result?.count).isEqualTo(0)
    }

    @Test
    fun should_calculate_deep_inheritance_bad_smell_result() {
        val mockResult = BadSmellLevel(5, 0, 0)
        every { deepInheritanceRepository.getDitAboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result =
            BadSmellType.DEEPINHERITANCE.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.DEEPINHERITANCE)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.DEEPINHERITANCE.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.B)
        assertThat(result?.count).isEqualTo(5)
    }

    @Test
    fun should_calculate_module_sizing_bad_smell_result() {
        every { sizingService.getModuleSizingSmellCount(1) } returns 8L

        val result =
            BadSmellType.SIZINGMODULES.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.SIZINGMODULES)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGMODULES.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.C)
        assertThat(result?.count).isEqualTo(8L)
    }

    @Test
    fun should_calculate_package_sizing_bad_smell_result() {
        every { sizingService.getPackageSizingSmellCount(1) } returns 9

        val result =
            BadSmellType.SIZINGPACKAGE.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.SIZINGPACKAGE)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGPACKAGE.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.C)
        assertThat(result?.count).isEqualTo(9)
    }

    @Test
    fun should_calculate_class_sizing_bad_smell_result() {
        every { sizingService.getClassSizingSmellCount(1) } returns 2

        val result = BadSmellType.SIZINGCLASS.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.SIZINGCLASS)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGCLASS.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.A)
    }

    @Test
    fun should_calculate_method_sizing_bad_smell_result() {
        every { sizingService.getMethodSizingSmellCount(1) } returns 1

        val result = BadSmellType.SIZINGMETHOD.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.SIZINGMETHOD)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGMETHOD.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.A)
    }

    @Test
    fun should_calculate_circular_denpendency_bad_smell_result() {
        val mockResult = BadSmellLevel(12, 23, 34)
        every {
            circularDenpendencyRepository.getCircularDependencyBadSmellCalculateResult(
                any(),
                any(),
                any()
            )
        } returns mockResult

        val result =
            BadSmellType.CYCLEDEPENDENCY.badSmellCalculator?.getBadSmellOverviewItem(1, BadSmellType.CYCLEDEPENDENCY)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.CYCLEDEPENDENCY.value)
        assertThat(result?.category).isEqualTo(BadSmellGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevelType.D)
        assertThat(result?.count).isEqualTo(69 * 4)
    }
}
