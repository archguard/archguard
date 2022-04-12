package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.code.method.domain.JMethod
import com.thoughtworks.archguard.code.method.domain.JMethodRepository
import com.thoughtworks.archguard.code.method.domain.service.MethodCallersService
import com.thoughtworks.archguard.code.method.domain.service.MethodConfigService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MethodCallersServiceTest {
    private lateinit var service: MethodCallersService

    @MockK
    private lateinit var repo: JMethodRepository

    @MockK
    private lateinit var configureService: ConfigureService

    @MockK
    private lateinit var methodConfigService: MethodConfigService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        service = MethodCallersService(repo, configureService, methodConfigService)

    }

    @Test
    fun `should get method callers`() {
        val systemId = 1L
        //given
        val target = JMethod("id", "method", "clazz", "module", "void", emptyList())
        val caller1 = JMethod("1", "caller1", "clazz2", "module", "void", emptyList())
        val caller2 = JMethod("2", "caller2", "clazz3", "module", "void", emptyList())
        //when
        every { repo.findMethodCallers(target.id) } returns listOf(caller1)
        every { repo.findMethodCallers(caller1.id) } returns listOf(caller2)
        every { repo.findMethodCallers(caller2.id) } returns listOf()
        every { configureService.isDisplayNode(any(), any()) } returns true
        every { methodConfigService.buildColorConfig(any(), any()) } returns Unit

        val result = service.findCallers(systemId, listOf(target), 2)[0]
        //then
        Assertions.assertThat(result.callers.size).isEqualTo(1)
        Assertions.assertThat(result.callers[0]).isEqualToComparingFieldByField(caller1)
        Assertions.assertThat(result.callers[0].callers.size).isEqualTo(1)
        Assertions.assertThat(result.callers[0].callers[0]).isEqualToComparingFieldByField(caller2)
    }
}
