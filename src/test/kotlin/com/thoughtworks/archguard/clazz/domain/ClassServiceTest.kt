package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.clazz.domain.service.ClassDependenceesService
import com.thoughtworks.archguard.clazz.domain.service.ClassDependencerService
import com.thoughtworks.archguard.clazz.domain.service.ClassInvokeService
import com.thoughtworks.archguard.clazz.domain.service.ClassService
import com.thoughtworks.archguard.clazz.domain.service.ClassMethodCalleesService
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
        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val dependencee = JClass("id1", "com.thoughtworks.archguard.domain.dependencee", "archguard")
        val dependencer = JClass("id2", "com.thoughtworks.archguard.domain.dependencer", "archguard")
        //when
        every { repo.getJClassByName(targetName) } returns listOf(target)
        every { classDependenceesService.findDependencees(any(), any()) } returns listOf(dependencee)
        every { classDependencerService.findDependencers(any(), any()) } returns listOf(dependencer)

        val classDependencies = service.findDependencies("", targetName, 1)
        //then
        assertThat(classDependencies.caller.size).isEqualTo(1)
        assertThat(classDependencies.callee.size).isEqualTo(1)
        assertThat(classDependencies.caller[0]).isEqualToComparingFieldByField(dependencer)
        assertThat(classDependencies.callee[0]).isEqualToComparingFieldByField(dependencee)
    }

    @Test
    fun `should get class invokes`() {
        //given
        val targetName = "clazz"
        val module = ""
        val target = JClass("1", targetName, module)
        val deep = 3
        val needIncludeImpl = true
        //when
        every { repo.getJClassByName(targetName) } returns listOf(target)
        every {
            classInvokeService.findInvokes(target, deep, deep, needIncludeImpl)
        } returns target
        val invokes = service.findInvokes(module, targetName, deep, deep, needIncludeImpl)
        //then
        assertThat(invokes).isEqualToComparingFieldByField(target)
    }

    @Test
    fun `should get class method callees`() {
        //given
        val needIncludeImpl = true
        val needParents = true
        val module = "module"
        val name = "clazz"
        val deep = 2
        val targetClass = JClass("id", name, module)
        //when
        every {
            classMethodCalleesService.findClassMethodsCallees(targetClass, deep, needIncludeImpl, needParents)
        } returns JClass("id", "clazz", "module")
        every { repo.getJClassBy(name, module) } returns targetClass
        val target = service.findMethodsCallees(module, name, deep, needIncludeImpl, needParents)
        //then
        assertThat(target.methodCallees).isEmpty()
    }
}
