package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.model.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReportServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var dependencyService: DependencyService

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
        service = spyk(service)
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
        val element = LogicModule("id1", "module1", listOf(LogicComponent.createLeaf("com.test1"), LogicComponent.createLeaf("com.test2")))
        val element2 = LogicModule("id2", "module2", listOf(LogicComponent.createLeaf("com.test3"), LogicComponent.createLeaf("com.test4")))
        val element3 = LogicModule("id3", "module3", listOf(LogicComponent.createLeaf("com.test5"), LogicComponent.createLeaf("com.test6")))
        val dependency1 = Dependency(JMethodVO("any",  "test1.clazz", "com", "any", listOf()), JMethodVO("any", "test3.clazz", "com", "any", listOf()))
        val dependency2 = Dependency(JMethodVO("any", "test4.clazz", "com", "any", listOf()), JMethodVO("any",  "test2.clazz", "com", "any", listOf()))
        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element, element2, element3)
        every { dependencyService.getAllWithFullNameStart(any(), any()) } returns listOf(dependency1, dependency2)
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
        val element = LogicModule("id1", "module1", listOf(LogicComponent.createLeaf("com.test1"), LogicComponent.createLeaf("com.test2")))
        val element2 = LogicModule("id2", "module2", listOf(LogicComponent.createLeaf("com.test3"), LogicComponent.createLeaf("com.test4")))
        every { logicModuleRepository.getAllByShowStatus(true) } returns listOf(element, element2)
        every { dependencyService.getAllWithFullNameStart(any(), any()) } returns listOf()
        //when
        val logicModuleCoupling = service.getLogicModuleCouplingReportDetail()
        //then
        assertThat(logicModuleCoupling.size).isEqualTo(0)
    }
}
