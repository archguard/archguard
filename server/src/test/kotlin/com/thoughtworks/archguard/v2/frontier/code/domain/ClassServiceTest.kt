package com.thoughtworks.archguard.v2.frontier.code.domain

import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClassRepository
import com.thoughtworks.archguard.v2.frontier.clazz.domain.service.*
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassServiceTest {

    private lateinit var service: ClassService

    @MockK
    private lateinit var classInvokeService: ClassInvokeService

    @MockK
    lateinit var classDependenciesService: ClassDependenciesService

    @MockK
    lateinit var classDependencerService: ClassDependencerService

    @MockK
    lateinit var classMethodCalleesService: ClassMethodCalleesService

    @MockK
    lateinit var jClassRepository: JClassRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = ClassService(
            classMethodCalleesService,
            classDependenciesService,
            classDependencerService,
            jClassRepository,
            classInvokeService
        )
    }

    @Test
    fun `should get class dependencies`() {
        // given
        val systemId: Long = 1
        val targetName = "clazz"
        val dependencee = JClass("id1", "com.thoughtworks.archguard.domain.dependencee", "archguard")
        val dependencer = JClass("id2", "com.thoughtworks.archguard.domain.dependencer", "archguard")
        val expected = JClass("1", targetName, "module")
        (expected.dependencies as MutableList).add(dependencee)
        (expected.dependencers as MutableList).add(dependencer)
        // when
        every { jClassRepository.getJClassBy(any(), any(), any()) } returns expected
        every { classDependenciesService.findDependencies(systemId, any(), any()) } returns expected
        every { classDependencerService.findDependencers(systemId, any(), any()) } returns expected

        val result = service.getDependencies(systemId, "module", targetName, 1)
        // then
        assertThat(result.dependencers.size).isEqualTo(1)
        assertThat(result.dependencies.size).isEqualTo(1)
        assertThat(result.dependencers[0]).isEqualToComparingFieldByField(dependencer)
        assertThat(result.dependencies[0]).isEqualToComparingFieldByField(dependencee)
    }

    @Test
    fun `should get class invokes`() {
        // given
        val systemId = 1L
        val targetName = "clazz"
        val module = "module"
        val target = JClass("1", targetName, module)
        val deep = 3
        val needIncludeImpl = true
        // when
        every { jClassRepository.getJClassBy(systemId, targetName, module) } returns target
        every {
            classInvokeService.findInvokes(systemId, target, deep, deep, needIncludeImpl)
        } returns target
        val invokes = service.findInvokes(systemId, module, targetName, deep, deep, needIncludeImpl)
        // then
        assertThat(invokes).isEqualToComparingFieldByField(target)
    }

    @Test
    fun `should get class method callees`() {
        // given
        val systemId: Long = 1
        val needIncludeImpl = true
        val needParents = true
        val module = "module"
        val name = "clazz"
        val deep = 2
        val targetClass = JClass("id", name, module)
        // when
        every {
            classMethodCalleesService.findClassMethodsCallees(systemId, targetClass, deep, needIncludeImpl, needParents)
        } returns JClass("id", "clazz", "module")
        every { jClassRepository.getJClassBy(systemId, name, module) } returns targetClass
        val target = service.findMethodsCallees(systemId, module, name, deep, needIncludeImpl, needParents)
        // then
        assertThat(target.methods).isEmpty()
    }
}
