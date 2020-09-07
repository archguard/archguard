package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class SizingService(val sizingRepository: SizingRepository) {
    fun getModulePackageCountSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ModulesSizingListDto {
        checkLimitAndOffset(limit, offset)
        val count = sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, threshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingListAbovePackageCountThreshold(systemId, threshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getModuleSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ModulesSizingListDto {
        checkLimitAndOffset(limit, offset)
        val count = sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, threshold)
        val moduleLinesAboveThreshold = sizingRepository.getModuleSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return ModulesSizingListDto(moduleLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageClassCountSizingAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): PackagesSizingListDto {
        checkLimitAndOffset(limit, offset)
        val count = sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, threshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingListAboveClassCountThreshold(systemId, threshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): PackagesSizingListDto {
        checkLimitAndOffset(limit, offset)
        val count = sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, threshold)
        val packageLinesAboveThreshold = sizingRepository.getPackageSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return PackagesSizingListDto(packageLinesAboveThreshold, count, offset / limit + 1)
    }

    fun getMethodSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): MethodSizingListDto {
        checkLimitAndOffset(limit, offset)
        val methodLinesCount = sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, threshold)
        val methodLinesAboveThreshold = sizingRepository.getMethodSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return MethodSizingListDto(methodLinesAboveThreshold, methodLinesCount, offset / limit + 1)
    }

    fun getClassSizingListAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ClassSizingListWithLineDto {
        checkLimitAndOffset(limit, offset)
        val classLinesCount = sizingRepository.getClassSizingAboveLineThresholdCount(systemId, threshold)
        val classLinesAboveThreshold = sizingRepository.getClassSizingAboveLineThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithLineDto(classLinesAboveThreshold, classLinesCount, offset / limit + 1)
    }

    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ClassSizingListWithMethodCountDto {
        checkLimitAndOffset(limit, offset)
        val classSizingListAboveMethodCountThresholdCount = sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, threshold)
        val classSizingListAboveMethodCountThreshold = sizingRepository.getClassSizingListAboveMethodCountThreshold(systemId, threshold, limit, offset)
        return ClassSizingListWithMethodCountDto(classSizingListAboveMethodCountThreshold, classSizingListAboveMethodCountThresholdCount, offset / limit + 1)
    }

    private fun checkLimitAndOffset(limit: Long, offset: Long) {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
    }


}
