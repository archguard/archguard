package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.ServicesMapService
import com.thoughtworks.archguard.report.domain.container.ContainerServiceResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.RestController

class ContainerServiceControllerTest {
    private val service = mock(ServicesMapService::class.java)
    private val controller = ContainerServiceController(service)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(controller).build()

    @Test
    fun `should return container service response when getting by system_id`() {
        val systemId = 1L
        val response = ContainerServiceResponse(systemId, demands = emptyList(), resources = emptyList())
        `when`(service.findBySystemId(systemId)).thenReturn(response)

        mockMvc.perform(get("/api/container-service/{systemId}", systemId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(systemId))
    }

    @Test
    fun `should return container service response when getting services map by system id`() {
        // given
        val systemId = 1L
        val expectedResponse = ContainerServiceResponse(systemId, demands = emptyList(), resources = emptyList())

        `when`(service.findBySystemId(systemId)).thenReturn(expectedResponse)

        // when
        val actualResponse = controller.getServicesMap(systemId)

        // then
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `should return list of container service responses when getting systems by ids`() {
        // given
        val ids = listOf("1", "2", "3")
        val expectedResponses = listOf(
            ContainerServiceResponse(1L, demands = emptyList(), resources = emptyList()),
            ContainerServiceResponse(2L, demands = emptyList(), resources = emptyList()),
            ContainerServiceResponse(3L, demands = emptyList(), resources = emptyList())
        )

        `when`(service.findAllServiceByIds(ids)).thenReturn(expectedResponses)

        // when
        val actualResponses = controller.getSystemByIds(ids)

        // then
        assertEquals(expectedResponses, actualResponses)
    }
}
