package com.thoughtworks.archguard.dependence.domain.logic_module

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogicModuleServiceTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository
    @MockK
    lateinit var baseModuleRepository: BaseModuleRepository
    @InjectMockKs
    var service : LogicModuleService = LogicModuleService()
    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    fun `should get module coupling`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        val element3 = LogicModule(null, "module3", listOf("com.test5", "com.test6"))
        val dependency1 = ModuleGraphDependency("com.test1", "com.test3")
        val dependency2 = ModuleGraphDependency("com.test4", "com.test2")
        every { logicModuleRepository.getAll() } returns listOf(element, element2, element3)
        every { logicModuleRepository.getAllDependence(any()) } returns listOf(dependency1, dependency2)
        //when
        val logicModuleCoupling = service.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].fanIn).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].fanOut).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module2" }[0].fanIn).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module2" }[0].fanOut).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module3" }[0].fanIn).isEqualTo(0)
        assertThat(logicModuleCoupling.filter { it.module == "module3" }[0].fanOut).isEqualTo(0)
    }

    @Test
    fun `should be zero when no dependence`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        every { logicModuleRepository.getAll() } returns listOf(element, element2)
        every { logicModuleRepository.getAllDependence(any()) } returns listOf()
        //when
        val logicModuleCoupling = service.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].moduleInstability).isEqualTo(0.0)
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].moduleCoupling).isEqualTo(0.0)
    }
}