package com.thoughtworks.archguard.dependence.domain.logic_module

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.domain.base_module.JClass
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
    fun `should get dependency between logic modules`() {
        // given
        val caller = "module1"
        val callee = "module2"
        val dependency1 = ModuleDependency("module1", "any", "any", "module2", "any", "any")
        val dependency2 = ModuleDependency("module2", "any", "any", "module3", "any", "any")
        val dependencies = listOf(dependency1, dependency2)
        every { logicModuleRepository.getDependence(caller, callee) } returns dependencies

        // when
        val actual = service.getLogicModulesDependencies(caller, callee)

        // then
        assertThat(actual.size).isEqualTo(dependencies.size)
    }

    @Test
    fun `should get graph of all logic modules dependency`() {
        // given
        val logicModule1 = LogicModule("1", "module1", listOf("bm1", "bm2"))
        val logicModule2 = LogicModule("2", "module2", listOf("bm3", "bm4"))
        val logicModule3 = LogicModule("3", "module3", listOf("bm5"))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val dependency1 = ModuleGraphDependency("bm1.any", "bm3.any")
        val dependency2 = ModuleGraphDependency("bm3.any", "bm2.any")
        val dependency3 = ModuleGraphDependency("bm5.any", "bm4.any")
        val dependencies = listOf(dependency1, dependency2, dependency3)

        every { logicModuleRepository.getAll() } returns logicModules
        every { logicModuleRepository.getAllDependence(any()) } returns dependencies

        // when
        val moduleGraph = service.getLogicModuleGraph()

        // then
        assertThat(moduleGraph.nodes.size).isEqualTo(3)
        assertThat(moduleGraph.edges.size).isEqualTo(3)
    }

    @Test
    fun `should get module coupling`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        val element3 = LogicModule(null, "module3", listOf("com.test5", "com.test6"))
        val dependency1 = ModuleGraphDependency("com.test1", "com.test3")
        val dependency2 = ModuleGraphDependency("com.test4", "com.test2")
        every { logicModuleRepository.getAll() } returns listOf(element, element2, element3)
        every { logicModuleRepository.getAllDependence(any()) } returns listOf(dependency1, dependency2)
        //when
        val logicModuleCoupling = service.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].fanIn).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].fanOut).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module2" }[0].fanIn).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module2" }[0].fanOut).isEqualTo(1)
        assertThat(logicModuleCoupling.filter { it.module == "module3" }[0].fanIn).isEqualTo(0)
        assertThat(logicModuleCoupling.filter { it.module == "module3" }[0].fanOut).isEqualTo(0)
    }

    @Test
    fun `should be zero when no dependence`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        every { logicModuleRepository.getAll() } returns listOf(element, element2)
        every { logicModuleRepository.getAllDependence(any()) } returns listOf()
        //when
        val logicModuleCoupling = service.getLogicModuleCoupling()
        //then
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].moduleInstability).isEqualTo(0.0)
        assertThat(logicModuleCoupling.filter { it.module == "module1" }[0].moduleCoupling).isEqualTo(0.0)
    }

    @Test
    fun `should get logic module with interface members  for one JClass`() {
        val jClass = JClass("id1", "ServiceImpl", "module1")
        every { logicModuleRepository.getParentClassId(any()) } returns listOf("id2", "id3")
        every { baseModuleRepository.getJClassesById("id2") } returns JClass("id2", "Service", "module2")
        every { baseModuleRepository.getJClassesById("id3") } returns JClass("id3", "ParentClass", "module3")
        val logicModule = service.getLogicModuleForJClass(jClass)
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
        every { service.getLogicModuleForJClass(jClass1) } returns LogicModule(null, "module1", listOf("module1", "module3", "module4"))
        every { service.getLogicModuleForJClass(jClass2) } returns LogicModule(null, "module2", listOf("module2"))
        every { service.getLogicModuleForJClass(jClass3) } returns LogicModule(null, "module1", listOf("module1", "module3", "module5"))

        val defineLogicModuleWithInterface = service.getLogicModulesForAllJClass(jClasses)
        assertThat(defineLogicModuleWithInterface.size).isEqualTo(2)
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module1" }[0].members.toSet()).isEqualTo(setOf("module1", "module3", "module4", "module5"))
        assertThat(defineLogicModuleWithInterface.filter { it.name == "module2" }[0].members.toSet()).isEqualTo(setOf("module2"))

    }

    @Test
    fun `should get class module by single full match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "a.b.c")))
        val classModule = service.getClassModule(logicModules, "a.b.c")
        assertThat(classModule).isEqualTo(listOf("lg2"))
    }

    @Test
    fun `should get class module by multi full match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "a.b.c")),
                LogicModule("id3", "lg3", listOf("a", "a.b", "a.b.c")),
                LogicModule("id4", "lg4", listOf("a", "a.b.c", "a.b.c.d")))
        val classModule = service.getClassModule(logicModules, "a.b.c")
        assertThat(classModule).isEqualTo(listOf("lg2", "lg3", "lg4"))
    }

    @Test
    fun `should get class module by single start with match`() {
        val logicModules: List<LogicModule> = listOf(LogicModule("id1", "lg1", listOf("a", "a.b", "a.b.c.d")),
                LogicModule("id2", "lg2", listOf("a", "a.b", "abc")),
                LogicModule("id3", "lg3", listOf("a", "a.b", "abc.d.e.d.f", "abc.d.e.d")),
                LogicModule("id4", "lg4", listOf("a", "a.b", "abc.d.e.d", "abc.d.e")))
        val classModule = service.getClassModule(logicModules, "abc.d.e.d.f.g")
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
        val classModule = service.getClassModule(logicModules, "abc.d.e.d.f.g")
        assertThat(classModule).isEqualTo(listOf("lg3", "lg5"))
    }

    @Test
    fun `should map to module`() {
        val results = listOf(ModuleGraphDependency("caller.method1", "callee.method1"),
                ModuleGraphDependency("caller.method2", "callee.method2"))
        val modules = listOf(LogicModule("id1", "module1", listOf("caller.method1")),
                LogicModule("id2", "module2", listOf("callee.method1")),
                LogicModule("id3", "module3", listOf("callee.method1")),
                LogicModule("id4", "module4", listOf("caller.method2", "callee.method2")),
                LogicModule("id5", "module5", listOf("caller.method1")))
        val moduleDependency = service.mapToModule(results, modules)
        assertThat(moduleDependency.size).isEqualTo(4)
        assertThat(moduleDependency).containsAll(listOf(ModuleGraphDependency("module1", "module2"), ModuleGraphDependency("module1", "module3"),
                ModuleGraphDependency("module5", "module2"), ModuleGraphDependency("module5", "module3")))
    }
}
