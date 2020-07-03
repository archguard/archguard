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
        val logicModule1 = NewLogicModule("1", "module1", listOf(SubModule("bm1"), SubModule("bm2")))
        val logicModule2 = NewLogicModule("2", "module2", listOf(SubModule("bm3"), SubModule("bm4")))
        val logicModule3 = NewLogicModule("3", "module3", listOf(SubModule("bm5")))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = NewDependency(createJClassFromFullName("bm1.any"), createJClassFromFullName("bm3.any"))
        val dependency2 = NewDependency(createJClassFromFullName("bm3.any"), createJClassFromFullName("bm2.any"))
        val dependency3 = NewDependency(createJClassFromFullName("bm5.any"), createJClassFromFullName("bm4.any"))
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { logicModuleRepository.getAllByShowStatusNew(true) } returns logicModules
        every { logicModuleRepository.getAllClassDependencyNew(any()) } returns dependencies

        // when
        val moduleGraph = service.getLogicModuleGraph()

        // then
        assertThat(moduleGraph.nodes.size).isEqualTo(3)
        assertThat(moduleGraph.edges.size).isEqualTo(3)
    }

    @Test
    fun `should map to module`() {
        val results = listOf(NewDependency(createJClassFromFullName("caller.method1"), createJClassFromFullName("callee.method1")),
                NewDependency(createJClassFromFullName("caller.method2"), createJClassFromFullName("callee.method2")))
        val logicModule1 = NewLogicModule("id1", "module1", listOf(createJClassFromFullName("caller.method1")))
        val logicModule2 = NewLogicModule("id2", "module2", listOf(createJClassFromFullName("callee.method1")))
        val logicModule3 = NewLogicModule("id3", "module3", listOf(createJClassFromFullName("callee.method1")))
        val logicModule4 = NewLogicModule("id4", "module4", listOf(createJClassFromFullName("caller.method2"), createJClassFromFullName("callee.method2")))
        val modules = listOf(logicModule1, logicModule2, logicModule3, logicModule4)
        val moduleDependency = service.mapClassDependenciesToModuleDependencies(results, modules)
        assertThat(moduleDependency.size).isEqualTo(2)
        assertThat(moduleDependency).containsAll(listOf(NewDependency(logicModule1, logicModule2), NewDependency(logicModule1, logicModule3)))
    }
}