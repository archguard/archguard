package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.code.method.domain.JMethodRepository
import com.thoughtworks.archguard.code.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.code.method.domain.service.MethodConfigService
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.archguard.model.code.JMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MethodCalleesServiceTest {
    private lateinit var service: MethodCalleesService

    @MockK
    private lateinit var repo: JMethodRepository

    @MockK
    private lateinit var configureService: ConfigureService

    @MockK
    private lateinit var methodConfigService: MethodConfigService

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = MethodCalleesService(repo, configureService, methodConfigService)
    }

    @Test
    fun `should get method callees`() {
        // given
        val systemId = 1L
        val target = JMethod("id", "method", "clazz", "module", "void", emptyList())
        val callee1 = JMethod("1", "callee1", "clazz2", "module", "void", emptyList())
        val callee2 = JMethod("2", "callee2", "clazz3", "module", "void", emptyList())
        val implement = JMethod("3", "callee2", "clazz3", "module", "void", emptyList())
        // when
        every { repo.findMethodCallees(target.id) } returns listOf(callee1)
        every { repo.findMethodCallees(callee1.id) } returns listOf(callee2)
        every { repo.findMethodCallees(callee2.id) } returns listOf()
        every { repo.findMethodCallees(implement.id) } returns listOf()
        every { repo.findMethodImplements(target.id, target.name) } returns listOf(implement)
        every { repo.findMethodImplements(callee1.id, callee1.name) } returns listOf()
        every { repo.findMethodImplements(callee2.id, callee2.name) } returns listOf()
        every { repo.findMethodImplements(implement.id, implement.name) } returns listOf()
        every { configureService.isDisplayNode(any(), any()) } returns true
        every { methodConfigService.buildColorConfig(any(), any()) } returns Unit

        val result = service.findCallees(systemId, listOf(target), 2, true)[0]
        // then
        assertThat(result.callees.size).isEqualTo(1)
        assertThat(result.callees[0]).isEqualToComparingFieldByField(callee1)
        assertThat(result.implements.size).isEqualTo(1)
        assertThat(result.implements[0]).isEqualToComparingFieldByField(implement)
        assertThat(result.callees[0].callees.size).isEqualTo(1)
        assertThat(result.callees[0].callees[0]).isEqualToComparingFieldByField(callee2)
    }
}
