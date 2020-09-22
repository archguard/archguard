package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.MethodCouplingRepository
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class HubService(val classCouplingRepository: ClassCouplingRepository, val methodCouplingRepository: MethodCouplingRepository) {
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

    fun getMethodHubListAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): MethodHubListDto {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
        val count = methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodFanInThreshold, methodFanOutThreshold)
        val methodsAboveThreshold = methodCouplingRepository.getCouplingAboveThreshold(systemId, methodFanInThreshold, methodFanOutThreshold, offset, limit, orderByFanIn)
        return MethodHubListDto(methodsAboveThreshold, count, offset / limit + 1)
    }

}
