package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
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
    fun `should get module coupling`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        val element3 = LogicModule(null, "module3", listOf("com.test5", "com.test6"))
        val dependency1 = Dependency("com.test1.clazz", "com.test3.clazz")
        val dependency2 = Dependency("com.test4.clazz", "com.test2.clazz")
        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element, element2, element3)
        every { logicModuleRepository.getAllClassDependency(any()) } returns listOf(dependency1, dependency2)
        //when
        val logicModuleCoupling = service.getLogicModuleCouplingReportDetail()
        //then
        assertThat(logicModuleCoupling.filter { it.moduleName == "module1" }[0].outerModuleCouplingAverage).isEqualTo(0.0)
        assertThat(logicModuleCoupling.filter { it.moduleName == "module1" }[0].outerModuleInstabilityAverage).isEqualTo(0.5)
        assertThat(logicModuleCoupling.filter { it.moduleName == "module2" }[0].outerModuleCouplingAverage).isEqualTo(0.0)
        assertThat(logicModuleCoupling.filter { it.moduleName == "module2" }[0].outerModuleInstabilityAverage).isEqualTo(0.5)
    }

    @Test
    fun `should be zero when no dependence`() {
        //given
        val element = LogicModule(null, "module1", listOf("com.test1", "com.test2"))
        val element2 = LogicModule(null, "module2", listOf("com.test3", "com.test4"))
        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element, element2)
        every { logicModuleRepository.getAllClassDependency(any()) } returns listOf()
        //when
        val logicModuleCoupling = service.getLogicModuleCouplingReportDetail()
        //then
        assertThat(logicModuleCoupling.size).isEqualTo(0)
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

    @Test
    fun `should map to module`() {
        val results = listOf(Dependency("caller.method1", "callee.method1"),
                Dependency("caller.method2", "callee.method2"))
        val modules = listOf(LogicModule("id1", "module1", listOf("caller.method1")),
                LogicModule("id2", "module2", listOf("callee.method1")),
                LogicModule("id3", "module3", listOf("callee.method1")),
                LogicModule("id4", "module4", listOf("caller.method2", "callee.method2")),
                LogicModule("id5", "module5", listOf("caller.method1")))
        val moduleDependency = mapClassDependencyToModuleDependency(results, modules)
        assertThat(moduleDependency.size).isEqualTo(4)
        assertThat(moduleDependency).containsAll(listOf(Dependency("module1", "module2"), Dependency("module1", "module3"),
                Dependency("module5", "module2"), Dependency("module5", "module3")))
    }

    @Test
    fun `group class coupling reports by module name`() {
        val classCouplingReport1 = ClassCouplingReport("com.thoughtworks.archguard.test1.class1", 0, 0, 0, 0)
        val classCouplingReport2 = ClassCouplingReport("com.thoughtworks.archguard.test1.class2", 1, 1, 0, 0)
        val classCouplingReport3 = ClassCouplingReport("com.thoughtworks.archguard.test2.class3", 1, 1, 0, 0)
        val classCouplingReports: List<ClassCouplingReport> = listOf(classCouplingReport1, classCouplingReport2, classCouplingReport3)
        val modules: List<LogicModule> = listOf()
        service = spyk(service)
        mockkStatic("com.thoughtworks.archguard.module.domain.LogicModuleServiceKt")
        every { getModule(modules, "class1") } returns listOf("module1", "module2")
        every { getModule(modules, "class2") } returns listOf("module2")
        every { getModule(modules, "class3") } returns listOf("module1", "module3")
        val packageReport = service.groupToPackage(classCouplingReports)
        assertThat(packageReport.size).isEqualTo(2)
        assertThat(packageReport.filter { it.packageName == "com.thoughtworks.archguard.test1" }?.get(0).classCouplingReports)
                .containsAll(mutableListOf(classCouplingReport1, classCouplingReport2))
        assertThat(packageReport.filter { it.packageName == "com.thoughtworks.archguard.test2" }?.get(0).classCouplingReports)
                .containsAll(mutableListOf(classCouplingReport3))
    }
}
