package com.thoughtworks.archguard.report.domain.sizing;

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.archguard.smell.BadSmellType
import org.archguard.threshold.ThresholdKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class SizingServiceTest {
    private val thresholdSuiteService: ThresholdSuiteService = Mockito.mock(ThresholdSuiteService::class.java)
    private val sizingRepository: SizingRepository = Mockito.mock(SizingRepository::class.java)
    private val sizingService = SizingService(thresholdSuiteService, sizingRepository)

    @Test
    fun `should return module sizing list above package count threshold by filter sizing`() {
        // given
        val systemId = 1L
        val filter = FilterSizing(
            limit = 10,
            offset = 0,
            module = "module",
            className = "className",
            packageName = "packageName",
            name = "name"
        )
        val count = 3L
        val moduleSizingList = listOf(
            ModuleSizing(
                id = "mockId",
                systemId = 1234567890,
                moduleName = "mockModuleName",
                packageCount = 10,
                classCount = 20,
                lines = 1000
            )
        )
        val threshold = 100

        `when`(
            thresholdSuiteService.getThresholdValue(
                systemId,
                ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT
            )
        ).thenReturn(threshold)
        `when`(
            sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(
                systemId,
                threshold,
                filter.module
            )
        ).thenReturn(count)
        `when`(
            sizingRepository.getModuleSizingListAbovePackageCountThresholdByFilterSizing(
                systemId,
                threshold,
                filter.limit,
                filter.offset,
                filter.module
            )
        ).thenReturn(moduleSizingList)

        // when
        val result = sizingService.getModulePackageCountSizingAboveThresholdByFilterSizing(systemId, filter)

        // then
        verify(thresholdSuiteService).getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT)
        verify(sizingRepository).getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold, filter.module)
        verify(sizingRepository).getModuleSizingListAbovePackageCountThresholdByFilterSizing(
            systemId,
            threshold,
            filter.limit,
            filter.offset,
            filter.module
        )
        assert(result.first == moduleSizingList)
        assert(result.second == count)
        assert(result.third == threshold)
    }

    @Test
    fun shouldGetMethodSizingSmellCount() {
        val systemId = 1L
        val threshold = 10
        val expectedCount = 5L

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_METHOD_BY_LOC)).thenReturn(
            threshold
        )
        `when`(sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)).thenReturn(expectedCount)

        val actualCount = sizingService.getMethodSizingSmellCount(systemId)

        assertEquals(expectedCount, actualCount)
    }


    @Test
    fun shouldGetClassSizingSmellCount() {
        val systemId = 1L
        val locThreshold = 10
        val methodCountThreshold = 20
        val expectedCount = 5L

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_LOC)).thenReturn(
            locThreshold
        )
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_FUNC_COUNT)).thenReturn(
            methodCountThreshold
        )
        `when`(sizingRepository.getClassSizingAboveLineThresholdCount(systemId, locThreshold)).thenReturn(expectedCount)
        `when`(
            sizingRepository.getClassSizingListAboveMethodCountThresholdCount(
                systemId,
                methodCountThreshold
            )
        ).thenReturn(expectedCount)

        val actualCount = sizingService.getClassSizingSmellCount(systemId)

        assertEquals(expectedCount * 2, actualCount)
    }

    @Test
    fun shouldGetModuleSizingSmellCount() {
        val systemId = 1L
        val threshold = 10
        val locThreshold = 20
        val expectedCount = 5L

        `when`(
            thresholdSuiteService.getThresholdValue(
                systemId,
                ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT
            )
        ).thenReturn(threshold)
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_LOC)).thenReturn(
            locThreshold
        )
        `when`(
            sizingRepository.getModuleSizingAboveLineThresholdCount(
                systemId,
                locThreshold
            )
        ).thenReturn(expectedCount)
        `when`(sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)).thenReturn(
            expectedCount
        )

        val actualCount = sizingService.getModuleSizingSmellCount(systemId)

        assertEquals(expectedCount * 2, actualCount)
    }

    @Test
    fun shouldGetPackageSizingSmellCount() {
        val systemId = 1L
        val locThreshold = 10
        val clzCountThreshold = 20
        val expectedCount = 5L

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_LOC)).thenReturn(
            locThreshold
        )
        `when`(
            thresholdSuiteService.getThresholdValue(
                systemId,
                ThresholdKey.SIZING_PACKAGE_BY_CLASS_COUNT
            )
        ).thenReturn(clzCountThreshold)
        `when`(sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, locThreshold)).thenReturn(
            expectedCount
        )
        `when`(
            sizingRepository.getPackageSizingListAboveClassCountThresholdCount(
                systemId,
                clzCountThreshold
            )
        ).thenReturn(expectedCount)

        val actualCount = sizingService.getPackageSizingSmellCount(systemId)

        assertEquals(expectedCount * 2, actualCount)
    }

    @Test
    fun shouldGetSizingReport() {
        val systemId = 1L
        val methodSizingCount = 5L
        val classSizingCount = 10L
        val packageSizingCount = 15L
        val moduleSizingCount = 20L

        `when`(sizingService.getMethodSizingSmellCount(systemId)).thenReturn(methodSizingCount)
        `when`(sizingService.getClassSizingSmellCount(systemId)).thenReturn(classSizingCount)
        `when`(sizingService.getPackageSizingSmellCount(systemId)).thenReturn(packageSizingCount)
        `when`(sizingService.getModuleSizingSmellCount(systemId)).thenReturn(moduleSizingCount)

        val expectedReport = mapOf(
            BadSmellType.SIZINGMETHOD to methodSizingCount,
            BadSmellType.SIZINGCLASS to classSizingCount,
            BadSmellType.SIZINGPACKAGE to packageSizingCount,
            BadSmellType.SIZINGMODULES to moduleSizingCount
        )

        val actualReport = sizingService.getSizingReport(systemId)

        assertEquals(expectedReport, actualReport)
    }
}
