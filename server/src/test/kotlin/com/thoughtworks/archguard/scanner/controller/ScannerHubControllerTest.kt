package com.thoughtworks.archguard.scanner.controller;

import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubExecutorService
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ScannerHubControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var hubService: HubExecutorService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ScannerHubController(
            "", "", "",
            hubService
        )).build()
    }

    @Test
    fun shouldScanModuleAndReturnIsRunningTrue() {
        val id = 1L
        val scannerVersion = "1.6.2"
        val url = "jdbc:mysql://localhost:3306/db"
        val isRunning = true

        `when`(hubService.doScanIfNotRunning(id, url, scannerVersion, InMemoryConsumer()))
            .thenReturn(isRunning)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/scanner/1/reports", id)
            .param("scannerVersion", scannerVersion))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isRunning").value(false))
    }

    @Test
    fun shouldEvaluateAndReturnIsRunningTrue() {
        val id = 1L
        val type = "type"
        val url = "jdbc:mysql://localhost:3306/db"
        val scannerVersion = "1.6.2"
        val isRunning = true

        `when`(hubService.evaluate(type, id, url, scannerVersion, listOf()))
            .thenReturn(isRunning)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/scanner/1/evaluations", id)
            .content("{\"type\":\"$type\",\"scannerVersion\":\"$scannerVersion\"}")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isRunning").value(false))
    }

    @Test
    fun shouldGetEvaluationStatusAndReturnIsRunningTrue() {
        val id = 1L
        val type = "type"
        val url = "jdbc:mysql://localhost:3306/db"
        val isRunning = true

        `when`(hubService.getEvaluationStatus(type, id))
            .thenReturn(isRunning)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/scanner/1/evaluations/status", id)
            .param("type", type))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isRunning").value(isRunning))
    }
}
