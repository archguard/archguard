package com.thoughtworks.archguard.qualitygate.domain

import com.thoughtworks.archguard.config.domain.Configure
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfileServiceTest {
    @InjectMockKs
    var service = ProfileService()

    @MockK
    lateinit var repo: ProfileRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should get all configures`() {
        //given
        val profile = QualityGateProfile("name", listOf(QualityGateConfig()))
        //when
        every { repo.getAll() } returns listOf(profile)
        val configures = service.getAll()
        //then
        Assertions.assertThat(configures.size).isEqualTo(1)
        Assertions.assertThat(configures[0]).isEqualToComparingFieldByField(profile)
    }

    @Test
    fun `should create configure`() {
        //given
        val profile = QualityGateProfile("name", listOf(QualityGateConfig()))
        //when
        every { repo.create(profile) } just Runs
        service.create(profile)
        //then
        verify { repo.create(profile) }
    }

    @Test
    fun `should update configure`() {
        //given
        val id = "id"
        val profile = QualityGateProfile("name", listOf(QualityGateConfig()))
        //when
        every { repo.update(any()) } just Runs
        service.update(id, profile)
        //then
        verify { repo.update(any()) }
    }

    @Test
    fun `should delete configure`() {
        //given
        val id = "id"
        //when
        every { repo.delete(id) } just Runs
        service.delete(id)
        //then
        verify { repo.delete(id) }
    }
}