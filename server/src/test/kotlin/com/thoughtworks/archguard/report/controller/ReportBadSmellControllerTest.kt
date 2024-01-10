package com.thoughtworks.archguard.report.controller;

import io.mockk.MockKAnnotations
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
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
        mockMvc.perform(get("/api/systems/1/badsmell-thresholds"))
            .andExpect(status().isOk)
            .andReturn()
    }
}
