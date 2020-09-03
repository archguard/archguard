package com.thoughtworks.archguard.report_bak.domain.service

import com.thoughtworks.archguard.report_bak.domain.repository.CodeLineRepository
import org.springframework.stereotype.Service

@Service
class CodeLineService(val codeLineRepository: CodeLineRepository) {

    fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): MethodLinesDto {
        val methodLinesCount = codeLineRepository.getMethodLinesAboveThresholdCount(systemId, threshold)

        if (limit == 0L) {
            val methodLinesAboveThreshold = codeLineRepository.getMethodLinesAboveThreshold(systemId, threshold)
            return MethodLinesDto(methodLinesAboveThreshold, methodLinesCount, 0)
        }
        val methodLinesAboveThreshold = codeLineRepository.getMethodLinesAboveThreshold(systemId, threshold, limit, offset)
        return MethodLinesDto(methodLinesAboveThreshold, methodLinesCount, offset / limit)
    }


}