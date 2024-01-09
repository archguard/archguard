package com.thoughtworks.archguard.code.module.domain

import org.archguard.arch.LeafManger
import org.archguard.arch.LogicModule
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LogicModuleServiceTest {
    @MockK
    private lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    private lateinit var couplingService: CouplingService

    private lateinit var service: LogicModuleService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        service = LogicModuleService(logicModuleRepository, couplingService)
    }

    @Test
    fun `should get logic modules`() {
        // given
        val systemId: Long = 1
        val logicModule1 = LogicModule("1", "m1", listOf(LeafManger.createLeaf("bm1"), LeafManger.createLeaf("bm2")))
        val logicModule2 = LogicModule("2", "m2", listOf(LeafManger.createLeaf("bm3"), LeafManger.createLeaf("bm4")))
        val logicModules = listOf(logicModule1, logicModule2)
        every { logicModuleRepository.getAllBySystemId(systemId) } returns logicModules

        // when
        val actual = service.getLogicModules(systemId)

        // then
        assertThat(actual.size).isEqualTo(logicModules.size)
    }

    @Test
    fun `should update and calculate when update module`() {
        val systemId: Long = 1
        val logicModule = LogicModule.createWithOnlyLeafMembers("1", "m1",
            listOf(LeafManger.createLeaf("bm1"), LeafManger.createLeaf("bm2")))

        val slot = slot<LogicModule>()
        every { logicModuleRepository.update(capture(slot()), capture(slot)) } just Runs

        service.updateLogicModule(systemId, "id", logicModule)

        verify(exactly = 1) { couplingService.persistAllClassCouplingResults(systemId) }
        assertEquals(logicModule.name, slot.captured.name)
    }

    @Test
    fun `should delete and calculate when delete module`() {
        val systemId: Long = 1
        service.deleteLogicModule(systemId, "id")

        verify(exactly = 1) { logicModuleRepository.delete("id") }
        verify(exactly = 1) { couplingService.persistAllClassCouplingResults(systemId) }
    }
}
