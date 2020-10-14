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

    fun getModulePackageCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): ModulesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, moduleClassCountSizingThreshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingListAbovePackageCountThreshold(systemId, moduleClassCountSizingThreshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getModuleSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): ModulesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, moduleSizingLineThreshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingAboveLineThreshold(systemId, moduleSizingLineThreshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageClassCountSizingAboveThreshold(systemId: Long, limit: Long, offset: Long): PackagesSizingListDto {
        validPagingParam(limit, offset)
        val threshold = packageClassCountSizingThreshold
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingListAboveClassCountThreshold(systemId, threshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): PackagesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, packageSizingLineThreshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingAboveLineThreshold(systemId, packageSizingLineThreshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getMethodSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): Pair<List<MethodSizing>, Long> {
        validPagingParam(limit, offset)
        val threshold = methodSizingThreshold
        val methodLinesCount = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
        val methodLinesAboveThreshold = sizingRepository.getMethodSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return (methodLinesAboveThreshold to methodLinesCount)
    }

    fun getClassSizingListAboveLineThreshold(systemId: Long, limit: Long, offset: Long): ClassSizingListWithLineDto {
        validPagingParam(limit, offset)
        val threshold = classSizingThreshold
        val classLinesCount = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, threshold)
        val classLinesAboveThreshold = sizingRepository.getClassSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithLineDto(classLinesAboveThreshold, classLinesCount, offset / limit + 1)
    }

    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, limit: Long, offset: Long): ClassSizingListWithMethodCountDto {
        validPagingParam(limit, offset)
        val threshold = classMethodCountSizingThreshold
        val classSizingListAboveMethodCountThresholdCount = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, threshold)
        val classSizingListAboveMethodCountThreshold = sizingRepository.getClassSizingListAboveMethodCountThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithMethodCountDto(classSizingListAboveMethodCountThreshold, classSizingListAboveMethodCountThresholdCount, offset / limit + 1)
    }

    fun getSizingReport(systemId: Long): Map<BadSmellType, Long> {
        val methodSizing = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, methodSizingThreshold)

        val classSizing = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, classSizingThreshold) +
                sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, classMethodCountSizingThreshold)

        val packageSizing = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, packageSizingLineThreshold) +
                sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, packageClassCountSizingThreshold)

        val moduleSizing = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, moduleSizingLineThreshold) +
                sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, moduleClassCountSizingThreshold)

        return mapOf((BadSmellType.SIZINGMETHOD to methodSizing),
                (BadSmellType.SIZINGCLASS to classSizing),
                (BadSmellType.SIZINGPACKAGE to packageSizing),
                (BadSmellType.SIZINGMODULES to moduleSizing))
    }

}
