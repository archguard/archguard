package com.thoughtworks.archguard.config.domain

import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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
}