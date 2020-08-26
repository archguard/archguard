package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DitServiceTest {
    private lateinit var service: DitServiceImpl

    @MockK
    private lateinit var repo: JClassRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = DitServiceImpl(repo)
    }

    @Test
    fun `should get DepthOfInheritance`() {
        //given
        val projectId: Long = 1
        val child = JClass("any", "Child", "module")
        val parent = JClass("any", "Parent", "module")
        val grandparent = JClass("any", "Grandparent", "module")

        every { repo.findClassParents(projectId, "module", "Child") } returns listOf(parent)
        every { repo.findClassParents(projectId, "module", "Parent") } returns listOf(grandparent)
        every { repo.findClassParents(projectId, "module", "Grandparent") } returns listOf()

        //when
        val childDIT = service.getDepthOfInheritance(projectId, child)
        val parentDIT = service.getDepthOfInheritance(projectId, parent)
        val grandparentDIT = service.getDepthOfInheritance(projectId, grandparent)

        //then
        assertEquals(2, childDIT)
        assertEquals(1, parentDIT)
        assertEquals(0, grandparentDIT)

    }
}
