package com.thoughtworks.archguard.module.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReportServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @InjectMockKs
    var service: ReportServiceImpl = ReportServiceImpl()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `group class coupling reports by module name`() {
        val classCouplingReport1 = ClassCouplingReport("com.thoughtworks.archguard.test1.class1", 0, 0, 0, 0)
        val classCouplingReport2 = ClassCouplingReport("com.thoughtworks.archguard.test1.class2", 1, 1, 0, 0)
        val classCouplingReport3 = ClassCouplingReport("com.thoughtworks.archguard.test2.class3", 1, 1, 0, 0)
        val classCouplingReports: List<ClassCouplingReport> = listOf(classCouplingReport1, classCouplingReport2, classCouplingReport3)
        val modules: List<LogicModuleLegacy> = listOf()
        service = spyk(service)
        mockkStatic("com.thoughtworks.archguard.module.domain.LogicModuleServiceKt")
        every { getModuleLegacy(modules, "class1") } returns listOf("module1", "module2")
        every { getModuleLegacy(modules, "class2") } returns listOf("module2")
        every { getModuleLegacy(modules, "class3") } returns listOf("module1", "module3")
        val packageReport = service.groupToPackage(classCouplingReports)
        assertThat(packageReport.size).isEqualTo(2)
        assertThat(packageReport.filter { it.packageName == "com.thoughtworks.archguard.test1" }.get(0).classCouplingReports)
                .containsAll(mutableListOf(classCouplingReport1, classCouplingReport2))
        assertThat(packageReport.filter { it.packageName == "com.thoughtworks.archguard.test2" }.get(0).classCouplingReports)
                .containsAll(mutableListOf(classCouplingReport3))
    }

    @Test
    fun `should get module coupling`() {
        //given
        val element = LogicModule(null, "module1", listOf(createModuleMember("com.test1"), createModuleMember("com.test2")))
        val element2 = LogicModule(null, "module2", listOf(createModuleMember("com.test3"), createModuleMember("com.test4")))
        val element3 = LogicModule(null, "module3", listOf(createModuleMember("com.test5"), createModuleMember("com.test6")))
        val dependency1 = Dependency(createJClassFromFullName("com.test1.clazz"), createJClassFromFullName("com.test3.clazz"))
        val dependency2 = Dependency(createJClassFromFullName("com.test4.clazz"), createJClassFromFullName("com.test2.clazz"))
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
        val element = LogicModule(null, "module1", listOf(createModuleMember("com.test1"), createModuleMember("com.test2")))
        val element2 = LogicModule(null, "module2", listOf(createModuleMember("com.test3"), createModuleMember("com.test4")))
        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element, element2)
        every { logicModuleRepository.getAllClassDependency(any()) } returns listOf()
        //when
        val logicModuleCoupling = service.getLogicModuleCouplingReportDetail()
        //then
        assertThat(logicModuleCoupling.size).isEqualTo(0)
    }
}