package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.MethodLine
import com.thoughtworks.archguard.report.domain.repository.CodeLineRepository
import org.springframework.stereotype.Service

@Service
class CodeLineService(val codeLineRepository: CodeLineRepository) {
    fun getMethodLinesAboveThreshold(systemId: Long, threshold: Int): List<MethodLine> {
        return codeLineRepository.getMethodLinesAboveThreshold(systemId, threshold).sortedByDescending { it.lines }
    }

}