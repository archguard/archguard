package com.thoughtworks.archguard.config.domain

import io.mockk.MockKAnnotations.init
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ConfigureServiceTest {
    @InjectMockKs
    var service = ConfigureService()

    @MockK
    lateinit var repo: ConfigureRepository

    @BeforeEach
    internal fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get all configures`() {
        //given
        val configure = NodeConfigure("id", "nodeHidden", "clz", "21", 1)
        //when
        every { repo.getConfigures() } returns listOf(configure)
        val configures = service.getConfigures()
        //then
        assertThat(configures.size).isEqualTo(1)
        assertThat(configures[0]).isEqualToComparingFieldByField(configure)
    }

    @Test
    internal fun `should create configure`() {
        //given
        val configure = NodeConfigure("id", "nodeHidden", "clz", "21", 1)
        //when
        every { repo.create(configure) } just Runs
        service.create(configure)
        //then
        verify { repo.create(configure) }
    }

    @Test
    internal fun `should update configure`() {
        //given
        val id = "id"
        val configure = NodeConfigure(id, "nodeHidden", "clz", "21", 1)
        //when
        every { repo.update(any()) } just Runs
        service.update(id, configure)
        //then
        verify { repo.update(any()) }
    }

    @Test
    internal fun `should delete configure`() {
        //given
        val id = "id"
        //when
        every { repo.delete(id) } just Runs
        service.delete(id)
        //then
        verify { repo.delete(id) }
    }
}