package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.metrics.domain.MetricsServiceImpl
import com.thoughtworks.archguard.metrics.domain.abc.AbcService
import com.thoughtworks.archguard.metrics.domain.abstracts.AbstractAnalysisService
import com.thoughtworks.archguard.metrics.domain.coupling.ClassMetrics
import com.thoughtworks.archguard.metrics.domain.coupling.MetricsRepository
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetrics
import com.thoughtworks.archguard.metrics.domain.coupling.PackageMetrics
import com.thoughtworks.archguard.metrics.domain.noc.NocService
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MetricsServiceImplTest {

    private lateinit var service: MetricsServiceImpl

    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var dependencyService: DependencyService

    @MockK
    lateinit var metricsRepository: MetricsRepository

    @MockK
    lateinit var jClassRepository: JClassRepository

    @MockK
    lateinit var abstractAnalysisService: AbstractAnalysisService
    @MockK
    lateinit var jMethodRepository: JMethodRepository
    @MockK
    lateinit var nocService: NocService
    @MockK
    lateinit var abcService: AbcService
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = MetricsServiceImpl(metricsRepository, logicModuleRepository, dependencyService, jClassRepository,
                abstractAnalysisService, jMethodRepository, nocService, abcService)
    }

    private val allMetrics = ModuleMetrics(
            id = 1L,
            moduleName = "module1",
            packageMetrics = listOf(PackageMetrics(
                    id = 1L,
                    moduleId = 1L,
                    packageName = "package1",
                    classMetrics = listOf(ClassMetrics(
                            id = 1L,
                            packageId = 1L,
                            className = "class1",
                            innerFanIn = 1,
                            innerFanOut = 1,
                            outerFanIn = 1,
                            outerFanOut = 1,
                            innerInstability = 1.0000,
                            innerCoupling = 1.0000,
                            outerInstability = 1.0000,
                            outerCoupling = 1.0000
                    )),
                    innerCouplingAvg = 2.0000,
                    innerCouplingMed = 2.0000,
                    innerInstabilityAvg = 2.0000,
                    innerInstabilityMed = 2.0000,
                    outerCouplingAvg = 2.0000,
                    outerCouplingMed = 2.0000,
                    outerInstabilityAvg = 2.0000,
                    outerInstabilityMed = 2.0000
            )),
            innerInstabilityMed = 3.0000,
            innerInstabilityAvg = 3.0000,
            innerCouplingMed = 3.0000,
            innerCouplingAvg = 3.0000,
            outerInstabilityMed = 3.0000,
            outerInstabilityAvg = 3.0000,
            outerCouplingMed = 3.0000,
            outerCouplingAvg = 3.0000
    )

    private val moduleMetrics = ModuleMetrics(
            id = 1L,
            moduleName = "module1",
            packageMetrics = listOf(),
            innerInstabilityMed = 3.0000,
            innerInstabilityAvg = 3.0000,
            innerCouplingMed = 3.0000,
            innerCouplingAvg = 3.0000,
            outerInstabilityMed = 3.0000,
            outerInstabilityAvg = 3.0000,
            outerCouplingMed = 3.0000,
            outerCouplingAvg = 3.0000
    )

    @Test
    fun `group class coupling metrics by module name`() {
        val classMetrics1 = ClassMetrics.of("com.tw.arch.package1.class1", 0, 0, 0, 0)
        val classMetrics2 = ClassMetrics.of("com.tw.arch.package1.class2", 1, 1, 0, 0)
        val classMetrics3 = ClassMetrics.of("com.tw.arch.package2.class3", 1, 1, 0, 0)
        service = spyk(service)
        val packageMetrics = service.groupToPackage(listOf(classMetrics1, classMetrics2, classMetrics3))
        assertThat(packageMetrics.size).isEqualTo(2)
        assertThat(packageMetrics.filter { it.packageName == "com.tw.arch.package1" }[0].classMetrics)
                .containsAll(mutableListOf(classMetrics1, classMetrics2))
        assertThat(packageMetrics.filter { it.packageName == "com.tw.arch.package2" }[0].classMetrics)
                .containsAll(mutableListOf(classMetrics3))
    }

    @Test
    fun `should get module coupling`() {
        val element = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("com.test1"), LogicComponent.createLeaf("com.test2")))
        val element2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("com.test3"), LogicComponent.createLeaf("com.test4")))
        val element3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("com.test5"), LogicComponent.createLeaf("com.test6")))
        val dependency1 = Dependency(JClassVO("test1.clazz", "com"), JClassVO("test3.clazz", "com"))
        val dependency2 = Dependency(JClassVO("test4.clazz", "com"), JClassVO("test2.clazz", "com"))

        every { logicModuleRepository.getAll() } returns listOf(element, element2, element3)
        every { dependencyService.getAllClassDependencies() } returns listOf(dependency1, dependency2)
        val slot = slot<List<ModuleMetrics>>()
        every { metricsRepository.insert(capture(slot)) } just Runs

        service.calculateCoupling()

        assertEquals(0.0, slot.captured.filter { it.moduleName == "module1" }[0].outerCouplingAvg)
        assertEquals(0.5, slot.captured.filter { it.moduleName == "module1" }[0].outerInstabilityAvg)
        assertEquals(0.0, slot.captured.filter { it.moduleName == "module2" }[0].outerCouplingAvg)
        assertEquals(0.5, slot.captured.filter { it.moduleName == "module2" }[0].outerInstabilityAvg)
    }

    @Test
    fun `should be zero when no dependence`() {
        val element = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("com.test1"), LogicComponent.createLeaf("com.test2")))
        val element2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("com.test3"), LogicComponent.createLeaf("com.test4")))

        every { logicModuleRepository.getAll() } returns listOf(element, element2)
        every { dependencyService.getAllClassDependencies() } returns listOf()
        val slot = slot<List<ModuleMetrics>>()
        every { metricsRepository.insert(capture(slot)) } just Runs

        service.calculateCoupling()

        assertEquals(0, slot.captured.size)
    }

    @Test
    fun `should get all metrics by module name`() {
        val element = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("com.package1"), LogicComponent.createLeaf("com.package2")))

        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element)
        val slot = slot<List<String>>()
        every { metricsRepository.findAllMetrics(capture(slot)) } answers { listOf(allMetrics) }

        val result = service.getAllMetrics()

        assertEquals(1, slot.captured.size)
        assertEquals(element.name, slot.captured[0])
        assertEquals(allMetrics.packageMetrics[0].packageName, result[0].packageMetrics[0].packageName)
        assertEquals(allMetrics.packageMetrics[0].classMetrics[0].innerCoupling,
                result[0].packageMetrics[0].classMetrics[0].innerCoupling)
    }

    @Test
    fun `should get module metrics by module name`() {
        val element = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("com.package1"), LogicComponent.createLeaf("com.package2")))

        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element)
        val slot = slot<List<String>>()
        every { metricsRepository.findModuleMetrics(capture(slot)) } answers { listOf(moduleMetrics) }

        val result = service.getModuleMetrics()

        assertEquals(1, slot.captured.size)
        assertEquals(element.name, slot.captured[0])
        assertEquals(moduleMetrics.moduleName, result[0].moduleName)
        assertEquals(moduleMetrics.innerCouplingAvg, result[0].innerCouplingAvg)
    }
}
