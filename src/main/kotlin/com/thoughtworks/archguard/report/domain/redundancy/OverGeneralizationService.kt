package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.module.ClassVO
import org.springframework.stereotype.Service

@Service
class OverGeneralizationService(val overGeneralizationRepository: OverGeneralizationRepository) {
    fun getOneExtendsWithTotalCountOld(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ClassVO>> {
        val count = overGeneralizationRepository.getOverGeneralizationCount(systemId)
        val list = overGeneralizationRepository.getOverGeneralizationList(systemId, limit, offset)
        return (count to list)
    }

    fun getOneExtendsWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<OverGeneralizationPair>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val parentClasses = overGeneralizationRepository.getOverGeneralizationParentClassId(systemId)
        val pairList = overGeneralizationRepository.getOverGeneralizationPairList(parentClasses, limit, offset)
        return (parentClasses.size.toLong() to pairList)
    }

    fun getRedundantReport(systemId: Long): Map<BadSmellType, Long> {
        val overGeneralizationCount = overGeneralizationRepository.getOverGeneralizationCount(systemId)
        return mapOf((BadSmellType.OVER_GENERALIZATION to overGeneralizationCount))
    }


}
