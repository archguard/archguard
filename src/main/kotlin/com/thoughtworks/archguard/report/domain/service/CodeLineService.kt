package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.repository.CodeLineRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class CodeLineService(val codeLineRepository: CodeLineRepository) {

    fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): MethodLinesDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val methodLinesCount = codeLineRepository.getMethodLinesAboveThresholdCount(systemId, threshold)
        val methodLinesAboveThreshold = codeLineRepository.getMethodLinesAboveThreshold(systemId, threshold, limit, offset)
        return MethodLinesDto(methodLinesAboveThreshold, methodLinesCount, offset / limit)
    }


}