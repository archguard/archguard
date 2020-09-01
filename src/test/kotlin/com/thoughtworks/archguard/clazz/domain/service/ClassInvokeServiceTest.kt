package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.ClassRelation
import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassInvokeServiceTest {
    private lateinit var service: ClassInvokeService

    @MockK
    private lateinit var repo: JClassRepository

    @MockK
    private lateinit var configService: ConfigureService

    @MockK
    private lateinit var classConfigService: ClassConfigService

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = ClassInvokeService(repo, configService, classConfigService)
    }

    @Test
    fun `should get invoke methods`() {
        //given
        val systemId = 1L
        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val parent = JClass("parent", "parent", "module")
        val impl = JClass("impl", "impl", "module")
        val callee = JClass("2", "callee", "module")
        val caller = JClass("3", "caller", "module")
        //when
        every { repo.findClassParents(systemId, target.module, target.name) } returns listOf(parent)
        every { configService.isDisplayNode(any(), any()) } returns true
        every { repo.findClassImplements(systemId, target.name, target.module) } returns listOf(impl)
        every { repo.findCallees(systemId, target.name, target.module) } returns listOf(ClassRelation(callee, 1))
        every { repo.findCallers(systemId, target.name, target.module) } returns listOf(ClassRelation(caller, 1))
        every { classConfigService.buildJClassColorConfig(any(), any()) } returns Unit
        every { classConfigService.buildClassRelationColorConfig(any(), any()) } returns Unit
        service.findInvokes(systemId, target, 1, 1, true)

        //then
        Assertions.assertThat(target.parents.size).isEqualTo(1)
        Assertions.assertThat(target.implements.size).isEqualTo(1)
        Assertions.assertThat(target.callers.size).isEqualTo(1)
        Assertions.assertThat(target.callees.size).isEqualTo(1)
    }
}
