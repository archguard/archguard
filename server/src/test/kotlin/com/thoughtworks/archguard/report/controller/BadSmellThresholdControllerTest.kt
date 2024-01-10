package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteServiceWithDepends
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BadSmellThresholdControllerTest {
    @MockBean
    private lateinit var thresholdSuiteService: ThresholdSuiteServiceWithDepends

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockMvc = MockMvcBuilders.standaloneSetup(BadSmellThresholdController(thresholdSuiteService)).build()
    }

    @Test
    fun shouldReloadThresholdCache() {
        // Given
        Mockito.doNothing().`when`(thresholdSuiteService).reloadAllSuites()

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bad-smell-threshold/reload"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
