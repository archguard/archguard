package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.repository.SizingRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class SizingService(val sizingRepository: SizingRepository) {

    fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): MethodSizingListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val methodLinesCount = sizingRepository.getMethodSizingAboveThresholdCount(systemId, threshold)
        val methodLinesAboveThreshold = sizingRepository.getMethodSizingAboveThreshold(systemId, threshold, limit, offset)
        return MethodSizingListDto(methodLinesAboveThreshold, methodLinesCount, offset / limit + 1)
    }

    fun getClassLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): ClassSizingListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val classLinesCount = sizingRepository.getClassSizingAboveThresholdCount(systemId, threshold)
        val classLinesAboveThreshold = sizingRepository.getClassSizingAboveThreshold(systemId, threshold, limit, offset)
        return ClassSizingListDto(classLinesAboveThreshold, classLinesCount, offset / limit + 1)
    }


}