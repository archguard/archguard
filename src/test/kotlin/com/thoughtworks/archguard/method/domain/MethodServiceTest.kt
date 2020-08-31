package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.method.domain.service.MethodCallersService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MethodServiceTest {

    @InjectMockKs
    private var service = MethodService()

    @MockK
    private lateinit var repo: JMethodRepository

    @MockK
    private lateinit var callerService: MethodCallersService

    @MockK
    private lateinit var calleeService: MethodCalleesService

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get method callers`() {
        //given
        val systemId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(systemId, moduleName, clazzName, methodName) } returns target
        every { callerService.findCallers(target, deep) } returns target
        val result = service.findMethodCallers(systemId, moduleName, clazzName, methodName, deep)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }

    @Test
    fun `should get method callees`() {
        //given
        val systemId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(systemId, moduleName, clazzName, methodName) } returns target
        every { calleeService.findCallees(target, deep, true) } returns target
        val result = service.findMethodCallees(systemId, moduleName, clazzName, methodName, deep, true)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }

    @Test
    fun `should get method invokes`() {
        //given
        val systemId = 1L
        val moduleName = "module"
        val clazzName = "clazz"
        val methodName = "method"
        val target = listOf(JMethod("id", methodName, clazzName, moduleName, "void", emptyList()))
        val deep = 1
        //when
        every { repo.findMethodByModuleAndClazzAndName(systemId, moduleName, clazzName, methodName) } returns target
        every { calleeService.findCallees(target, deep, true) } returns target
        every { callerService.findCallers(target, deep) } returns target
        val result = service.findMethodInvokes(systemId, moduleName, clazzName, methodName, deep, deep, true)
        //then
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(target)
    }
}
