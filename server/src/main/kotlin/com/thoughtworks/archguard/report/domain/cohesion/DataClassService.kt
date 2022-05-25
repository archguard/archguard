package com.thoughtworks.archguard.report.domain.cohesion

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import org.springframework.stereotype.Service

@Service
class DataClassService(val dataClassRepository: DataClassRepository) {
    fun getDataClassWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<DataClass>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val dataClassCount = dataClassRepository.getAllDataClassCount(systemId)
        val dataClassList = dataClassRepository.getAllDataClass(systemId, limit, offset)
        return (dataClassCount to dataClassList)
    }

    fun getCohesionReport(systemId: Long): Map<BadSmellType, Long> {
        val count = dataClassRepository.getAllDataClassCount(systemId)
        return mapOf((BadSmellType.DATA_CLASS to count))
    }
}
