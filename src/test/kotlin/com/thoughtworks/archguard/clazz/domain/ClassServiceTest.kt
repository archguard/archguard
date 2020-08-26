package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.clazz.domain.service.*
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassServiceTest {

    @InjectMockKs
    var service = ClassService()

    @MockK
    private lateinit var classInvokeService: ClassInvokeService

    @MockK
    lateinit var classDependenceesService: ClassDependenceesService

    @MockK
    lateinit var classDependencerService: ClassDependencerService

    @MockK
    lateinit var classMethodCalleesService: ClassMethodCalleesService

    @MockK
    lateinit var repo: JClassRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    fun `should get class dependencies`() {
        //given
        val projectId: Long = 1
        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val dependencee = JClass("id1", "com.thoughtworks.archguard.domain.dependencee", "archguard")
        val dependencer = JClass("id2", "com.thoughtworks.archguard.domain.dependencer", "archguard")
        val expected = JClass("1", targetName, "module")
        (expected.dependencees as MutableList).add(dependencee)
        (expected.dependencers as MutableList).add(dependencer)
        //when
        every { repo.getJClassBy(any(), any(), any()) } returns expected
        every { classDependenceesService.findDependencees(any(), any()) } returns expected
        every { classDependencerService.findDependencers(any(), any()) } returns expected

        val result = service.getDependencies(projectId, "module", targetName, 1)
        //then
        assertThat(result.dependencers.size).isEqualTo(1)
        assertThat(result.dependencees.size).isEqualTo(1)
        assertThat(result.dependencers[0]).isEqualToComparingFieldByField(dependencer)
        assertThat(result.dependencees[0]).isEqualToComparingFieldByField(dependencee)
    }

    @Test
    fun `should get class invokes`() {
        //given
        val projectId = 1L
        val targetName = "clazz"
        val module = "module"
        val target = JClass("1", targetName, module)
        val deep = 3
        val needIncludeImpl = true
        //when
        every { repo.getJClassBy(projectId, targetName, module) } returns target
        every {
            classInvokeService.findInvokes(projectId, target, deep, deep, needIncludeImpl)
        } returns target
        val invokes = service.findInvokes(projectId, module, targetName, deep, deep, needIncludeImpl)
        //then
        assertThat(invokes).isEqualToComparingFieldByField(target)
    }

    @Test
    fun `should get class method callees`() {
        //given
        val projectId: Long = 1
        val needIncludeImpl = true
        val needParents = true
        val module = "module"
        val name = "clazz"
        val deep = 2
        val targetClass = JClass("id", name, module)
        //when
        every {
            classMethodCalleesService.findClassMethodsCallees(projectId, targetClass, deep, needIncludeImpl, needParents)
        } returns JClass("id", "clazz", "module")
        every { repo.getJClassBy(projectId, name, module) } returns targetClass
        val target = service.findMethodsCallees(projectId, module, name, deep, needIncludeImpl, needParents)
        //then
        assertThat(target.methods).isEmpty()
    }
}
