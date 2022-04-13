package com.thoughtworks.archguard.code.module.domain.graph

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.code.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.code.module.domain.model.LogicComponent
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import com.thoughtworks.archguard.code.module.domain.plugin.DependPlugin
import com.thoughtworks.archguard.code.module.domain.plugin.PluginManager
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GraphServiceTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var dependencyService: DependencyService

    @MockK
    lateinit var pluginManager: PluginManager

    private lateinit var service: GraphService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = GraphService(logicModuleRepository, dependencyService, pluginManager)
    }

    @Test
    fun `should get graph of all logic modules dependency`() {
        val systemId: Long = 1;
        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("submodule1.class")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("submodule2.class")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("submodule3.class")))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = Dependency(JClassVO("class", "submodule1"), JClassVO("class", "submodule2"))
        val dependency2 = Dependency(JClassVO("class", "submodule1"), JClassVO("class", "submodule3"))
        val dependency3 = Dependency(JClassVO("class", "submodule2"), JClassVO("class", "submodule3"))
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { pluginManager.getDependPlugin<DependPlugin>(systemId) } returns emptyList()
        every { logicModuleRepository.getAllByShowStatus(systemId, true) } returns logicModules
        every { dependencyService.getAllClassDependencies(systemId) } returns dependencies

        // when
        val moduleGraph = service.getLogicModuleGraph(systemId)

        // then
        assertEquals(3, moduleGraph.nodes.size)
        assertEquals(3, moduleGraph.edges.size)
    }

    @Test
    fun `map bottom logic module to top level logic module`() {
        val systemId: Long = 1;
        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("caller.method1")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("callee.method1")))
        val logicModule4 = LogicModule.createWithOnlyLeafMembers("id4", "module4", listOf(LogicComponent.createLeaf("caller.method2"), LogicComponent.createLeaf("callee.method2")))
        val logicModule5 = LogicModule.createWithOnlyLeafMembers("id5", "module5", listOf(LogicComponent.createLeaf("module5")))

        val service1 = LogicModule.create("id11", "lg11", emptyList(), listOf(logicModule1, logicModule3))
        val service2 = LogicModule.create("id12", "lg12", emptyList(), listOf(logicModule2, logicModule4))
        val bottomLogicModules = listOf(Dependency(logicModule1, logicModule2),
                Dependency(logicModule3, logicModule4), Dependency(logicModule4, logicModule5))
        every { logicModuleRepository.getAllBySystemId(systemId) } returns listOf(logicModule1, logicModule2, logicModule3, logicModule4, service1, service2)
        val serviceDependencies = service.mapModuleDependencyToServiceDependency(systemId, bottomLogicModules)
        Assertions.assertThat(serviceDependencies.size).isEqualTo(3)
        val results = listOf(Dependency(service1, service2), Dependency(service1, service2), Dependency(service2, logicModule5))
        Assertions.assertThat(serviceDependencies).usingRecursiveFieldByFieldElementComparator().containsAll(results)

    }

}
