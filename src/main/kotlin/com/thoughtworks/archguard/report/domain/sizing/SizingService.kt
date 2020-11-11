package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdKey
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import com.thoughtworks.archguard.report.infrastructure.FilterSizingPO.Companion.fromFilterSizing
import org.springframework.stereotype.Service

@Service
class SizingService(val thresholdSuiteService: ThresholdSuiteService,
                    val sizingRepository: SizingRepository) {

    fun getModulePackageCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT)
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingListAbovePackageCountThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getModulePackageCountSizingAboveThresholdByFilterSizing(systemId: Long, filter: FilterSizing): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT)
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingListAbovePackageCountThresholdByFilterSizing(systemId, threshold, filter.limit, filter.offset, filter.module)
        return Triple(data, count, threshold)
    }

    fun getModuleSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_LOC)
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getModuleSizingListAboveLineThresholdByFilterSizing(systemId: Long, filter: FilterSizing): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_LOC)
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingAboveLineThresholdByFilterSizing(systemId, threshold, filter.limit, filter.offset, filter.module)
        return Triple(data, count, threshold)
    }

    fun getPackageClassCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_CLASS_COUNT)
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getPackageSizingListAboveClassCountThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }


    fun getPackageClassCountSizingAboveThresholdByFilterSizing(systemId: Long, filter: FilterSizing): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_CLASS_COUNT)
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold, fromFilterSizing(filter))
        val data = sizingRepository.getPackageSizingListAboveClassCountThresholdByFilterSizing(systemId, threshold, fromFilterSizing(filter))
        return Triple(data, count, threshold)
    }

    fun getPackageSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_LOC)
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getPackageSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getPackageSizingListAboveLineThresholdByFilterSizing(systemId: Long, filter: FilterSizing): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_LOC)
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, threshold, fromFilterSizing(filter))
        val data = sizingRepository.getPackageSizingAboveLineThresholdByFilterSizing(systemId, threshold, fromFilterSizing(filter))
        return Triple(data, count, threshold)
    }

    fun getMethodSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<MethodSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_METHOD_BY_LOC)
        val count = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getMethodSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getMethodSizingListAboveLineThresholdByRequestSizing(systemId: Long, filter: FilterSizing): Triple<List<MethodSizing>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_METHOD_BY_LOC)
        val count = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold, fromFilterSizing(filter))
        val data = sizingRepository.getMethodSizingAboveLineThresholdByFilterSizing(systemId, threshold, fromFilterSizing(filter))
        return Triple(data, count, threshold)
    }


    fun getClassSizingListAboveLineThreshold(systemId: Long, filter: FilterSizing): Triple<List<ClassSizingWithLine>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_LOC)
        val count = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, threshold, fromFilterSizing(filter))
        val data = sizingRepository.getClassSizingAboveLineThresholdByRequestSizing(systemId, threshold, fromFilterSizing(filter))
        return Triple(data, count, threshold)
    }

    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, filter: FilterSizing): Triple<List<ClassSizingWithMethodCount>, Long, Int> {
        validPagingParam(filter.limit, filter.offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_FUNC_COUNT)
        val count = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, threshold, fromFilterSizing(filter))
        val data = sizingRepository.getClassSizingListAboveMethodCountThresholdByRequestSizing(systemId, threshold, fromFilterSizing(filter))
        return Triple(data, count, threshold)
    }


    fun getMethodSizingSmellCount(systemId: Long): Long {
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_METHOD_BY_LOC)
        return sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
    }

    fun getClassSizingSmellCount(systemId: Long): Long {
        val locThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_LOC)
        val methodCountThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_CLASS_BY_FUNC_COUNT)
        return sizingRepository.getClassSizingAboveLineThresholdCount(systemId, locThreshold) +
                sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, methodCountThreshold)
    }

    fun getModuleSizingSmellCount(systemId: Long): Long {
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT)
        val locThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_MODULE_BY_LOC)
        return sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, locThreshold) +
                sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
    }

    fun getPackageSizingSmellCount(systemId: Long): Long {
        val locThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_LOC)
        val clzCountThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.SIZING_PACKAGE_BY_CLASS_COUNT)
        return sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, locThreshold) +
                sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, clzCountThreshold)
    }

    fun getSizingReport(systemId: Long): Map<BadSmellType, Long> {
        val methodSizing = getMethodSizingSmellCount(systemId)
        val classSizing = getClassSizingSmellCount(systemId)
        val packageSizing = getPackageSizingSmellCount(systemId)
        val moduleSizing = getModuleSizingSmellCount(systemId)

        return mapOf((BadSmellType.SIZINGMETHOD to methodSizing),
                (BadSmellType.SIZINGCLASS to classSizing),
                (BadSmellType.SIZINGPACKAGE to packageSizing),
                (BadSmellType.SIZINGMODULES to moduleSizing))
    }

}
