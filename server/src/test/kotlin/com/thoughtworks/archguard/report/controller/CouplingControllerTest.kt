package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.ClassCouplingAppService
import com.thoughtworks.archguard.report.controller.coupling.ClassDataClumpsListDto
import com.thoughtworks.archguard.report.controller.coupling.DeepInheritanceListDto
import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.ClassDataClump
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritance
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceService
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus

class CouplingControllerTest {
    private val dataClumpsService = mock(DataClumpsService::class.java)
    private val deepInheritanceService = mock(DeepInheritanceService::class.java)
    private val classCouplingRepository = mock(ClassCouplingRepository::class.java)
    private val classCouplingAppService = mock(ClassCouplingAppService::class.java)

    private val couplingController = CouplingController(
        dataClumpsService,
        deepInheritanceService,
        classCouplingRepository,
        classCouplingAppService
    )

    @Test
    fun shouldGetClassesDataClumpsWithTotalCount() {
        // Given
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val limit = 10L
        val offset = 0L
        val resultList = listOf<ClassDataClump>()
        val count = 5L
        val threshold = 3

        given(dataClumpsService.getClassDataClumpsWithTotalCount(systemId, limit, offset))
            .willReturn(Triple(resultList, count, threshold))

        // When
        val response = couplingController.getClassesDataClumpsWithTotalCount(systemId, filterSizing)

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(ClassDataClumpsListDto(resultList, count, 1, threshold))
    }

    @Test
    fun shouldGetDeepInheritanceWithTotalCount() {
        // Given
        val systemId = 1L
        val filterSizing = FilterSizingDto(1, 10, null, null, null, null)
        val limit = 10L
        val offset = 0L
        val resultList = listOf<DeepInheritance>()
        val count = 5L
        val threshold = 3

        given(deepInheritanceService.getDeepInheritanceWithTotalCount(systemId, limit, offset))
            .willReturn(Triple(resultList, count, threshold))

        // When
        val response = couplingController.getDeepInheritanceWithTotalCount(systemId, filterSizing)

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DeepInheritanceListDto(resultList, count, 1, threshold))
    }

    @Test
    fun shouldGetAllClassCouplingData() {
        // Given
        val systemId = 1L
        val classCouplingList = listOf<ClassCoupling>()

        given(classCouplingRepository.getAllCoupling(systemId))
            .willReturn(classCouplingList)

        // When
        val response = couplingController.getAllClassCouplingData(systemId)

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(classCouplingList)
    }

    @Test
    fun shouldGetAllClassCouplingDataWithQualityGate() {
        // Given
        val systemId = 1L
        val qualityGateName = "gate"
        val classCouplingList = listOf<ClassCoupling>()

        given(classCouplingAppService.getCouplingFilterByQualityGate(systemId, qualityGateName))
            .willReturn(classCouplingList)

        // When
        val response = couplingController.getAllClassCouplingData(systemId, qualityGateName)

        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(classCouplingList)
    }
}