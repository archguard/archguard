package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DependencyServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @InjectMockKs
    var service: DependencyService = DependencyServiceImpl()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should get dependency between logic modules`() {
        // given
        val caller = "module1"
        val callee = "module2"
        val dependency1 = ModuleDependency("module1", "any", "any", "module2", "any", "any")
        val dependency2 = ModuleDependency("module2", "any", "any", "module3", "any", "any")
        val dependencies = listOf(dependency1, dependency2)
        every { logicModuleRepository.getDependence(caller, callee) } returns dependencies

        // when
        val actual = service.getLogicModulesDependencies(caller, callee)

        // then
        assertThat(actual.size).isEqualTo(dependencies.size)
    }
}