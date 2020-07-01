package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogicModuleServiceTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var baseModuleRepository: BaseModuleRepository

    @InjectMockKs
    var service: LogicModuleService = LogicModuleService()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    fun `should get logic modules`() {
        // given
        val logicModule1 = LogicModule("1", "m1", listOf("bm1", "bm2"))
        val logicModule2 = LogicModule("2", "m2", listOf("bm3", "bm4"))
        val logicModules = listOf(logicModule1, logicModule2)
        every { logicModuleRepository.getAll() } returns logicModules

        // when
        val actual = service.getLogicModules()

        // then
        assertThat(actual.size).isEqualTo(logicModules.size)
    }

    @Test
    fun `should get logic module with interface members  for one JClass`() {
        val jClass = JClass("id1", "ServiceImpl", "module1")
        every { logicModuleRepository.getParentClassId(any()) } returns listOf("id2", "id3")
        every { baseModuleRepository.getJClassesById("id2") } returns JClass("id2", "Service", "module2")
        every { baseModuleRepository.getJClassesById("id3") } returns JClass("id3", "ParentClass", "module3")
        val logicModule = service.getIncompleteLogicModuleForJClass(jClass)
        assertThat(logicModule.name).isEqualTo("module1")
        assertThat(logicModule.members.toSet()).isEqualTo(setOf("module1", "module2.Service", "module3.ParentClass"))
    }

    @Test
    fun `should get logic module with interface members for all JClasses`() {
        val jClass1 = JClass("id1", "Service1Impl", "module1")
        val jClass2 = JClass("id2", "Controller", "module2")
        val jClass3 = JClass("id3", "Service2Impl", "module1")
        val jClasses = listOf(jClass1, jClass2, jClass3)
        service = spyk(service)
        every { service.getIncompleteLogicModuleForJClass(jClass1) } returns LogicModule(null, "module1", listOf("module1", "module3", "module4"))
        every { service.getIncompleteLogicModuleForJClass(jClass2) } returns LogicModule(null, "module2", listOf("module2"))
        every { service.getIncompleteLogicModuleForJClass(jClass3) } returns LogicModule(null, "module1", listOf("module1", "module3", "module5"))

        val defineLogicModuleWithInterface = service.getLogicModulesForAllJClass(jClasses)
        assertThat(defineLogicModuleWithInterface.size).isEqualTo(2)
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module1" }[0].members.toSet()).isEqualTo(setOf("module1", "module3", "module4", "module5"))
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module2" }[0].members.toSet()).isEqualTo(setOf("module2"))

    }

    @Test
    fun `should get class module by single full match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "a.b.c")))
        val classModule = getModule(logicModules, "a.b.c")
        assertThat(classModule).isEqualTo(listOf("lg2"))
    }

    @Test
    fun `should get class module by multi full match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "a.b.c")),
                LogicModule("id3", "lg3", listOf("a", "a.b", "a.b.c")),
                LogicModule("id4", "lg4", listOf("a", "a.b.c", "a.b.c.d")))
        val classModule = getModule(logicModules, "a.b.c")
        assertThat(classModule).isEqualTo(listOf("lg2", "lg3", "lg4"))
    }

    @Test
    fun `should get class module by single start with match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "abc")),
                LogicModule("id3", "lg3", listOf("a", "a.b", "abc.d.e.d.f", "abc.d.e.d")),
                LogicModule("id4", "lg4", listOf("a", "a.b", "abc.d.e.d", "abc.d.e")))
        val classModule = getModule(logicModules, "abc.d.e.d.f.g")
        assertThat(classModule).isEqualTo(listOf("lg3"))
    }

    @Test
    fun `should get class module by multi start with match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "abc")),
                LogicModule("id3", "lg3", listOf("a", "a.b", "abc.d.e.d.f", "abc.d.e.d")),
                LogicModule("id4", "lg4", listOf("a", "a.b", "abc.d.e.d", "abc.d.e")),
                LogicModule("id5", "lg5", listOf("a", "a.b", "abc.d.e.d.f", "abc.d.e")),
                LogicModule("id6", "lg6", listOf("a", "a.b", "abc.d.e.d.f.g.h", "abc.d.e")))
        val classModule = getModule(logicModules, "abc.d.e.d.f.g")
        assertThat(classModule).isEqualTo(listOf("lg3", "lg5"))
    }
}
