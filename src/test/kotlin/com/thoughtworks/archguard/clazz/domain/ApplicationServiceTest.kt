package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.clazz.domain.service.ClassDependenceesService
import com.thoughtworks.archguard.clazz.domain.service.ClassDependencerService
import com.thoughtworks.archguard.module.domain.model.JClass
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationServiceTest {
    @InjectMockKs
    var service = ApplicationService()

    @MockK
    lateinit var classDependenceesService: ClassDependenceesService
    @MockK
    lateinit var classDependencerService: ClassDependencerService

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get class dependencies`() {
        //given
        val dependencee = JClass("com.thoughtworks.archguard.domain.dependencee", "archguard")
        val dependencer = JClass("com.thoughtworks.archguard.domain.dependencer", "archguard")
        //when
        every { classDependenceesService.findDependencees(any(), any()) } returns listOf(dependencee)
        every { classDependencerService.findDependencers(any(), any()) } returns listOf(dependencer)

        val classDependencies = service.findDependencies("className", 1)
        //then
        assertThat(classDependencies.caller.size).isEqualTo(1)
        assertThat(classDependencies.callee.size).isEqualTo(1)
        assertThat(classDependencies.caller[0]).isEqualToComparingFieldByField(dependencer)
        assertThat(classDependencies.callee[0]).isEqualToComparingFieldByField(dependencee)
    }
}