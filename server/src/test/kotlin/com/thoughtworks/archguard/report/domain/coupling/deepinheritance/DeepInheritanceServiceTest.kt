package com.thoughtworks.archguard.report.domain.coupling.deepinheritance;

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.archguard.smell.BadSmellType
import org.archguard.threshold.ThresholdKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DeepInheritanceServiceTest {

    private val thresholdSuiteService = mock(ThresholdSuiteService::class.java)
    private val deepInheritanceRepository = mock(DeepInheritanceRepository::class.java)
    private val deepInheritanceService = DeepInheritanceService(thresholdSuiteService, deepInheritanceRepository)

    @Test
    fun should_getDeepInheritanceWithTotalCount() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val threshold = 5
        val ditAboveThresholdCount = 20L
        val ditAboveThresholdList = listOf(
            DeepInheritance("1", systemId, "module1", "com.example.module1.package1", "Class1", 2),
            DeepInheritance("2", systemId, "module1", "com.example.module1.package1", "Class2", 3),
        )
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DEEP_INHERITANCE)).thenReturn(threshold)
        `when`(deepInheritanceRepository.getDitAboveThresholdCount(systemId, threshold)).thenReturn(ditAboveThresholdCount)
        `when`(deepInheritanceRepository.getDitAboveThresholdList(systemId, threshold, limit, offset)).thenReturn(ditAboveThresholdList)

        // when
        val result = deepInheritanceService.getDeepInheritanceWithTotalCount(systemId, limit, offset)

        // then
        assertEquals(ditAboveThresholdList, result.first)
        assertEquals(ditAboveThresholdCount, result.second)
        assertEquals(threshold, result.third)
    }

    @Test
    fun should_getDeepInheritanceReport() {
        // given
        val systemId = 1L
        val threshold = 5
        val ditAboveThresholdCount = 20L
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DEEP_INHERITANCE)).thenReturn(threshold)
        `when`(deepInheritanceRepository.getDitAboveThresholdCount(systemId, threshold)).thenReturn(ditAboveThresholdCount)

        // when
        val result = deepInheritanceService.getDeepInheritanceReport(systemId)

        // then
        assertEquals(mapOf(BadSmellType.DEEPINHERITANCE to ditAboveThresholdCount), result)
    }
}
