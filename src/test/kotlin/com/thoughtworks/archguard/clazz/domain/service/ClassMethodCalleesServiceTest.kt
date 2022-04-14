package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.clazz.domain.service.ClassConfigService
import com.thoughtworks.archguard.code.clazz.domain.service.ClassMethodCalleesService
import com.thoughtworks.archguard.code.method.domain.JMethod
import com.thoughtworks.archguard.code.method.domain.JMethodRepository
import com.thoughtworks.archguard.code.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.code.method.domain.service.MethodConfigService
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassMethodCalleesServiceTest {
    private lateinit var service: ClassMethodCalleesService

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @MockK
    private lateinit var jMethodRepository: JMethodRepository

    @MockK
    private lateinit var methodCalleesService: MethodCalleesService

    @MockK
    private lateinit var configureService: ConfigureService

    @MockK
    private lateinit var classConfigService: ClassConfigService

    @MockK
    private lateinit var methodConfigService: MethodConfigService

    @BeforeEach
    fun setUp() {
        init(this)
        service = ClassMethodCalleesService(
            jMethodRepository, jClassRepository, methodCalleesService, configureService,
            classConfigService, methodConfigService
        )
    }

    @Test
    fun `should get class method callees`() {
        // given
        val systemId: Long = 1
        val name = "clazz"
        val module = "module"
        val target = JClass("id", name, module)
        val method1 = JMethod("1", "method1", "class", "module", "void", emptyList())
        val method2 = JMethod("2", "method2", "class", "module", "void", emptyList())
        val parent = JClass("1", "parent", "module")
        // when
        every { jMethodRepository.findMethodsByModuleAndClass(systemId, module, name) } returns listOf(method1, method2)
        every { jMethodRepository.findMethodsByModuleAndClass(systemId, module, parent.name) } returns listOf(method1)
        every { jClassRepository.findClassParents(systemId, module, name) } returns listOf(parent)
        every { jClassRepository.findClassParents(systemId, parent.module!!, parent.name) } returns listOf()
        every { methodCalleesService.buildMethodCallees(systemId, listOf(method1, method2), 1, true) } returns listOf(method1)
        every { methodCalleesService.buildMethodCallees(systemId, listOf(method1), 1, true) } returns listOf(method2)
        every { configureService.isDisplayNode(any(), any()) } returns true
        every { classConfigService.buildJClassColorConfig(any(), any()) } returns Unit
        every { methodConfigService.buildColorConfig(any(), any()) } returns Unit

        val result = service.findClassMethodsCallees(systemId, target, 1, true, true)
        // then
        assertThat(result.methods.size).isEqualTo(2)
        assertThat(result.parents.size).isEqualTo(1)
        assertThat(result.parents[0].methods.size).isEqualTo(1)
    }
}
