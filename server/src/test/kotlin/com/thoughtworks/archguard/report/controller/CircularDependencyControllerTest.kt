package com.thoughtworks.archguard.report.controller;

import com.thoughtworks.archguard.report.controller.coupling.CircularDependencyListDto
import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependency
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.models.MethodVO
import com.thoughtworks.archguard.report.domain.models.ModuleVO
import com.thoughtworks.archguard.report.domain.models.PackageVO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity

class CircularDependencyControllerTest {

    private val circularDependencyService = mock(CircularDependencyService::class.java)
    private val circularDependencyController = CircularDependencyController(circularDependencyService)

    @Test
    fun shouldGetModuleCircularDependencyWithTotalCount() {
        // given
        val systemId = 1L
        val filterSizing = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "Module1",
            className = "Class1",
            packageName = "com.example",
            name = "Test"
        )
        val limit = 10L
        val offset = 0L
        val resultList = listOf<CircularDependency<ModuleVO>>()
        val count = 0L
        val threshold = 0

        `when`(circularDependencyService.getModuleCircularDependencyWithTotalCount(systemId, limit, offset))
            .thenReturn(Triple(resultList, count, threshold))

        // when
        val response = circularDependencyController.getModuleCircularDependencyWithTotalCount(systemId, filterSizing)

        // then
        assertEquals(ResponseEntity.ok(CircularDependencyListDto(resultList, count, 1, threshold)), response)
    }

    @Test
    fun shouldGetPackageCircularDependencyWithTotalCount() {
        // given
        val systemId = 1L
        val filterSizing = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "Module1",
            className = "Class1",
            packageName = "com.example",
            name = "Test"
        )
        val limit = 10L
        val offset = 0L
        val resultList = listOf<CircularDependency<PackageVO>>()
        val count = 0L
        val threshold = 0

        `when`(circularDependencyService.getPackageCircularDependencyWithTotalCount(systemId, limit, offset))
            .thenReturn(Triple(resultList, count, threshold))

        // when
        val response = circularDependencyController.getPackageCircularDependencyWithTotalCount(systemId, filterSizing)

        // then
        assertEquals(ResponseEntity.ok(CircularDependencyListDto(resultList, count, 1, threshold)), response)
    }

    @Test
    fun shouldGetClassCircularDependencyWithTotalCount() {
        // given
        val systemId = 1L
        val filterSizing = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "Module1",
            className = "Class1",
            packageName = "com.example",
            name = "Test"
        )
        val limit = 10L
        val offset = 0L
        val resultList = listOf<CircularDependency<ClassVO>>()
        val count = 0L
        val threshold = 0

        `when`(circularDependencyService.getClassCircularDependencyWithTotalCount(systemId, limit, offset))
            .thenReturn(Triple(resultList, count, threshold))

        // when
        val response = circularDependencyController.getClassCircularDependencyWithTotalCount(systemId, filterSizing)

        // then
        assertEquals(ResponseEntity.ok(CircularDependencyListDto(resultList, count, 1, threshold)), response)
    }

    @Test
    fun shouldGetMethodCircularDependencyWithTotalCount() {
        // given
        val systemId = 1L
        val filterSizing = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "Module1",
            className = "Class1",
            packageName = "com.example",
            name = "Test"
        )
        val limit = 10L
        val offset = 0L
        val resultList = listOf<CircularDependency<MethodVO>>()
        val count = 0L
        val threshold = 0

        `when`(circularDependencyService.getMethodCircularDependencyWithTotalCount(systemId, limit, offset))
            .thenReturn(Triple(resultList, count, threshold))

        // when
        val response = circularDependencyController.getMethodCircularDependencyWithTotalCount(systemId, filterSizing)

        // then
        assertEquals(ResponseEntity.ok(CircularDependencyListDto(resultList, count, 1, threshold)), response)
    }
}
