package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SizingService(val sizingRepository: SizingRepository) {
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
    private val moduleClassCountSizingThreshold: Int = 0

    fun getModulePackageCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = moduleClassCountSizingThreshold
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingListAbovePackageCountThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getModuleSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ModuleSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = moduleSizingLineThreshold
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getModuleSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getPackageClassCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = packageClassCountSizingThreshold
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getPackageSizingListAboveClassCountThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getPackageSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<PackageSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = packageSizingLineThreshold
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getPackageSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getMethodSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<MethodSizing>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = methodSizingThreshold
        val count = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getMethodSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }


    fun getClassSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ClassSizingWithLine>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = classSizingThreshold
        val count = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, threshold)
        val data = sizingRepository.getClassSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, limit: Long, offset: Long): Triple<List<ClassSizingWithMethodCount>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = classMethodCountSizingThreshold
        val count = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, threshold)
        val data = sizingRepository.getClassSizingListAboveMethodCountThreshold(systemId, threshold, limit, offset)
        return Triple(data, count, threshold)
    }

    fun getMethodSizingSmellCount(systemId: Long): Long {
        return sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, methodSizingThreshold)
    }

    fun getClassSizingSmellCount(systemId: Long): Long {
        return sizingRepository.getClassSizingAboveLineThresholdCount(systemId, classSizingThreshold) +
                sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, classMethodCountSizingThreshold)
    }

    fun getModuleSizingSmellCount(systemId: Long): Long {
        return sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, moduleSizingLineThreshold) +
                sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, moduleClassCountSizingThreshold)
    }

    fun getPackageSizingSmellCount(systemId: Long): Long {
        return sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, packageSizingLineThreshold) +
                sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, packageClassCountSizingThreshold)
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
