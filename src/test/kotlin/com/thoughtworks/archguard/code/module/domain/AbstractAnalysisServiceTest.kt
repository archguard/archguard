package com.thoughtworks.archguard.code.module.domain

import com.thoughtworks.archguard.code.clazz.domain.ClazzType
import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.metrics.domain.abstracts.AbstractAnalysisService
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import com.thoughtworks.archguard.code.module.domain.model.PackageVO
import com.thoughtworks.archguard.code.module.domain.model.SubModule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AbstractAnalysisServiceTest {
    @MockK
    lateinit var jClassRepository: JClassRepository

    @MockK
    lateinit var logicModuleRepository: LogicModuleRepository

    private lateinit var abstractAnalysisService: AbstractAnalysisService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        abstractAnalysisService = AbstractAnalysisService(jClassRepository, logicModuleRepository)
    }

    @Test
    internal fun should_calculate_package_abstract_ratio() {
        val systemId: Long = 1
        val jClass1 = JClass("1", "pk1.name1", "module1")
        jClass1.addClassType(ClazzType.ABSTRACT_CLASS)
        val jClass2 = JClass("2", "pk1.name2", "module1")
        jClass2.addClassType(ClazzType.INTERFACE)
        val jClass3 = JClass("3", "pk1.name3", "module1")
        every { jClassRepository.getAllBySystemId(systemId) } returns listOf(jClass1, jClass2, jClass3)
        val abstractRatio = abstractAnalysisService.calculatePackageAbstractRatio(systemId, PackageVO("pk1", "module1"))
        assertThat(abstractRatio.ratio).isEqualTo(2.0 / 3)
    }

    @Test
    internal fun should_calculate_module_abstract_ratio() {
        val systemId: Long = 1
        val jClass1 = JClass("1", "pk1.name1", "module1")
        jClass1.addClassType(ClazzType.ABSTRACT_CLASS)
        val jClass2 = JClass("2", "pk1.name2", "module1")
        jClass2.addClassType(ClazzType.INTERFACE)
        val jClass3 = JClass("3", "pk1.name3", "module1")
        val lg1 = LogicModule.createWithOnlyLeafMembers("id1", "lg1", listOf(SubModule("module1")))
        every { logicModuleRepository.getAllBySystemId(systemId) } returns listOf(lg1)
        every { jClassRepository.getAllBySystemId(systemId) } returns listOf(jClass1, jClass2, jClass3)
        val abstractRatio = abstractAnalysisService.calculateModuleAbstractRatio(systemId, lg1)
        assertThat(abstractRatio.ratio).isEqualTo(2.0 / 3)
    }
}
