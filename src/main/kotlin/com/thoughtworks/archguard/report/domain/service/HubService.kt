package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.repository.HubRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class HubService(val hubRepository: HubRepository) {
    fun getClassHubListAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): ClassHubListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val classesCount = hubRepository.getClassAboveHubThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        val classesAboveThreshold = hubRepository.getClassListAboveHubThreshold(systemId, classFanInThreshold, classFanOutThreshold, limit, offset, orderByFanIn)
        return ClassHubListDto(classesAboveThreshold, classesCount, offset / limit + 1)
    }

}
