package com.thoughtworks.archguard.report.domain.deepinheritance

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DeepInheritanceService(val deepInheritanceRepository: DeepInheritanceRepository) {

    @Value("\${threshold.deep-inheritance.dit}")
    private val deepInheritanceDitThreshold: Int = 1

    fun getDeepInheritanceWithTotalCount(systemId: Long, limit: Long, offset: Long): DeepInheritanceListDto {
        validPagingParam(limit, offset)
        val lcoM4AboveThresholdCount = deepInheritanceRepository
                .getDitAboveThresholdCount(systemId, deepInheritanceDitThreshold)
        val lcoM4AboveThresholdList = deepInheritanceRepository
                .getDitAboveThresholdList(systemId, deepInheritanceDitThreshold, limit, offset)
        return DeepInheritanceListDto(lcoM4AboveThresholdList,
                lcoM4AboveThresholdCount,
                offset / limit + 1)
    }
}