package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NocServiceTest {
    @MockK
    lateinit var jClassRepository: JClassRepository

    private lateinit var nocService: NocService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        nocService = NocService(jClassRepository)
    }

    @Test
    internal fun should_get_noc_for_class() {
        val systemId: Long = 1
        val jClass = JClass("0", "c", "m")
        val jClass1 = JClass("1", "c1", "m1")
        val jClass2 = JClass("2", "c2", "m2")
        val jClass3 = JClass("3", "c3", "m3")
        val jClass4 = JClass("4", "c4", "m4")
        val jClass5 = JClass("5", "c5", "m5")
        val jClass6 = JClass("6", "c6", "m6")
        every { jClassRepository.findClassImplements(systemId, "c", "m") } returns listOf(jClass1, jClass2)
        every { jClassRepository.findClassImplements(systemId, "c1", "m1") } returns listOf(jClass3)
        every { jClassRepository.findClassImplements(systemId, "c2", "m2") } returns listOf(jClass4, jClass5)
        every { jClassRepository.findClassImplements(systemId, "c3", "m3") } returns listOf()
        every { jClassRepository.findClassImplements(systemId, "c4", "m4") } returns listOf()
        every { jClassRepository.findClassImplements(systemId, "c5", "m5") } returns listOf(jClass6)
        every { jClassRepository.findClassImplements(systemId, "c6", "m6") } returns listOf()

        val noc = nocService.getNoc(systemId, jClass)
        assertThat(noc).isEqualTo(6)

        val noc2 = nocService.getNoc(systemId, jClass2)
        assertThat(noc2).isEqualTo(3)
    }
}
