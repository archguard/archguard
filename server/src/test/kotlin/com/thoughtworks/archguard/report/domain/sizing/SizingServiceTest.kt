package com.thoughtworks.archguard.report.domain.sizing;

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.archguard.threshold.ThresholdKey
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

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
}
