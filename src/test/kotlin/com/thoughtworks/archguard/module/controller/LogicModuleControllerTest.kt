package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.infrastructure.dto.LogicModuleLegacy
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
        return LogicModule(id, name, members.map { LogicComponent.createLeaf(it) })
    }

    @Test
    fun `should get all logic modules`() {
        val projectId: Long = 1
        // given
        val logicModules = listOf(LogicModule.createWithOnlyLeafMembers("id", "name", listOf(LogicComponent.createLeaf("module"))))
        every { service.getLogicModules(projectId) } returns logicModules

        // when
        val actual = controller.getLogicModules(projectId)

        // then
        assertThat(actual).isNotEmpty
        assertThat(actual.size).isEqualTo(1)
    }

    @Test
    fun `should update LogicModule`() {
        // given
        val projectId: Long = 1
        val id = "any"
        val logicModule = createLogicModule(id)
        every { service.updateLogicModule(projectId, any(), any()) } just runs

        // when
        controller.updateLogicModule(projectId, id, LogicModuleLegacy.fromLogicModule(logicModule))

        // then
        verify { service.updateLogicModule(projectId, any(), any()) }
    }

}
