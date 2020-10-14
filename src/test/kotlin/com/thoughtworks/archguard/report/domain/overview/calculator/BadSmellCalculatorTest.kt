package com.thoughtworks.archguard.report.domain.overview.calculator

import ModuleCouplingRepository
import com.thoughtworks.archguard.report.application.DashboardGroup
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellLevel
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.coupling.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.PackageCouplingRepository
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationRepository
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyRepository
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
    lateinit var sizingRepository: SizingRepository

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
        moduleCalculator = ModuleOverSizingCalculator(sizingRepository)
        packageCalculator = PackageOverSizingCalculator(sizingRepository)
        classCalculator = ClassOverSizingCalculator(sizingRepository)
        methodCalculator = MethodOverSizingCalculator(sizingRepository)
        circularDependencyCalculator = CircularDependencyCalculator(circularDenpendencyRepository)
        methodHubCalculator = MethodHubCouplingCalculator(methodCouplingRepository)
        packageHubCalculator = PackageHubCouplingCalculator(packageCouplingRepository)
        moduleHubCalculator = ModuleHubCouplingCalculator(moduleCouplingRepository)
        redundantElementCalculator = RedundantElementCalculator(redundancyRepository, dataClassRepository)
        overGeneralizationCalculator = OverGeneralizationCalculator(overGeneralizationRepository)
        shotgunSurgeryCalculator = ShotgunSurgeryCalculator(shotgunSurgeryService)
        dataClassCalculator = DataClassCalculator(dataClassRepository)

        BadSmellType.BadSmellTypeInjector(moduleCalculator, packageCalculator, classCalculator, methodCalculator,
                classHubCalculator, methodHubCalculator, packageHubCalculator, moduleHubCalculator,
                dataClumpsCalculator, deepInheritanceCalculator, circularDependencyCalculator,
                redundantElementCalculator, overGeneralizationCalculator,
                dataClassCalculator, shotgunSurgeryCalculator, sleepCalculator, unassertCalculator, ignoreCalculator)
                .postConstruct()
    }

    @Test
    fun should_calculate_class_hub_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(3, 3, 3)
        every { classCouplingRepository.getCouplingAboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result = BadSmellType.CLASSHUB.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.CLASSHUB.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.D)
        assertThat(result?.count).isEqualTo(9)
    }

    @Test
    fun should_calculate_Data_clumps_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(0, 0, 0)
        every { dataClumpsRepository.getLCOM4AboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result = BadSmellType.DATACLUMPS.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.DATACLUMPS.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.A)
        assertThat(result?.count).isEqualTo(0)
    }

    @Test
    fun should_calculate_deep_inheritance_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(5, 0, 0)
        every { deepInheritanceRepository.getDitAboveBadSmellCalculateResult(any(), any()) } returns mockResult
        val result = BadSmellType.DEEPINHERITANCE.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.DEEPINHERITANCE.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.B)
        assertThat(result?.count).isEqualTo(5)
    }

    @Test
    fun should_calculate_module_sizing_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(5, 0, 0)
        val mockResultLine = BadSmellCalculateResult(1, 2, 0)
        every { sizingRepository.getModuleSizingListAbovePackageCountBadSmellResult(any(), any()) } returns mockResult
        every { sizingRepository.getModuleSizingAboveLineBadSmellResult(any(), any()) } returns mockResultLine

        val result = BadSmellType.SIZINGMODULES.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGMODULES.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.C)
        assertThat(result?.count).isEqualTo(8)
    }

    @Test
    fun should_calculate_package_sizing_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(5, 0, 1)
        val mockResultLine = BadSmellCalculateResult(1, 2, 0)
        every { sizingRepository.getPackageSizingListAboveClassCountBadSmellResult(any(), any()) } returns mockResult
        every { sizingRepository.getPackageSizingAboveLineBadSmellResult(any(), any()) } returns mockResultLine

        val result = BadSmellType.SIZINGPACKAGE.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGPACKAGE.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.D)
        assertThat(result?.count).isEqualTo(9)
    }

    @Test
    fun should_calculate_class_sizing_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(0, 0, 1)
        val mockResultLine = BadSmellCalculateResult(1, 0, 0)
        every { sizingRepository.getClassSizingListAboveMethodCountBadSmellResult(any(), any()) } returns mockResult
        every { sizingRepository.getClassSizingAboveLineBadSmellResult(any(), any()) } returns mockResultLine

        val result = BadSmellType.SIZINGCLASS.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGCLASS.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.D)
        assertThat(result?.count).isEqualTo(2)
    }


    @Test
    fun should_calculate_method_sizing_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(0, 0, 0)
        every { sizingRepository.getMethodSizingAboveLineBadSmellResult(any(), any()) } returns mockResult

        val result = BadSmellType.SIZINGMETHOD.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.SIZINGMETHOD.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.SIZING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.A)
        assertThat(result?.count).isEqualTo(0)
    }

    @Test
    fun should_calculate_circular_denpendency_bad_smell_result() {
        val mockResult = BadSmellCalculateResult(12, 23, 34)
        every { circularDenpendencyRepository.getCircularDependencyBadSmellCalculateResult(any(), any(), any()) } returns mockResult

        val result = BadSmellType.CYCLEDEPENDENCY.calculate(1)

        assertThat(result?.badSmell).isEqualTo(BadSmellType.CYCLEDEPENDENCY.value)
        assertThat(result?.category).isEqualTo(DashboardGroup.COUPLING.value)
        assertThat(result?.level).isEqualTo(BadSmellLevel.D)
        assertThat(result?.count).isEqualTo(69 * 4)
    }
}
