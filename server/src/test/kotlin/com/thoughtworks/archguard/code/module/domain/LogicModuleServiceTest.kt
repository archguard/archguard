package com.thoughtworks.archguard.code.module.domain

import com.thoughtworks.archguard.code.module.domain.model.LeafManger
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
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
    fun `should get class module by single full match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2)
        val classModule = getModule(logicModules, LeafManger.createLeaf("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2))
    }

    @Test
    fun `should get class module by multi full match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val lg4 = LogicModule("id4", "lg4",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b.c"), LeafManger.createLeaf("a.b.c.d")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LeafManger.createLeaf("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2, lg3, lg4))
    }

    @Test
    fun `should get class module by single start with match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e.d")
            ))
        val lg4 = LogicModule("id4", "lg4",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LeafManger.createLeaf("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3))
    }

    @Test
    fun `should get class module by multi start with match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e.d")
            ))
        val lg4 = LogicModule("id4", "lg4",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val lg5 = LogicModule("id5", "lg5",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val lg6 = LogicModule("id6", "lg6",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f.g.h"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4, lg5, lg6)
        val classModule = getModule(logicModules, LeafManger.createLeaf("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3, lg5))
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
