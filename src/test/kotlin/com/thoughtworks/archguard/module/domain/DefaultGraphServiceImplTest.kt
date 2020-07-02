package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultGraphServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    private lateinit var service: DefaultGraphServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = DefaultGraphServiceImpl(logicModuleRepository)
    }

    @Test
    fun `should get graph of all logic modules dependency`() {
        // given
        val logicModule1 = LogicModule("1", "module1", listOf("bm1", "bm2"))
        val logicModule2 = LogicModule("2", "module2", listOf("bm3", "bm4"))
        val logicModule3 = LogicModule("3", "module3", listOf("bm5"))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = Dependency("bm1.any", "bm3.any")
        val dependency2 = Dependency("bm3.any", "bm2.any")
        val dependency3 = Dependency("bm5.any", "bm4.any")
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { logicModuleRepository.getAllByShowStatus(true) } returns logicModules
        every { logicModuleRepository.getAllClassDependency(any()) } returns dependencies

        // when
        val moduleGraph = service.getLogicModuleGraph()

        // then
        assertThat(moduleGraph.nodes.size).isEqualTo(3)
        assertThat(moduleGraph.edges.size).isEqualTo(3)
    }

    @Test
    fun `should map to module`() {
        val results = listOf(Dependency("caller.method1", "callee.method1"),
                Dependency("caller.method2", "callee.method2"))
        val modules = listOf(LogicModule("id1", "module1", listOf("caller.method1")),
                LogicModule("id2", "module2", listOf("callee.method1")),
                LogicModule("id3", "module3", listOf("callee.method1")),
                LogicModule("id4", "module4", listOf("caller.method2", "callee.method2")))
        val moduleDependency = service.mapClassDependenciesToModuleDependencies(results, modules)
        assertThat(moduleDependency.size).isEqualTo(2)
        assertThat(moduleDependency).containsAll(listOf(Dependency("module1", "module2"), Dependency("module1", "module3")))
    }
}