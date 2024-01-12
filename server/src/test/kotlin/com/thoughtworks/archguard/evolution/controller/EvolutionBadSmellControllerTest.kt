package com.thoughtworks.archguard.evolution.controller;

import com.thoughtworks.archguard.evolution.domain.BadSmellSuite
import com.thoughtworks.archguard.evolution.domain.BadSmellSuiteWithSelected
import com.thoughtworks.archguard.evolution.domain.BadSmellThresholdService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class EvolutionBadSmellControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var badSmellThresholdService: BadSmellThresholdService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(EvolutionBadSmellController(badSmellThresholdService)).build()
    }

    @Test
    fun shouldReturnAllThresholds() {
        val badSmellSuite1 = BadSmellSuite(1, "Suite 1", false, listOf())
        val badSmellSuite2 = BadSmellSuite(2, "Suite 2", true, listOf())
        val badSmellSuites = listOf(badSmellSuite1, badSmellSuite2)

        `when`(badSmellThresholdService.getAllSuits()).thenReturn(badSmellSuites)

        mockMvc.perform(get("/api/evolution/badsmell-thresholds"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].suiteName").value("Suite 1"))
            .andExpect(jsonPath("$[0].isDefault").value(false))
    }

    @Test
    fun shouldReturnThresholdsBySystemId() {
        val systemId = 1L
        val badSmellSuite1 = BadSmellSuiteWithSelected(1, "Suite 1", false, true, listOf())
        val badSmellSuite2 = BadSmellSuiteWithSelected(2, "Suite 2", true, false, listOf())
        val badSmellSuites = listOf(badSmellSuite1, badSmellSuite2)

        `when`(badSmellThresholdService.getBadSmellSuiteWithSelectedInfoBySystemId(systemId)).thenReturn(badSmellSuites)

        mockMvc.perform(get("/api/evolution/badsmell-thresholds/system/$systemId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].suiteName").value("Suite 1"))
            .andExpect(jsonPath("$[0].isDefault").value(false))
            .andExpect(jsonPath("$[0].isSelected").value(true))
    }
}
