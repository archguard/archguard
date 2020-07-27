package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DubboPluginTest {
    @MockK
    lateinit var dubboXmlDependencyAnalysisHelper: DubboXmlDependencyAnalysisHelper

    @MockK
    lateinit var jClassRepository: JClassRepository

    @InjectMockKs
    private var dubboPlugin = DubboPlugin()

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    internal fun should_return_intersect_result_with_analysis() {
        // given
        val class1 = JClassVO("class", "submodule1")
        val class2 = JClassVO("class", "sa")

        val dependency1 = Dependency(JMethodVO(class1, "any"), JMethodVO(class2,"any"))
        val dependencies = listOf(dependency1)

        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("submodule1")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("submodule2"), LogicComponent.createLeaf("sa.class")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("submodule3"), LogicComponent.createLeaf("sa.class")))
        val logicModules = listOf(logicModule1, logicModule2, logicModule3)

        val jClass = JClass("any", class2.name, class2.module)
        jClass.classType = ClazzType.INTERFACE

        every { jClassRepository.getJClassesHasModules() } returns listOf(jClass)
        every { dubboXmlDependencyAnalysisHelper.analysis(any(), any()) }  returns listOf(logicModule3)

        // when
        val dependenciesAfterAnalysis = dubboPlugin.mapToModuleDependencies(dependencies, logicModules, listOf())

        // then
        assertEquals(1, dependenciesAfterAnalysis.size)
        Assertions.assertThat(dependenciesAfterAnalysis).usingDefaultElementComparator().containsExactlyElementsOf(listOf(Dependency(logicModule1, logicModule3)))

    }



}
