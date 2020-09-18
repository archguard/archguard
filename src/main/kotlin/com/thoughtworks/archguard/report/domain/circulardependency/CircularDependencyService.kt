package com.thoughtworks.archguard.report.domain.circulardependency

import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.springframework.stereotype.Service

@Service
class CircularDependencyService(val circularDependencyRepository: CircularDependencyRepository) {
    fun getCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long, type: CircularDependencyType): CircularDependencyStringListDto {
        validPagingParam(limit, offset)
        val circularDependencyCount = circularDependencyRepository.getCircularDependencyCount(systemId, type)
        val circularDependencyList = circularDependencyRepository.getCircularDependency(systemId, type, limit, offset)
        return CircularDependencyStringListDto(circularDependencyList,
                circularDependencyCount,
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