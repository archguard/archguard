package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.ClassRelation
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.clazz.domain.JClass
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassInvokeServiceTest {
    @InjectMockKs
    var service = ClassInvokeService()
    @MockK
    private lateinit var repo: JClassRepository
    @MockK
    private lateinit var configService: ConfigureService

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    fun `should get invoke methods`() {
        //given
        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val parent = JClass("parent", "parent", "module")
        val impl = JClass("impl", "impl", "module")
        val callee = JClass("2", "callee", "module")
        val caller = JClass("3", "caller", "module")
        //when
        every { repo.findClassParents(target.module, target.name) } returns listOf(parent)
        every { configService.isDisplayClass(any()) } returns true
        every { repo.findClassImplements(target.name, target.module) } returns listOf(impl)
        every { repo.findCallees(target.name, target.module) } returns listOf(ClassRelation(callee, 1))
        every { repo.findCallers(target.name, target.module) } returns listOf(ClassRelation(caller, 1))
        every { repo.getJClassByName(target.name) } returns listOf(target)
        service.findInvokes(target, 1, 1, true)
        //then
        Assertions.assertThat(target.parents.size).isEqualTo(1)
        Assertions.assertThat(target.implements.size).isEqualTo(1)
        Assertions.assertThat(target.callers.size).isEqualTo(1)
        Assertions.assertThat(target.callees.size).isEqualTo(1)
    }
}
