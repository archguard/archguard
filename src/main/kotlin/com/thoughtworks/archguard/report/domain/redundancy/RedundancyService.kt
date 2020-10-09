package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.module.ClassVO
import org.springframework.stereotype.Service

@Service
class RedundancyService(val redundancyRepository: RedundancyRepository, val dataClassRepository: DataClassRepository) {
    fun getOneMethodClassWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ClassVO>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val oneMethodClassCount = redundancyRepository.getOneMethodClassCount(systemId)
        val oneMethodClassList = redundancyRepository.getOneMethodClass(systemId, limit, offset)
        return (oneMethodClassCount to oneMethodClassList)
    }

    fun getOneFieldClassWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<DataClass>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val oneFieldClassCount = dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)
        val oneFieldClassList = dataClassRepository.getAllDataClassWithOnlyOneField(systemId, limit, offset)
        return (oneFieldClassCount to oneFieldClassList)
    }

    fun getRedundantReport(systemId: Long): Map<BadSmellType, Long> {
        val oneMethodCount = redundancyRepository.getOneMethodClassCount(systemId)
        val oneFieldCount = dataClassRepository.getAllDataClassWithOnlyOneFieldCount(systemId)

        return mapOf((BadSmellType.REDUNDANT_ELEMENT to oneMethodCount + oneFieldCount))
    }

}
