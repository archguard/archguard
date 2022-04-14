package com.thoughtworks.archguard.code.module.controller

import com.thoughtworks.archguard.code.module.domain.LogicModuleService
import com.thoughtworks.archguard.code.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.code.module.domain.graph.GraphService
import com.thoughtworks.archguard.code.module.domain.model.LogicComponent
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import com.thoughtworks.archguard.code.module.infrastructure.dto.LogicModuleLegacy
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class LogicModuleControllerTest {
    @MockK
    private lateinit var service: LogicModuleService

    @MockK
    private lateinit var dependencyService: DependencyService

    @MockK
    private lateinit var graphService: GraphService
    private lateinit var controller: LogicModuleController

    @BeforeEach
    fun setUp() {
        init(this)
        controller = LogicModuleController(service, dependencyService, graphService)
    }

    private fun createLogicModule(id: String = UUID.randomUUID().toString(), name: String = "name", members: List<String> = listOf()): LogicModule {
        return LogicModule(id, name, members.map { LogicComponent.createLeaf(it) })
    }

    @Test
    fun `should get all logic modules`() {
        val systemId: Long = 1
        // given
        val logicModules = listOf(LogicModule.createWithOnlyLeafMembers("id", "name", listOf(LogicComponent.createLeaf("module"))))
        every { service.getLogicModules(systemId) } returns logicModules

        // when
        val actual = controller.getLogicModules(systemId)

        // then
        assertThat(actual).isNotEmpty
        assertThat(actual.size).isEqualTo(1)
    }

    @Test
    fun `should update LogicModule`() {
        // given
        val systemId: Long = 1
        val id = "any"
        val logicModule = createLogicModule(id)
        every { service.updateLogicModule(systemId, any(), any()) } just runs

        // when
        controller.updateLogicModule(systemId, id, LogicModuleLegacy.fromLogicModule(logicModule))

        // then
        verify { service.updateLogicModule(systemId, any(), any()) }
    }
}
