package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.controller.module.LogicModuleController
import com.thoughtworks.archguard.dependence.domain.module.LogicModule
import com.thoughtworks.archguard.dependence.domain.module.LogicModuleService
import com.thoughtworks.archguard.dependence.domain.module.ModuleCouplingReport
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class LogicModuleControllerTest {
    @MockK
    lateinit var service: LogicModuleService
    @InjectMockKs
    var controller = LogicModuleController()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    private fun createLogicModule(id: String = UUID.randomUUID().toString(), name: String = "name", members: List<String> = listOf()): LogicModule {
        return LogicModule(id, name, members)
    }

    @Test
    fun `should get all logic modules`() {
        // given
        val logicModules = listOf(createLogicModule())
        every { service.getLogicModules() } returns logicModules

        // when
        val actual = controller.getLogicModules()

        // then
        assertThat(actual).isNotEmpty
        assertThat(actual.size).isEqualTo(1)
    }

    @Test
    fun `should update LogicModule`() {
        // given
        val id = "any"
        val logicModule = createLogicModule(id)
        every { service.updateLogicModule(any(), any()) } just runs

        // when
        controller.updateLogicModule(id ,logicModule)

        // then
        verify { service.updateLogicModule(any(), any()) }
    }

    @Test
    fun `should get module coupling`() {
        //given
        val moduleCoupling = listOf(ModuleCouplingReport())
        every { service.getLogicModuleCoupling() } returns moduleCoupling

        //when
        val logicModuleCoupling = controller.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling).isNotEmpty
        assertThat(logicModuleCoupling.size).isEqualTo(1)
    }
}
