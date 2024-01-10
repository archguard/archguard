package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.domain.coupling.hub.HubService
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReportBadSmellControllerTest {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockMvc = MockMvcBuilders.standaloneSetup(ReportBadSmellController()).build()
    }

    @Test
    fun should_return_thresholds_when_getThresholds() {
        val result = mockMvc.perform(get("/api/systems/1/badsmell-thresholds"))
            .andExpect(status().isOk)
            .andReturn()
    }
}
