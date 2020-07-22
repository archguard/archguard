package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.metrics.ClassMetrics
import com.thoughtworks.archguard.module.domain.metrics.ModuleMetrics
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import io.mockk.spyk
import org.assertj.core.api.Assertions
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

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = MetricsServiceImpl(metricsRepository, logicModuleRepository, dependencyService)
    }

    @Test
    fun `group class coupling metrics by module name`() {
        val classMetrics1 = ClassMetrics.of("com.tw.arch.package1.class1", 0, 0, 0, 0)
        val classMetrics2 = ClassMetrics.of("com.tw.arch.package1.class2", 1, 1, 0, 0)
        val classMetrics3 = ClassMetrics.of("com.tw.arch.package2.class3", 1, 1, 0, 0)
        service = spyk(service)
        val packageMetrics = service.groupToPackage(listOf(classMetrics1, classMetrics2, classMetrics3))
        Assertions.assertThat(packageMetrics.size).isEqualTo(2)
        Assertions.assertThat(packageMetrics.filter { it.packageName == "com.tw.arch.package1" }[0].classMetrics)
                .containsAll(mutableListOf(classMetrics1, classMetrics2))
        Assertions.assertThat(packageMetrics.filter { it.packageName == "com.tw.arch.package2" }[0].classMetrics)
                .containsAll(mutableListOf(classMetrics3))
    }

    @Test
    fun `should get module coupling`() {
        val element = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("com.test1"), LogicComponent.createLeaf("com.test2")))
        val element2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("com.test3"), LogicComponent.createLeaf("com.test4")))
        val element3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("com.test5"), LogicComponent.createLeaf("com.test6")))
        val dependency1 = Dependency(JMethodVO("com", "test1.clazz", "any"), JMethodVO("com", "test3.clazz", "any"))
        val dependency2 = Dependency(JMethodVO("com", "test4.clazz", "any"), JMethodVO("com", "test2.clazz", "any"))

        every { logicModuleRepository.getAll() } returns listOf(element, element2, element3)
        every { dependencyService.getAll() } returns listOf(dependency1, dependency2)
        val slot = slot<List<ModuleMetrics>>()
        every { metricsRepository.insert(capture(slot)) } just Runs

        service.calculateCoupling()

        assertEquals(0.0, slot.captured.filter { it.moduleName == "module1" }[0].outerCouplingAvg)
        assertEquals(0.5, slot.captured.filter { it.moduleName == "module1" }[0].outerInstabilityAvg)
        assertEquals(0.0, slot.captured.filter { it.moduleName == "module2" }[0].outerCouplingAvg)
        assertEquals(0.5, slot.captured.filter { it.moduleName == "module2" }[0].outerInstabilityAvg)
    }
}