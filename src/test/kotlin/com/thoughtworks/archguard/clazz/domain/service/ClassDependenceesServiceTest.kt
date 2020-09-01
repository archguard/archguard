package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ClassDependenceesServiceTest {

    private lateinit var service: ClassDependenceesService

    @MockK
    private lateinit var repo: JClassRepository

    @MockK
    private lateinit var configureService: ConfigureService

    @MockK
    private lateinit var classConfigService: ClassConfigService

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = ClassDependenceesService(repo, configureService, classConfigService)
        every { configureService.isDisplayNode(any(), any()) } returns true
        every { classConfigService.buildJClassColorConfig(any(), any()) } returns Unit
    }

    @Test
    internal fun `should get class dependencees`() {
        //given
        val systemId = 1L

        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val callee1 = JClass("2", "callee1", "module")
        val callee2 = JClass("3", "callee2", "module")
        //when
        every { repo.findDependencees(target.id) } returns listOf(callee1)
        every { repo.findDependencees(callee1.id) } returns listOf(callee2)


        val result = service.findDependencees(systemId, target, 2)
        //then
        assertThat(result.dependencees.size).isEqualTo(1)
        assertThat(result.dependencees[0].name).isEqualTo("callee1")
        assertThat(result.dependencees[0].dependencees.size).isEqualTo(1)
        assertThat(result.dependencees[0].dependencees[0].name).isEqualTo("callee2")
    }

    @Test
    internal fun `should get class dependencees when deep is larger`() {
        //given
        val targetName = "clazz"
        val systemId = 1L

        val target = JClass("1", targetName, "module")
        val dependencee1 = JClass("2", "dependencee1", "module")
        val dependencee2 = JClass("3", "dependencee2", "module")
        //when
        every { repo.findDependencees(target.id) } returns listOf(dependencee1)
        every { repo.findDependencees(dependencee1.id) } returns listOf(dependencee2)
        every { repo.findDependencees(dependencee2.id) } returns listOf()

        val result = service.findDependencees(systemId, target, 4)
        //then
        assertThat(result.dependencees.size).isEqualTo(1)
        assertThat(result.dependencees[0].name).isEqualTo("dependencee1")
        assertThat(result.dependencees[0].dependencees.size).isEqualTo(1)
        assertThat(result.dependencees[0].dependencees[0].name).isEqualTo("dependencee2")
        assertThat(result.dependencees[0].dependencees[0].dependencees).isEmpty()
    }
}
