package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class HubService(val couplingRepository: CouplingRepository) {
    fun getClassHubListAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): ClassHubListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val classesCount = couplingRepository.getCouplingCount(systemId, classFanInThreshold, classFanOutThreshold)
        val classesAboveThreshold = couplingRepository.getCoupling(systemId, classFanInThreshold, classFanOutThreshold, limit, offset, orderByFanIn)
        return ClassHubListDto(classesAboveThreshold, classesCount, offset / limit + 1)
    }

}
