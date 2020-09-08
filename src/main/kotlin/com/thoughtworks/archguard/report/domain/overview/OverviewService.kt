package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OverviewService(val sizingRepository: SizingRepository) {
    @Value("\${threshold.method.line}")
    private val methodSizingThreshold: Int = 0

    @Value("\${threshold.class.line}")
    private val classSizingThreshold: Int = 0

    @Value("\${threshold.class.method.count}")
    private val classMethodCountSizingThreshold: Int = 0


    @Value("\${threshold.package.line}")
    private val packageSizingLineThreshold: Int = 0

    @Value("\${threshold.package.class.count}")
    private val packageClassCountSizingThreshold: Int = 0

    @Value("\${threshold.module.line}")
    private val moduleSizingLineThreshold: Int = 0

    @Value("\${threshold.module.package.count}")
    private val modulePackageCountSizingThreshold: Int = 0

    fun getOverview(systemId: Long): OverviewDto {
        val list = mutableListOf<OverviewItem>()
        list.add(this.getMethodOverSizingOverview(systemId))
        list.add(this.getClassOverSizingOverview(systemId))
        list.add(this.getPackageOverSizingOverview(systemId))
        list.add(this.getModuleOverSizingOverview(systemId))
        return OverviewDto(list)
    }

    private fun getMethodOverSizingOverview(systemId: Long): OverviewItem {
        val count = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, methodSizingThreshold)
        return OverviewItem(BadSmell.METHOD_OVER_SIZING, BadSmellCategory.OVER_SIZING, count)
    }

    private fun getClassOverSizingOverview(systemId: Long): OverviewItem {
        val methodCount = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, classSizingThreshold);
        val lineCount = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, classSizingThreshold)

        return OverviewItem(BadSmell.CLASS_OVER_SIZING, BadSmellCategory.OVER_SIZING, methodCount + lineCount)
    }

    private fun getPackageOverSizingOverview(systemId: Long): OverviewItem {
        val classCount = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, packageClassCountSizingThreshold)
        val lineCount = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, packageSizingLineThreshold)
        return OverviewItem(BadSmell.PACKAGE_OVER_SIZING, BadSmellCategory.OVER_SIZING, classCount + lineCount)
    }

    private fun getModuleOverSizingOverview(systemId: Long): OverviewItem {
        val packageCount = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, modulePackageCountSizingThreshold)
        val lineCount = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, moduleSizingLineThreshold)
        return OverviewItem(BadSmell.MODULE_OVER_SIZING, BadSmellCategory.OVER_SIZING, packageCount + lineCount)
    }
}
