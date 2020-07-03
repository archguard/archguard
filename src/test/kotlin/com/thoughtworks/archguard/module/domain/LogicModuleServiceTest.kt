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

    @MockK
    lateinit var jClassRepository: JClassRepository

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
        every { jClassRepository.getJClassById("id2") } returns JClass("id2", "Service", "module2")
        every { jClassRepository.getJClassById("id3") } returns JClass("id3", "ParentClass", "module3")
        val logicModule = service.getIncompleteLogicModuleForJClass(jClass)
        assertThat(logicModule.name).isEqualTo("module1")
        assertThat(logicModule.members.toSet()).isEqualTo(setOf(SubModule("module1"), JClass("id2", "Service", "module2"), JClass("id3", "ParentClass", "module3")))
    }

    @Test
    fun `should get logic module with interface members for all JClasses`() {
        val jClass1 = JClass("id1", "Service1Impl", "module1")
        val jClass2 = JClass("id2", "Controller", "module2")
        val jClass3 = JClass("id3", "Service2Impl", "module1")
        val jClasses = listOf(jClass1, jClass2, jClass3)
        service = spyk(service)
        every { service.getIncompleteLogicModuleForJClass(jClass1) } returns NewLogicModule(null, "module1", listOf(SubModule("module1"), JClass("Service1", "module-api")))
        every { service.getIncompleteLogicModuleForJClass(jClass2) } returns NewLogicModule(null, "module2", listOf(SubModule("module2")))
        every { service.getIncompleteLogicModuleForJClass(jClass3) } returns NewLogicModule(null, "module1", listOf(SubModule("module1"), JClass("Service2", "module-api")))

        val defineLogicModuleWithInterface = service.getLogicModulesForAllJClass(jClasses)
        assertThat(defineLogicModuleWithInterface.size).isEqualTo(2)
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module1" }[0].members.toSet()).isEqualTo(setOf(SubModule("module1"), JClass("Service1", "module-api"), JClass("Service2", "module-api")))
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module2" }[0].members.toSet()).isEqualTo(setOf(SubModule("module2")))

    }

    @Test
    fun `should get class module by single full match`() {
        val lg1 = NewLogicModule("id1", "lg1", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c.d")))
        val lg2 = NewLogicModule("id2", "lg2", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c")))
        val logicModules: List<NewLogicModule> = listOf(lg1, lg2)
        val classModule = getNewModule(logicModules, createModuleMember("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2))
    }

    @Test
    fun `should get class module by multi full match`() {
        val lg1 = NewLogicModule("id1", "lg1", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c.d")))
        val lg2 = NewLogicModule("id2", "lg2", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c")))
        val lg3 = NewLogicModule("id3", "lg3", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c")))
        val lg4 = NewLogicModule("id4", "lg4", listOf(createModuleMember("a"), createModuleMember("a.b.c"), createModuleMember("a.b.c.d")))
        val logicModules: List<NewLogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getNewModule(logicModules, createModuleMember("a.b.c"))
        assertThat(classModule).isEqualTo(listOf(lg2, lg3, lg4))
    }

    @Test
    fun `should get class module by single start with match`() {
        val lg1 = NewLogicModule("id1", "lg1", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c.d")))
        val lg2 = NewLogicModule("id2", "lg2", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc")))
        val lg3 = NewLogicModule("id3", "lg3", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d.f"), createModuleMember("abc.d.e.d")))
        val lg4 = NewLogicModule("id4", "lg4", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d"), createModuleMember("abc.d.e")))
        val logicModules: List<NewLogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getNewModule(logicModules, createModuleMember("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3))
    }

    @Test
    fun `should get class module by multi start with match`() {
        val lg1 = NewLogicModule("id1", "lg1", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("a.b.c.d")))
        val lg2 = NewLogicModule("id2", "lg2", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc")))
        val lg3 = NewLogicModule("id3", "lg3", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d.f"), createModuleMember("abc.d.e.d")))
        val lg4 = NewLogicModule("id4", "lg4", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d"), createModuleMember("abc.d.e")))
        val lg5 = NewLogicModule("id5", "lg5", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d.f"), createModuleMember("abc.d.e")))
        val lg6 = NewLogicModule("id6", "lg6", listOf(createModuleMember("a"), createModuleMember("a.b"), createModuleMember("abc.d.e.d.f.g.h"), createModuleMember("abc.d.e")))
        val logicModules: List<NewLogicModule> = listOf(lg1, lg2, lg3, lg4, lg5, lg6)
        val classModule = getNewModule(logicModules, createModuleMember("abc.d.e.d.f.g"))
        assertThat(classModule).isEqualTo(listOf(lg3, lg5))
    }
}
