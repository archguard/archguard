package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.method.domain.service.MethodCallersService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MethodServiceTest {

    private lateinit var service: MethodService

    @MockK
    private lateinit var repo: JMethodRepository

    @MockK
    private lateinit var callerService: MethodCallersService

    @MockK
    private lateinit var calleeService: MethodCalleesService

    @MockK
    private lateinit var configureService: ConfigureService

    @BeforeEach
    fun setUp() {
        init(this)
        service = MethodService(repo, calleeService, callerService, configureService)
    }

    @Test
    internal fun `should get method callers`() {
        //given
        val projectId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(projectId, moduleName, clazzName, methodName) } returns target
        every { callerService.findCallers(projectId, target, deep) } returns target
        every { configureService.isDisplayNode(any(), any()) } returns true

        val result = service.findMethodCallers(projectId, moduleName, clazzName, methodName, deep)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }

    @Test
    fun `should get method callees`() {
        //given
        val projectId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(projectId, moduleName, clazzName, methodName) } returns target
        every { calleeService.findCallees(projectId, target, deep, true) } returns target
        every { configureService.isDisplayNode(any(), any()) } returns true

        val result = service.findMethodCallees(projectId, moduleName, clazzName, methodName, deep, true)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }

    @Test
    fun `should get method invokes`() {
        //given
        val projectId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(projectId, moduleName, clazzName, methodName) } returns target
        every { calleeService.findCallees(projectId, target, deep, true) } returns target
        every { callerService.findCallers(projectId, target, deep) } returns target
        every { configureService.isDisplayNode(any(), any()) } returns true

        val result = service.findMethodInvokes(projectId, moduleName, clazzName, methodName, deep, deep, true)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }
}
