package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.ClassCouplingRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class HubService(val classCouplingRepository: ClassCouplingRepository) {
    fun getClassHubListAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): ClassHubListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val classesCount = classCouplingRepository.getCouplingAboveThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        val classesAboveThreshold = classCouplingRepository.getCouplingAboveThreshold(systemId, classFanInThreshold, classFanOutThreshold, offset, limit, orderByFanIn)
        return ClassHubListDto(classesAboveThreshold, classesCount, offset / limit + 1)
    }

}
