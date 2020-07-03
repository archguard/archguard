package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.LogicModuleLegacy
import com.thoughtworks.archguard.module.domain.LogicModuleService
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

    private fun createLogicModule(id: String = UUID.randomUUID().toString(), name: String = "name", members: List<String> = listOf()): LogicModuleLegacy {
        return LogicModuleLegacy(id, name, members)
    }

    @Test
    fun `should get all logic modules`() {
        // given
        val logicModules = listOf(createLogicModule())
        every { service.getLogicModulesLegacy() } returns logicModules

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
        controller.updateLogicModule(id, logicModule)

        // then
        verify { service.updateLogicModule(any(), any()) }
    }

}
