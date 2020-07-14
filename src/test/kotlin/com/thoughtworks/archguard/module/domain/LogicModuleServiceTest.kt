package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.ClazzType
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
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
    fun `should get logic module with interface members  for one JClass`() {
        val jClass = JClass("id1", "ServiceImpl", "module1")
        every { logicModuleRepository.getParentClassId(any()) } returns listOf("id2", "id3")
        val jClassId2 = JClass("id2", "Service", "module2")
        jClassId2.classType = ClazzType.INTERFACE
        val jClassId3 = JClass("id3", "ParentClass", "module3")
        every { jClassRepository.getJClassById("id2") } returns jClassId2
        every { jClassRepository.getJClassById("id3") } returns jClassId3
        val logicModule = service.getIncompleteLogicModuleForJClass(jClass)
        assertThat(logicModule.name).isEqualTo("module1")
        assertThat(logicModule.members.toSet()).usingFieldByFieldElementComparator().containsAll(setOf(SubModule("module1"), jClassId2))
    }

    @Test
    fun `should get logic module with interface members for all JClasses`() {
        val jClass1 = JClass("id1", "Service1Impl", "module1")
        val jClass2 = JClass("id2", "Controller", "module2")
        val jClass3 = JClass("id3", "Service2Impl", "module1")
        val jClasses = listOf(jClass1, jClass2, jClass3)
        service = spyk(service)
        every { service.getIncompleteLogicModuleForJClass(jClass1) } returns LogicModule("tempid", "module1", listOf(SubModule("module1"), JClass("Service1", "module-api")))
        every { service.getIncompleteLogicModuleForJClass(jClass2) } returns LogicModule("tempid", "module2", listOf(SubModule("module2")))
        every { service.getIncompleteLogicModuleForJClass(jClass3) } returns LogicModule("tempid", "module1", listOf(SubModule("module1"), JClass("Service2", "module-api")))

        val defineLogicModuleWithInterface = service.getLogicModulesForAllJClass(jClasses)
        assertThat(defineLogicModuleWithInterface.size).isEqualTo(2)
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module1" }[0].members.toSet()).usingFieldByFieldElementComparator().containsAll(setOf(SubModule("module1"), JClass("Service1", "module-api"), JClass("Service2", "module-api")))
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module2" }[0].members.toSet()).usingFieldByFieldElementComparator().containsAll(setOf(SubModule("module2")))

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
}
