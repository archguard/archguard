package com.thoughtworks.archguard.report.domain.coupling.dataclumps;

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsService
import org.archguard.smell.BadSmellType
import org.archguard.threshold.ThresholdKey

class DataClumpsServiceTest {

    @Test
    fun should_get_class_data_clumps_with_total_count() {
        // Given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val threshold = 5
        val lcoM4AboveThresholdCount = 20L
        val lcoM4AboveThresholdList = listOf(
            ClassDataClump(
                id = "1",
                systemId = 1,
                moduleName = "Module 1",
                packageName = "com.example",
                typeName = "ClassDataClump",
                lcom4 = 5
            )
        )
        val thresholdSuiteService = mock(ThresholdSuiteService::class.java)
        val dataClumpsRepository = mock(DataClumpsRepository::class.java)
        val dataClumpsService = DataClumpsService(thresholdSuiteService, dataClumpsRepository)

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)).thenReturn(threshold)
        `when`(dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, threshold)).thenReturn(lcoM4AboveThresholdCount)
        `when`(dataClumpsRepository.getLCOM4AboveThresholdList(systemId, threshold, limit, offset)).thenReturn(lcoM4AboveThresholdList)

        // When
        val result = dataClumpsService.getClassDataClumpsWithTotalCount(systemId, limit, offset)

        // Then
        assert(result.first == lcoM4AboveThresholdList)
        assert(result.second == lcoM4AboveThresholdCount)
        assert(result.third == threshold)
        verify(thresholdSuiteService).getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)
        verify(dataClumpsRepository).getLCOM4AboveThresholdCount(systemId, threshold)
        verify(dataClumpsRepository).getLCOM4AboveThresholdList(systemId, threshold, limit, offset)
    }

    @Test
    fun should_get_data_clump_report() {
        // Given
        val systemId = 1L
        val threshold = 5
        val lcoM4AboveThresholdCount = 20L
        val thresholdSuiteService = mock(ThresholdSuiteService::class.java)
        val dataClumpsRepository = mock(DataClumpsRepository::class.java)
        val dataClumpsService = DataClumpsService(thresholdSuiteService, dataClumpsRepository)

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)).thenReturn(threshold)
        `when`(dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, threshold)).thenReturn(lcoM4AboveThresholdCount)

        // When
        val result = dataClumpsService.getDataClumpReport(systemId)

        // Then
        assert(result[BadSmellType.DATACLUMPS] == lcoM4AboveThresholdCount)
        verify(thresholdSuiteService).getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)
        verify(dataClumpsRepository).getLCOM4AboveThresholdCount(systemId, threshold)
    }
}
