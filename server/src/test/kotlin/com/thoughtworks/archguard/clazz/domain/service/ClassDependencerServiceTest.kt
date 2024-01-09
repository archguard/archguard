package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.clazz.domain.service.ClassConfigService
import com.thoughtworks.archguard.code.clazz.domain.service.ClassDependencerService
import com.thoughtworks.archguard.config.domain.ConfigureService
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.archguard.model.code.JClass
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ClassDependencerServiceTest {
    private lateinit var service: ClassDependencerService

    @MockK
    private lateinit var repo: JClassRepository

    @MockK
    private lateinit var configureService: ConfigureService

    @MockK
    private lateinit var classConfigService: ClassConfigService

    @BeforeEach
    internal fun setUp() {
        init(this)
        service = ClassDependencerService(repo, configureService, classConfigService)
        every { configureService.isDisplayNode(any(), any()) } returns true
        every { classConfigService.buildJClassColorConfig(any(), any()) } returns Unit
    }

    @Test
    internal fun `should get callers`() {
        // given
        val systemId = 1L

        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val caller1 = JClass("2", "caller1", "module")
        val caller2 = JClass("3", "caller2", "module")
        // when
        every { repo.findDependencers(target.id) } returns listOf(caller1)
        every { repo.findDependencers(caller1.id) } returns listOf(caller2)

        val result = service.findDependencers(systemId, target, 2)
        // then
        Assertions.assertThat(result.dependencers.size).isEqualTo(1)
        Assertions.assertThat(result.dependencers[0].name).isEqualTo("caller1")
        Assertions.assertThat(result.dependencers[0].dependencers.size).isEqualTo(1)
        Assertions.assertThat(result.dependencers[0].dependencers[0].name).isEqualTo("caller2")
    }

    @Test
    internal fun `should get class dependencers when deep is larger`() {
        // given
        val systemId = 1L

        val targetName = "clazz"
        val target = JClass("1", targetName, "module")
        val dependencer1 = JClass("2", "dependencer1", "module")
        val dependencer2 = JClass("3", "dependencer2", "module")
        // when
        every { repo.findDependencers(target.id) } returns listOf(dependencer1)
        every { repo.findDependencers(dependencer1.id) } returns listOf(dependencer2)
        every { repo.findDependencers(dependencer2.id) } returns listOf()

        val result = service.findDependencers(systemId, target, 4)
        // then
        Assertions.assertThat(result.dependencers.size).isEqualTo(1)
        Assertions.assertThat(result.dependencers[0].name).isEqualTo("dependencer1")
        Assertions.assertThat(result.dependencers[0].dependencers.size).isEqualTo(1)
        Assertions.assertThat(result.dependencers[0].dependencers[0].name).isEqualTo("dependencer2")
        Assertions.assertThat(result.dependencers[0].dependencers[0].dependencers).isEmpty()
    }
}
