package com.thoughtworks.archguard.scanner.controller;

import com.thoughtworks.archguard.scanner.domain.analyser.ArchitectureDependencyAnalysis
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus

class AnalysisControllerTest {
    private val dependencyAnalysis = mock(ArchitectureDependencyAnalysis::class.java)
    private val analysisController = AnalysisController(dependencyAnalysis)

    @Test
    fun shouldAnalyseDependencySuccessfully() {
        val systemId = 1L
        val scannerVersion = "1.6.2"

        val response = analysisController.analyseDependency(systemId, scannerVersion)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("", response.body)
    }

    @Test
    fun shouldHandleAnalyseDependencyException() {
        val systemId = 1L
        val scannerVersion = "1.6.2"
        val errorMessage = "Error occurred during analysis"

        `when`(dependencyAnalysis.asyncAnalyse(systemId, scannerVersion)).thenThrow(RuntimeException(errorMessage))

        val response = analysisController.analyseDependency(systemId, scannerVersion)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(errorMessage, response.body)
    }

    @Test
    fun shouldPostAnalyseSuccessfully() {
        val systemId = 1L
        val scannerVersion = "1.6.2"

        val response = analysisController.postAnalyse(systemId, scannerVersion)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("", response.body)
    }

    @Test
    fun shouldHandlePostAnalyseException() {
        val systemId = 1L
        val scannerVersion = "1.6.2"
        val errorMessage = "Error occurred during post analysis"

        `when`(dependencyAnalysis.postAnalyse(systemId, scannerVersion)).thenThrow(RuntimeException(errorMessage))

        val response = analysisController.postAnalyse(systemId, scannerVersion)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(errorMessage, response.body)
    }

    @Test
    fun shouldCancelAnalyseDependencySuccessfully() {
        val systemId = 1L

        val response = analysisController.cancelAnalyseDependency(systemId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("", response.body)
    }

    @Test
    fun shouldHandleCancelAnalyseDependencyException() {
        val systemId = 1L
        val errorMessage = "Error occurred during cancelling analysis"

        `when`(dependencyAnalysis.cancelAnalyse(systemId)).thenThrow(RuntimeException(errorMessage))

        val response = analysisController.cancelAnalyseDependency(systemId)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(errorMessage, response.body)
    }
}
