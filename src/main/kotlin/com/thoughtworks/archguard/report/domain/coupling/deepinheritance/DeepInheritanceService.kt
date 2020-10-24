package com.thoughtworks.archguard.report.domain.coupling.deepinheritance

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdKey
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.springframework.stereotype.Service

@Service
class DeepInheritanceService(val thresholdSuiteService: ThresholdSuiteService,
                             val deepInheritanceRepository: DeepInheritanceRepository) {

    fun getDeepInheritanceWithTotalCount(systemId: Long, limit: Long, offset: Long): Triple<List<DeepInheritance>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DEEP_INHERITANCE)
        val lcoM4AboveThresholdCount = deepInheritanceRepository
                .getDitAboveThresholdCount(systemId, threshold)
        val lcoM4AboveThresholdList = deepInheritanceRepository
                .getDitAboveThresholdList(systemId, threshold, limit, offset)
        return Triple(lcoM4AboveThresholdList, lcoM4AboveThresholdCount, threshold)
    }

    fun getDeepInheritanceReport(systemId: Long): Map<BadSmellType, Long> {
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DEEP_INHERITANCE)
        return mapOf((BadSmellType.DEEPINHERITANCE to
                deepInheritanceRepository.getDitAboveThresholdCount(systemId, threshold)))
    }
}