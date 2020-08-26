package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NocServiceImplTest {
    @MockK
    lateinit var jClassRepository: JClassRepository

    private lateinit var nocService: NocServiceImpl

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        nocService = NocServiceImpl(jClassRepository)
    }

    @Test
    internal fun `should get noc for class`() {
        val projectId: Long = 1
        val jClass = JClass("0", "c", "m")
        val jClass1 = JClass("1", "c1", "m1")
        val jClass2 = JClass("2", "c2", "m2")
        val jClass3 = JClass("3", "c3", "m3")
        val jClass4 = JClass("4", "c4", "m4")
        val jClass5 = JClass("5", "c5", "m5")
        val jClass6 = JClass("6", "c6", "m6")
        every { jClassRepository.findClassImplements(projectId, "c", "m") } returns listOf(jClass1, jClass2)
        every { jClassRepository.findClassImplements(projectId, "c1", "m1") } returns listOf(jClass3)
        every { jClassRepository.findClassImplements(projectId, "c2", "m2") } returns listOf(jClass4, jClass5)
        every { jClassRepository.findClassImplements(projectId, "c3", "m3") } returns listOf()
        every { jClassRepository.findClassImplements(projectId, "c4", "m4") } returns listOf()
        every { jClassRepository.findClassImplements(projectId, "c5", "m5") } returns listOf(jClass6)
        every { jClassRepository.findClassImplements(projectId, "c6", "m6") } returns listOf()

        val noc = nocService.getNoc(projectId, jClass)
        assertThat(noc).isEqualTo(6)

        val noc2 = nocService.getNoc(projectId, jClass2)
        assertThat(noc2).isEqualTo(3)
    }
}
