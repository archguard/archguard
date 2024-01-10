package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewDto
import com.thoughtworks.archguard.report.domain.overview.OverviewService
import com.thoughtworks.archguard.report.domain.overview.SystemOverview
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OverviewControllerTest {
    @Mock
    private lateinit var overviewService: OverviewService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockMvc = MockMvcBuilders.standaloneSetup(OverviewController(overviewService)).build()
    }

    @Test
    fun shouldReturnOverview() {
        val systemId = 1L
        val overview = BadSmellOverviewDto(listOf())
        given(overviewService.getOverview(systemId)).willReturn(overview)

        mockMvc.perform(get("/api/systems/{systemId}/overview", systemId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()
    }

    @Test
    fun shouldReturnSystemOverview() {
        val systemId = 1L
        val systemOverview = SystemOverview(1, 1L, listOf())
        given(overviewService.getSystemOverview(systemId)).willReturn(systemOverview)

        mockMvc.perform(get("/api/systems/{systemId}/overview/system", systemId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }
}
