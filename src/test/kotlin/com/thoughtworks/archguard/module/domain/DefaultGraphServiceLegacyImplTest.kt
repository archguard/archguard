package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.model.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultGraphServiceLegacyImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var dependencyService: DependencyService

    private lateinit var service: DefaultGraphServiceLegacyImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = DefaultGraphServiceLegacyImpl(logicModuleRepository, dependencyService)
    }

    @Test
    fun `should get graph of all logic modules dependency`() {
        // given
        val logicModule1 = LogicModule("1", "module1", listOf(SubModule("bm1"), SubModule("bm2")))
        val logicModule2 = LogicModule("2", "module2", listOf(SubModule("bm3"), SubModule("bm4")))
        val logicModule3 = LogicModule("3", "module3", listOf(SubModule("bm5")))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = Dependency(JClassVO("any","bm1"), JClassVO("any","bm3"))
        val dependency2 = Dependency(JClassVO("any","bm3"), JClassVO("any","bm2"))
        val dependency3 = Dependency(JClassVO("any","bm5"), JClassVO("any","bm4"))
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { logicModuleRepository.getAllByShowStatus(true) } returns logicModules
        every { dependencyService.getAllClassDependencyLegacy(any()) } returns dependencies
        every { logicModuleRepository.getAll() } returns logicModules

        // when
        val moduleGraph = service.getLogicModuleGraphLegacy()

        // then
        assertThat(moduleGraph.nodes.size).isEqualTo(3)
        assertThat(moduleGraph.edges.size).isEqualTo(3)
    }

    @Test
    fun `should map to module`() {
        val results = listOf(Dependency(JClassVO( "method1", "caller"), JClassVO( "method1", "callee")),
                Dependency(JClassVO("method2", "caller"), JClassVO( "method2", "callee")))
        val logicModule1 = LogicModule("id1", "module1", listOf(LogicComponent.createLeaf("caller.method1")))
        val logicModule2 = LogicModule("id2", "module2", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule3 = LogicModule("id3", "module3", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule4 = LogicModule("id4", "module4", listOf(LogicComponent.createLeaf("caller.method2"), LogicComponent.createLeaf("callee.method2")))
        val modules = listOf(logicModule1, logicModule2, logicModule3, logicModule4)
        val moduleDependency = service.mapClassDependenciesToModuleDependencies(results, modules)
        assertThat(moduleDependency.size).isEqualTo(2)
        assertThat(moduleDependency).containsAll(listOf(Dependency(logicModule1, logicModule2), Dependency(logicModule1, logicModule3)))
    }

    @Test
    fun `map bottom logic module to top level logic module`() {
        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("caller.method1")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule4 = LogicModule.createWithOnlyLeafMembers("id4", "module4", listOf(LogicComponent.createLeaf("caller.method2"), LogicComponent.createLeaf("callee.method2")))
        val logicModule5 = LogicModule.createWithOnlyLeafMembers("id5", "module5", listOf(LogicComponent.createLeaf("module5")))

        val service1 = LogicModule.createWithOnlyLogicModuleMembers("id11", "lg11", listOf(logicModule1, logicModule3))
        val service2 = LogicModule.createWithOnlyLogicModuleMembers("id12", "lg12", listOf(logicModule2, logicModule4))
        val bottomLogicModules = listOf(Dependency(logicModule1, logicModule2),
                Dependency(logicModule3, logicModule4), Dependency(logicModule4, logicModule5))
        every { logicModuleRepository.getAll() } returns listOf(logicModule1, logicModule2, logicModule3, logicModule4, service1, service2)
        val serviceDependencies = service.mapModuleDependencyToServiceDependency(bottomLogicModules)
        assertThat(serviceDependencies.size).isEqualTo(3)
        val results = listOf(Dependency(service1, service2), Dependency(service1, service2), Dependency(service2, logicModule5))
        assertThat(serviceDependencies).usingRecursiveFieldByFieldElementComparator().containsAll(results)

    }
}
