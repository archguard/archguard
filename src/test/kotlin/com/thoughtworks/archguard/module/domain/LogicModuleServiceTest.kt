package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.metrics.domain.MetricsService

import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import io.mockk.MockKAnnotations.init
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
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
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var metricsService: MetricsService

    @InjectMockKs
    var service: LogicModuleService = LogicModuleService()

    @BeforeEach
    fun setUp() {
        init(this, relaxUnitFun = true)
    }

    @Test
    fun `should get logic modules`() {
        // given
        val logicModule1 = LogicModule("1", "m1", listOf(LogicComponent.createLeaf("bm1"), LogicComponent.createLeaf("bm2")))
        val logicModule2 = LogicModule("2", "m2", listOf(LogicComponent.createLeaf("bm3"), LogicComponent.createLeaf("bm4")))
        val logicModules = listOf(logicModule1, logicModule2)
        every { logicModuleRepository.getAll() } returns logicModules

        // when
        val actual = service.getLogicModules()

        // then
        assertThat(actual.size).isEqualTo(logicModules.size)
    }

    @Test
    fun `should get class module by single full match`() {
        val lg1 = LogicModule("id1", "lg1", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2)
        val classModule = getModule(logicModules, LogicComponent.createLeaf("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2))
    }

    @Test
    fun `should get class module by multi full match`() {
        val lg1 = LogicModule("id1", "lg1", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c")))
        val lg3 = LogicModule("id3", "lg3", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c")))
        val lg4 = LogicModule("id4", "lg4", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b.c"), LogicComponent.createLeaf("a.b.c.d")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LogicComponent.createLeaf("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2, lg3, lg4))
    }

    @Test
    fun `should get class module by single start with match`() {
        val lg1 = LogicModule("id1", "lg1", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d.f"), LogicComponent.createLeaf("abc.d.e.d")))
        val lg4 = LogicModule("id4", "lg4", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d"), LogicComponent.createLeaf("abc.d.e")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LogicComponent.createLeaf("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3))
    }

    @Test
    fun `should get class module by multi start with match`() {
        val lg1 = LogicModule("id1", "lg1", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d.f"), LogicComponent.createLeaf("abc.d.e.d")))
        val lg4 = LogicModule("id4", "lg4", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d"), LogicComponent.createLeaf("abc.d.e")))
        val lg5 = LogicModule("id5", "lg5", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d.f"), LogicComponent.createLeaf("abc.d.e")))
        val lg6 = LogicModule("id6", "lg6", listOf(LogicComponent.createLeaf("a"), LogicComponent.createLeaf("a.b"), LogicComponent.createLeaf("abc.d.e.d.f.g.h"), LogicComponent.createLeaf("abc.d.e")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4, lg5, lg6)
        val classModule = getModule(logicModules, LogicComponent.createLeaf("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3, lg5))
    }

    @Test
    fun `should update and calculate when update module`() {
        val logicModule = LogicModule.createWithOnlyLeafMembers("1", "m1", listOf(LogicComponent.createLeaf("bm1"), LogicComponent.createLeaf("bm2")))

        val slot = slot<LogicModule>()
        every { logicModuleRepository.update(capture(slot()), capture(slot)) } just Runs

        service.updateLogicModule("id", logicModule)

        verify(exactly = 1) { metricsService.calculateCouplingLegacy() }
        assertEquals(logicModule.name, slot.captured.name)
    }

    @Test
    fun `should delete and calculate when delete module`() {
        service.deleteLogicModule("id")

        verify(exactly = 1) { logicModuleRepository.delete("id") }
        verify(exactly = 1) { metricsService.calculateCouplingLegacy() }
    }
}
