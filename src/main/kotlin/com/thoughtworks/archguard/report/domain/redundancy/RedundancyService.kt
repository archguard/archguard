package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.module.ClassVO
import org.springframework.stereotype.Service

@Service
class RedundancyService(val redundancyRepository: RedundancyRepository) {
    fun getOneMethodClassWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ClassVO>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val oneMethodClassCount = redundancyRepository.getOneMethodClassCount(systemId, limit, offset)
        val oneMethodClassList = redundancyRepository.getOneMethodClass(systemId, limit, offset)
        return (oneMethodClassCount to oneMethodClassList)
    }

    fun getOneFieldClassWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ClassVO>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val oneFieldClassCount = redundancyRepository.getOneFieldClassCount(systemId, limit, offset)
        val oneFieldClassList = redundancyRepository.getOneFieldClass(systemId, limit, offset)
        return (oneFieldClassCount to oneFieldClassList)
    }

}
