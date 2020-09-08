package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.hub.HubRepository
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OverviewService(val sizingRepository: SizingRepository,
                      val hubRepository: HubRepository,
                      val dataClumpsRepository: DataClumpsRepository,
                      val deepInheritanceRepository: DeepInheritanceRepository) {
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

    @Value("\${threshold.class.fanIn}")
    private val classFanInThreshold: Int = 0

    @Value("\${threshold.class.fanOut}")
    private val classFanOutThreshold: Int = 0

    @Value("\${threshold.dataclumps.lcom4}")
    private val dataClumpsLCOM4Threshold: Int = 1

    @Value("\${threshold.deep-inheritance.dit}")
    private val deepInheritanceDitThreshold: Int = 1

    fun getOverview(systemId: Long): OverviewDto {
        val list = mutableListOf<OverviewItem>()
        list.add(this.getMethodOverSizingOverview(systemId))
        list.add(this.getClassOverSizingOverview(systemId))
        list.add(this.getPackageOverSizingOverview(systemId))
        list.add(this.getModuleOverSizingOverview(systemId))
        list.add(this.getClassHubOverview(systemId))
        list.add(this.getDataClumpsOverview(systemId))
        list.add(this.getDeepInheritanceOverview(systemId))
        list.add(this.getCycleDependency(systemId))
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

    private fun getClassHubOverview(systemId: Long): OverviewItem {
        val count = hubRepository.getClassAboveHubThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        return OverviewItem(BadSmell.COUPLING_CLASS_HUB, BadSmellCategory.COUPLING, count)
    }

    private fun getDataClumpsOverview(systemId: Long): OverviewItem {
        val count = dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, dataClumpsLCOM4Threshold)
        return OverviewItem(BadSmell.COUPLING_DATA_CLUMPS, BadSmellCategory.COUPLING, count)
    }

    private fun getDeepInheritanceOverview(systemId: Long): OverviewItem {
        val count = deepInheritanceRepository.getDitAboveThresholdCount(systemId, deepInheritanceDitThreshold)
        return OverviewItem(BadSmell.COUPLING_DEEP_INHERITANCE, BadSmellCategory.COUPLING, count)
    }

    private fun getCycleDependency(systemId: Long): OverviewItem {
        //todo
        val count: Long = 0
        return OverviewItem(BadSmell.COUPLING_CYCLE_DEPENDENCY, BadSmellCategory.COUPLING, count)
    }


}
