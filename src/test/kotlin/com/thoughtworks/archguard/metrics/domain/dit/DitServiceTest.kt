package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DitServiceTest {
    @InjectMockKs
    var service = DitService()

    @MockK
    private lateinit var repo: JClassRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    fun `should get DepthOfInheritance`() {
        //given
        val child = JClass("any", "Child", "module")
        val parent = JClass("any", "Parent", "module")
        val grandparent = JClass("any", "Grandparent", "module")

        every { repo.findClassParents("module", "Child") } returns listOf(parent)
        every { repo.findClassParents("module", "Parent") } returns listOf(grandparent)
        every { repo.findClassParents("module", "Grandparent") } returns listOf()

        //when
        val childDIT = service.getDepthOfInheritance(child)
        val parentDIT = service.getDepthOfInheritance(parent)
        val grandparentDIT = service.getDepthOfInheritance(grandparent)

        //then
        assertEquals(2, childDIT)
        assertEquals(1, parentDIT)
        assertEquals(0, grandparentDIT)

    }
}
