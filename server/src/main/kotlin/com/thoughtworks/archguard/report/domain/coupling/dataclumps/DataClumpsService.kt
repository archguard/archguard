package com.thoughtworks.archguard.report.domain.coupling.dataclumps

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdKey
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.springframework.stereotype.Service

@Service
class DataClumpsService(
    val thresholdSuiteService: ThresholdSuiteService,
    val dataClumpsRepository: DataClumpsRepository
) {

    fun getClassDataClumpsWithTotalCount(systemId: Long, limit: Long, offset: Long): Triple<List<ClassDataClump>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)
        val lcoM4AboveThresholdCount = dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, threshold)
        val lcoM4AboveThresholdList = dataClumpsRepository.getLCOM4AboveThresholdList(systemId, threshold, limit, offset)
        return Triple(lcoM4AboveThresholdList, lcoM4AboveThresholdCount, threshold)
    }

    fun getDataClumpReport(systemId: Long): Map<BadSmellType, Long> {
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_DATA_CLUMPS)
        return mapOf((BadSmellType.DATACLUMPS to dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, threshold)))
    }
}
