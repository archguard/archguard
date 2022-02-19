package com.thoughtworks.archguard.config.domain

import io.mockk.MockKAnnotations.init
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScannerConfigureServiceTest {
    private lateinit var service: ConfigureService

    @MockK
    lateinit var configureRepository: ConfigureRepository

    @BeforeEach
    fun setUp() {
        init(this)
        service = ConfigureService(configureRepository)
    }

    @Test
    fun `should get all configures`() {
        //given
        val configure = Configure("id", 1L, "nodeHidden", "clz", "21", 1)
        //when
        every { configureRepository.getConfigures(configure.systemId) } returns listOf(configure)
        val configures = service.getConfigures(configure.systemId)
        //then
        assertThat(configures.size).isEqualTo(1)
        assertThat(configures[0]).isEqualToComparingFieldByField(configure)
    }

    @Test
    fun `should create configure`() {
        //given
        val configure = Configure("id", 1L, "nodeHidden", "clz", "21", 1)
        //when
        every { configureRepository.create(configure) } just Runs
        service.create(configure)
        //then
        verify { configureRepository.create(configure) }
    }

    @Test
    fun `should update configure`() {
        //given
        val id = "id"
        val configure = Configure(id, 1L, "nodeHidden", "clz", "21", 1)
        //when
        every { configureRepository.update(any()) } just Runs
        service.update(id, configure)
        //then
        verify { configureRepository.update(any()) }
    }

    @Test
    fun `should delete configure`() {
        //given
        val id = "id"
        //when
        every { configureRepository.delete(id) } just Runs
        service.delete(id)
        //then
        verify { configureRepository.delete(id) }
    }

    @Test
    fun `should display class`() {
        //given
        //when
        every { configureRepository.getConfigures(1L) } returns listOf(
                Configure("", 1L, "nodeDisplay", "com", "contain", 1),
                Configure("", 1L, "nodeDisplay", "org", "contain", 1),
                Configure("", 1L, "nodeDisplay", "common", "contain", 1),
                Configure("", 1L, "nodeDisplay", "org.scalatest", "hidden", 1),
                Configure("", 1L, "nodeColor", "org.scalatest", "#ff8975", 1)
        )

        assert(service.isDisplayNode(1L, "common.domain.ConfigureService"))
        assert(service.isDisplayNode(1L, "org.scalamock.scalatest.MockFactory"))
        assert(!service.isDisplayNode(1L, "common.domain.ConfigureServiceTest"))
        assert(!service.isDisplayNode(1L, "V"))
        assert(!service.isDisplayNode(1L, "org.scalatest.FlatSpec"))
        assert(!service.isDisplayNode(1L, "scala.concurrent.Future"))
        assert(!service.isDisplayNode(1L, "java.lang.Integer"))
    }
}
