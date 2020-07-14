package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DubboXmlDependencyAnalysisHelperTest {
    @MockK
    lateinit var xmlConfigService: XmlConfigService

    private lateinit var dubboXmlDependencyAnalysisHelper: DubboXmlDependencyAnalysisHelper

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        dubboXmlDependencyAnalysisHelper = DubboXmlDependencyAnalysisHelper(xmlConfigService)
    }

    @Test
    internal fun should_get_analysis_logic_module_result() {
        every { xmlConfigService.getRealCalleeModuleByXmlConfig(any(), any()) } returns listOf(
                SubModuleDubbo("id1", "name1", "path1"), SubModuleDubbo("id2", "name2", "path2"))
        mockkStatic("com.thoughtworks.archguard.module.domain.LogicModuleServiceKt")
        val lg1 = LogicModule("id1", "lg1", listOf(SubModule("m1")))
        val lg2 = LogicModule("id2", "lg2", listOf(SubModule("m2")))
        val lg3 = LogicModule("id1", "lg1", listOf(SubModule("m1")))
        every { getModule(any(), any()) } returnsMany listOf(listOf(lg1, lg2), listOf(lg3))

        val analysis = dubboXmlDependencyAnalysisHelper.analysis(Dependency(JClass.create("a.b"), JClass.create("a.c")), listOf(lg1, lg2))
        assertThat(analysis.size).isEqualTo(2)
        assertThat(analysis).usingDefaultElementComparator().containsExactlyElementsOf(listOf(lg1, lg2))
        assertThat(analysis).usingDefaultElementComparator().containsExactlyElementsOf(listOf(lg3, lg2))

    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }
}