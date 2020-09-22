package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import org.springframework.stereotype.Service

@Service
class SizingService(val sizingRepository: SizingRepository) {
    fun getModulePackageCountSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ModulesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingListAbovePackageCountThreshold(systemId, threshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getModuleSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ModulesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, threshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageClassCountSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): PackagesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingListAboveClassCountThreshold(systemId, threshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): PackagesSizingListDto {
        validPagingParam(limit, offset)
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, threshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getMethodSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): MethodSizingListDto {
        validPagingParam(limit, offset)
        val methodLinesCount = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
        val methodLinesAboveThreshold = sizingRepository.getMethodSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return MethodSizingListDto(methodLinesAboveThreshold, methodLinesCount, offset / limit + 1)
    }

    fun getClassSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ClassSizingListWithLineDto {
        validPagingParam(limit, offset)
        val classLinesCount = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, threshold)
        val classLinesAboveThreshold = sizingRepository.getClassSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithLineDto(classLinesAboveThreshold, classLinesCount, offset / limit + 1)
    }

    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ClassSizingListWithMethodCountDto {
        validPagingParam(limit, offset)
        val classSizingListAboveMethodCountThresholdCount = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, threshold)
        val classSizingListAboveMethodCountThreshold = sizingRepository.getClassSizingListAboveMethodCountThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithMethodCountDto(classSizingListAboveMethodCountThreshold, classSizingListAboveMethodCountThresholdCount, offset / limit + 1)
    }


}
