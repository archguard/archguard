package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DubboGraphServiceImplTest {
    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    @MockK
    lateinit var dependencyService: DependencyService

    @MockK
    lateinit var dubboXmlDependencyAnalysisHelper: DependencyAnalysisHelper

    private lateinit var dubboGraphService: DubboGraphServiceImpl

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        dubboGraphService = DubboGraphServiceImpl(logicModuleRepository, dependencyService, dubboXmlDependencyAnalysisHelper)
    }

    @Test
    internal fun should_stop_analysis_when_callee_is_not_interface() {
        val lg1 = LogicModule("id1", "lg1", listOf(SubModule("s1"), JClassVO.create("s1.c1")))
        val lg2 = LogicModule("id2", "lg2", listOf(SubModule("s2"), JClassVO.create("sa.c")))
        val lg3 = LogicModule("id3", "lg3", listOf(SubModule("s3"), JClassVO.create("sa.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3)
        val caller = JClassVO( "c1", "s1")
        val callee = JClassVO("c", "sa")
        callee.classType = ClazzType.CLASS
        val jClassDependency: Dependency<JClassVO> = Dependency(caller, callee)
        val moduleDependency = dubboGraphService.mapClassDependencyToModuleDependency(logicModules, jClassDependency)
        assertThat(moduleDependency).usingDefaultElementComparator().containsExactlyElementsOf(listOf(Dependency(lg1, lg2), Dependency(lg1, lg3)))
        verify(exactly = 0) { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) }

    }

    @Test
    internal fun should_return_the_same_result_with_no_analysis_when_analysis_return_empty() {
        val lg1 = LogicModule("id1", "lg1", listOf(SubModule("s1"), JClassVO.create("s1.c1")))
        val lg2 = LogicModule("id2", "lg2", listOf(SubModule("s2"), JClassVO.create("sa.c")))
        val lg3 = LogicModule("id3", "lg3", listOf(SubModule("s3"), JClassVO.create("sa.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3)
        val caller = JClassVO( "c1", "s1")
        val callee = JClassVO( "c", "sa")
        callee.classType = ClazzType.INTERFACE
        val jClassDependency: Dependency<JClassVO> = Dependency(caller, callee)

        every { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) } returns emptyList()

        val moduleDependency = dubboGraphService.mapClassDependencyToModuleDependency(logicModules, jClassDependency)
        assertThat(moduleDependency).usingDefaultElementComparator().containsExactlyElementsOf(listOf(Dependency(lg1, lg2), Dependency(lg1, lg3)))

        verify(exactly = 1) { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) }
    }

    @Test
    internal fun should_return_intersect_result_with_analysis_when_analysis_return_not_empty() {
        val lg1 = LogicModule("id1", "lg1", listOf(SubModule("s1"), JClassVO.create("s1.c1")))
        val lg2 = LogicModule("id2", "lg2", listOf(SubModule("s2"), JClassVO.create("sa.c")))
        val lg3 = LogicModule("id3", "lg3", listOf(SubModule("s3"), JClassVO.create("sa.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3)
        val caller = JClassVO("c1", "s1")
        val callee = JClassVO("c", "sa")
        callee.classType = ClazzType.INTERFACE
        val jClassDependency: Dependency<JClassVO> = Dependency(caller, callee)

        every { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) } returns listOf(lg3)

        val moduleDependency = dubboGraphService.mapClassDependencyToModuleDependency(logicModules, jClassDependency)
        assertThat(moduleDependency).usingDefaultElementComparator().containsExactlyElementsOf(listOf(Dependency(lg1, lg3)))

        verify(exactly = 1) { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) }
    }
}
