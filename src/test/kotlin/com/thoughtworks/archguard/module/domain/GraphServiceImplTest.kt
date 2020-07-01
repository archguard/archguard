package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GraphServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @InjectMockKs
    var service: GraphService = GraphServiceImpl()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
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

}