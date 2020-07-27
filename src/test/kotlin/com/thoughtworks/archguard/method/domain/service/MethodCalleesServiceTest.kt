package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MethodCalleesServiceTest {
    @InjectMockKs
    private var service = MethodCalleesService()

    @MockK
    private lateinit var repo: JMethodRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    fun `should get method callees`() {
        //given
        val target = JMethod("id", "method", "clazz", "module", null, emptyList())
        val callee1 = JMethod("1", "callee1", "clazz2", "module", null, emptyList())
        val callee2 = JMethod("2", "callee2", "clazz3", "module", null, emptyList())
        val implement = JMethod("3", "callee2", "clazz3", "module", null, emptyList())
        //when
        every { repo.findMethodCallees(target.id) } returns listOf(callee1)
        every { repo.findMethodCallees(callee1.id) } returns listOf(callee2)
        every { repo.findMethodCallees(callee2.id) } returns listOf()
        every { repo.findMethodCallees(implement.id) } returns listOf()
        every { repo.findMethodImplements(target.id, target.name) } returns listOf(implement)
        every { repo.findMethodImplements(callee1.id, callee1.name) } returns listOf()
        every { repo.findMethodImplements(callee2.id, callee2.name) } returns listOf()
        every { repo.findMethodImplements(implement.id, implement.name) } returns listOf()
        val result = service.findCallees(listOf(target), 2, true)[0]
        //then
        assertThat(result.callees.size).isEqualTo(1)
        assertThat(result.callees[0]).isEqualToComparingFieldByField(callee1)
        assertThat(result.implements.size).isEqualTo(1)
        assertThat(result.implements[0]).isEqualToComparingFieldByField(implement)
        assertThat(result.callees[0].callees.size).isEqualTo(1)
        assertThat(result.callees[0].callees[0]).isEqualToComparingFieldByField(callee2)
    }
}