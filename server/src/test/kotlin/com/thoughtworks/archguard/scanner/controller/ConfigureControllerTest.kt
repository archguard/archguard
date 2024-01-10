package com.thoughtworks.archguard.scanner.controller;

import com.thoughtworks.archguard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateMessageDTO
import com.thoughtworks.archguard.scanner.domain.config.service.ScannerConfigureService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ConfigureControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var configureService: ScannerConfigureService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ConfigureController(configureService)).build()
    }

    @Test
    fun shouldReturnConfigures() {
        val configure1 = ConfigureDTO("1", "type1", "key1", "value1")
        val configure2 = ConfigureDTO("2", "type2", "key2", "value2")
        val configureList = listOf(configure1, configure2)

        `when`(configureService.getConfigures()).thenReturn(configureList)

        mockMvc.perform(get("/api/scanner/config"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].type").value("type1"))
            .andExpect(jsonPath("$[1].type").value("type2"))
    }

    @Test
    fun shouldUpdateConfigure() {
        val update1 = UpdateDTO("config1", "value1")
        val update2 = UpdateDTO("config2", "value2")
        val updateList = listOf(update1, update2)
        val updateMessage = UpdateMessageDTO(true, "Configure updated successfully")

        `when`(configureService.updateConfigure(updateList)).thenReturn(updateMessage)

        mockMvc.perform(
            post("/api/scanner/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Json.encodeToString(listOf((update1)))
                )
        )
            .andExpect(status().isOk)
    }
}
