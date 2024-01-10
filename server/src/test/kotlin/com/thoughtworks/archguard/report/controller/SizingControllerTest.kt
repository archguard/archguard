package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.ValidPagingParam.validFilterParam
import com.thoughtworks.archguard.report.domain.sizing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SizingControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sizingService: SizingService

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(SizingController(sizingService)).build()
    }

    @Test
    fun `should return modules above line threshold`() {
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val request = validFilterParam(filterSizing)
        val moduleSizingList = listOf(
            ModuleSizing("1", 1, "Module1", 10, 20, 200)
        )
        val count = 1L
        val threshold = 10

        `when`(sizingService.getModuleSizingListAboveLineThresholdByFilterSizing(systemId, request.toFilterSizing()))
            .thenReturn(Triple(moduleSizingList, count, threshold))

        mockMvc.perform(
            post("/api/systems/1/sizing/modules/above-line-threshold", systemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Json.encodeToString(filterSizing)
                )
        )
            .andExpect(status().isOk)
    }
}
