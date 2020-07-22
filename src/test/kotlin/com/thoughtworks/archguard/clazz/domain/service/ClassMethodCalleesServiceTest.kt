package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.clazz.domain.JClass
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassMethodCalleesServiceTest {
    @InjectMockKs
    private var service: ClassMethodCalleesService = ClassMethodCalleesService()

    @MockK
    private lateinit var repo: JClassRepository

    @MockK
    private lateinit var methodRepo: JMethodRepository

    @MockK
    private lateinit var methodCalleesService: MethodCalleesService

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    fun `should get class method callees`() {
        //given
        val name = "clazz"
        val module = "module"
        val target = JClass("id", name, module)
        val method1 = JMethod("1", "method1", "class", "module")
        val method2 = JMethod("2", "method2", "class", "module")
        val parent = JClass("1", "parent", "module")
        //when
        every { methodRepo.findMethodsByModuleAndClass(module, name) } returns listOf(method1, method2)
        every { methodRepo.findMethodsByModuleAndClass(module, parent.name) } returns listOf(method1)
        every { repo.findClassParents(module, name) } returns listOf(parent)
        every { repo.findClassParents(parent.module, parent.name) } returns listOf()
        every { methodCalleesService.buildMethodCallees(method1, 1, true, true) } returns method1
        every { methodCalleesService.buildMethodCallees(method2, 1, true, true) } returns method2
        val result = service.findClassMethodsCallees(target, 1, true, true)
        //then
        assertThat(result.methods.size).isEqualTo(2)
        assertThat(result.parents.size).isEqualTo(1)
        assertThat(result.parents[0].methods.size).isEqualTo(1)
    }
}
