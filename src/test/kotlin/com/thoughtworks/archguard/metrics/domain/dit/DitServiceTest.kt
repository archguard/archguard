package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DitServiceTest {
    private lateinit var service: DitService

    @MockK
    private lateinit var jClassRepository: JClassRepository
    @MockK
    private lateinit var classMetricRepository: ClassMetricRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = DitService(jClassRepository, classMetricRepository)
    }

    @Test
    fun `should get DepthOfInheritance`() {
        //given
        val systemId: Long = 1
        val child = JClass("any", "Child", "module")
        val parent = JClass("any", "Parent", "module")
        val grandparent = JClass("any", "Grandparent", "module")

        every { jClassRepository.findClassParents(systemId, "module", "Child") } returns listOf(parent)
        every { jClassRepository.findClassParents(systemId, "module", "Parent") } returns listOf(grandparent)
        every { jClassRepository.findClassParents(systemId, "module", "Grandparent") } returns listOf()

        //when
        val childDIT = service.getDepthOfInheritance(systemId, child)
        val parentDIT = service.getDepthOfInheritance(systemId, parent)
        val grandparentDIT = service.getDepthOfInheritance(systemId, grandparent)

        //then
        assertEquals(2, childDIT)
        assertEquals(1, parentDIT)
        assertEquals(0, grandparentDIT)

    }
}
