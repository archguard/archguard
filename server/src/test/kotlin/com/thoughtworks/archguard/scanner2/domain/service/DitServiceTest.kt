package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DitServiceTest {
    private lateinit var service: DitService

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = DitService(jClassRepository)
    }

    @Test
    fun should_get_depthOfInheritance() {
        // given
        val systemId: Long = 1
        val child = JClass("any", "Child", "module")
        val parent = JClass("any", "Parent", "module")
        val grandparent = JClass("any", "Grandparent", "module")

        every { jClassRepository.findClassParents(systemId, "module", "Child") } returns listOf(parent)
        every { jClassRepository.findClassParents(systemId, "module", "Parent") } returns listOf(grandparent)
        every { jClassRepository.findClassParents(systemId, "module", "Grandparent") } returns listOf()

        // when
        val childDIT = service.getDepthOfInheritance(systemId, child)
        val parentDIT = service.getDepthOfInheritance(systemId, parent)
        val grandparentDIT = service.getDepthOfInheritance(systemId, grandparent)

        // then
        assertEquals(2, childDIT)
        assertEquals(1, parentDIT)
        assertEquals(0, grandparentDIT)
    }
}
