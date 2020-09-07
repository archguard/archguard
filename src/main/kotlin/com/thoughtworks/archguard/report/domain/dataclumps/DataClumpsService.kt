package com.thoughtworks.archguard.report.domain.dataclumps

import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DataClumpsService(val dataClumpsRepository: DataClumpsRepository) {

    @Value("\${threshold.dataclumps.lcom4}")
    private val dataClumpsLCOM4Threshold: Int = 1

    fun getClassDataClumpsWithTotalCount(systemId: Long, limit: Long, offset: Long): ClassDataClumpsListDto {
        validPagingParam(limit, offset)
        val lcoM4AboveThresholdCount = dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, dataClumpsLCOM4Threshold);
        val lcoM4AboveThresholdList = dataClumpsRepository.getLCOM4AboveThresholdList(systemId, dataClumpsLCOM4Threshold, limit, offset);
        return ClassDataClumpsListDto(lcoM4AboveThresholdList,
                lcoM4AboveThresholdCount,
                offset / limit + 1)
    }

    private fun validPagingParam(limit: Long, offset: Long) {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
    }
}