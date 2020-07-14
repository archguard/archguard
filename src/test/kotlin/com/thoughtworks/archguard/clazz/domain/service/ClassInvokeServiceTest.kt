package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.domain.PropsDependency
import com.thoughtworks.archguard.module.domain.model.JClass
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
        val callee = JClass("2","callee", "module")
        val caller = JClass("3","caller", "module")
        //when
        val parentDependence = PropsDependency(parent.name, targetName, 1, mapOf(Pair("parent", true)))
        val implDependence = PropsDependency(parent.name, targetName, 1, mapOf(Pair("implements", true)))
        val callerDependence = PropsDependency(parent.name, targetName, 1, mapOf())
        val calleeDependence = PropsDependency(parent.name, targetName, 1, mapOf())
        every { repo.findClassParents(target.name, target.module) } returns listOf(parentDependence)
        every { repo.findClassImplements(target.name, target.module) } returns listOf(implDependence)
        every { repo.findCallees(target.name, target.module) } returns listOf(calleeDependence)
        every { repo.findCallers(target.name, target.module) } returns listOf(callerDependence)
        every { repo.getJClassByName(target.name) } returns listOf(target)
        every { repo.getJClassByName(parent.name) } returns listOf(parent)
        every { repo.getJClassByName(impl.name) } returns listOf(impl)
        every { repo.getJClassByName(callee.name) } returns listOf(callee)
        every { repo.getJClassByName(caller.name) } returns listOf(caller)
        service.findInvokes(target, 1, 1, true)
        //then
        Assertions.assertThat(target.parents.size).isEqualTo(1)
        Assertions.assertThat(target.implements.size).isEqualTo(1)
        Assertions.assertThat(target.callers.size).isEqualTo(1)
        Assertions.assertThat(target.callees.size).isEqualTo(1)
    }
}