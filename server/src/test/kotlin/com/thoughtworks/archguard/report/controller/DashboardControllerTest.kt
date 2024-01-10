package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.application.Dashboard
import com.thoughtworks.archguard.report.application.DashboardService
import org.archguard.smell.BadSmellGroup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class DashboardControllerTest {

    @Test
    fun shouldReturnDashboardListWhenGetDashboard() {
        // Given
        val systemId = 1L
        val dashboardService = mock(DashboardService::class.java)
        val dashboardController = DashboardController(dashboardService)
        val dashboardList = listOf(
            Dashboard(BadSmellGroup.SIZING, listOf())
        )
        `when`(dashboardService.getDashboard(systemId)).thenReturn(dashboardList)

        // When
        val response = dashboardController.getDashboard(systemId)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(dashboardList, response.body)
    }

    @Test
    fun shouldSaveReportWhenSaveReport() {
        // Given
        val systemId = 1L
        val dashboardService = mock(DashboardService::class.java)
        val dashboardController = DashboardController(dashboardService)

        // When
        dashboardController.saveReport(systemId)

        // Then
        // Verify that the saveReport method of dashboardService is called
        Mockito.verify(dashboardService).saveReport(systemId)
    }
}
